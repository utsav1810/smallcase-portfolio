package com.smallcase.portfolio.services;

import com.smallcase.portfolio.dao.StockRepository;
import com.smallcase.portfolio.dao.TradeRepository;
import com.smallcase.portfolio.helpers.Constants;
import com.smallcase.portfolio.exception.PortfolioException;
import com.smallcase.portfolio.helpers.Utility;
import com.smallcase.portfolio.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is an implementation class of portfolio service.
 * It has all necessary services implemented end-to-end and exposed for other services/controllers to consume
 */
@Component
public class PortfolioServiceImpl implements PortfolioService{

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public List<Stock> fetchPortfolio() {
        return stockRepository.findAll();
    }

    @Override
    public Stock fetchStockInfo(String ticker) {
        if(!Utility.isTickerValid(ticker))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "This company is not listed. Valid listed companies are: " + Utility.alValidTicker);
        return stockRepository.findByTicker(ticker);
    }

    @Override
    @Transactional
    public Trade executeTrade(Trade trade) {

        //Pre validation of a trade input
        preValidateTrade(trade);

        //Create trade entry in DB
        trade.setStatus(Constants.STARTED);
        trade.setTimestamp(new Date());
        trade.generateAutoId();
        trade = tradeRepository.save(trade);

        trade.setPreviousQty(0);
        trade.setPreviousAvgPrice(0d);

        updateStockRecordForTrade(trade);
        trade.setErrorMessage(null);
        trade.setStatus(Constants.SUCCESS);

        return tradeRepository.save(trade);
    }

    /**
     * This method will update stock table record for a trade
     *
     * @param trade     -   trade object with details
     */
    private void updateStockRecordForTrade(Trade trade) {
        Stock stock = fetchStockInfo(trade.getTicker());

        //If user wants to sell shares but does not own the stock, throw error
        if(stock == null){
            if (Utility.isTradeTypeSell(trade))
                markTradeFailed(trade, "Buy some shares first");
            stockRepository.save(new Stock(trade.getTicker(), Utility.round(trade.getPrice(), 2), trade.getQty()));
        } else {
            int stockQty = stock.getQty();
            trade.setPreviousQty(stockQty);
            trade.setPreviousAvgPrice(stock.getAveragePrice());

            //If user wants to sell more shares than they own
            if (Utility.isTradeTypeSell(trade) && trade.getQty() > stockQty)
                markTradeFailed(trade, "Not enough shares to sell. You can sell only " + stock.getQty() + " shares");

            //If user wants to sell all shares
            if (Utility.isTradeTypeSell(trade) && trade.getQty() == stockQty)
                stockRepository.delete(stock);

            //If user wants to sell some shares or buy some shares
            else
                updateStockDetails(trade, stock);
        }
    }

    /**
     * This trade will be marked as FAILED in the DB with given error message
     *
     * @param trade             -       Existing trade object
     * @param errorMessage      -       Error message to store
     */
    private void markTradeFailed(Trade trade, String errorMessage) {
        System.out.println("Trade failed :" + trade + "\nReason: " + errorMessage);
        trade.setStatus(Constants.FAILED);
        trade.setErrorMessage(errorMessage);
        tradeRepository.save(trade);
        throw new PortfolioException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Override
    public StockTradeResponseList fetchAllTrades() {

        List<Stock> stocks = stockRepository.findAll(Sort.by(Sort.Direction.ASC, "ticker"));
        StockTradeResponseList responseList = new StockTradeResponseList();
        List<StockTradeResponse> response = new ArrayList<>();
        responseList.setStockTradeResponse(response);

        for(Stock stock: stocks){
            List<Trade> trade = tradeRepository.findByTicker(stock.getTicker(), Sort.by(Sort.Direction.DESC, "timestamp"));
            response.add(new StockTradeResponse(stock, trade));
        }

        return responseList;
    }

    @Override
    public ReturnResponse fetchReturns() {
        ReturnResponse returnResponse = new ReturnResponse();
        List<Stock> stocks = fetchPortfolio();
        double returnAmount = 0d;
        double returnPercentage = 0d;
        double totalBuyPrice = 0d;
        for(Stock stock: stocks){
            double buyingPrice = stock.getAveragePrice();
            int quantity = stock.getQty();
            totalBuyPrice += buyingPrice*quantity;
            returnAmount += Utility.round((Constants.CURRENT_PRICE - buyingPrice) * quantity, 2);
        }
        if(totalBuyPrice != 0)
            returnPercentage = Utility.round((returnAmount * 100)/(totalBuyPrice) , 2);
        returnResponse.setReturnAmount(returnAmount);
        returnResponse.setReturnPercentage(returnPercentage);
        return returnResponse;
    }

    @Override
    @Transactional
    public Trade deleteTrade(String ticker) {

        if(!Utility.isTickerValid(ticker))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "This company is not listed. Valid listed companies are: " + Utility.alValidTicker);

        //get last executed trade (FAILED/SUCCESS) for given ticker
        Trade lastTradeForTicker = tradeRepository.findTopByTickerOrderByTimestampDesc(ticker);
        if(lastTradeForTicker == null)
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Previous trade not found for: " + ticker);

        String status = lastTradeForTicker.getStatus();

        //if last trade was a success, then we need to do a rollback on stock table
        if(!Constants.FAILED.equals(status))
            rollBackTrade(lastTradeForTicker);

        //remove trade entry from trade table
        tradeRepository.delete(lastTradeForTicker);

        return lastTradeForTicker;
    }

    @Override
    @Transactional
    public Trade updateTrade(Trade trade) {

        //Trade validation - we will proceed for update operation only if basic trade validation is successful
        preValidateTrade(trade);

        //get last executed trade (FAILED/SUCCESS) for given ticker
        String ticker = trade.getTicker();
        Trade lastTradeForTicker = tradeRepository.findTopByTickerOrderByTimestampDesc(ticker);
        if(lastTradeForTicker == null)
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Previous trade not found for: " + ticker);

        String status = lastTradeForTicker.getStatus();

        //if last trade was a success, then we need to do a rollback on stock table
        if(!Constants.FAILED.equals(status))
            rollBackTrade(lastTradeForTicker);

        trade.setId(lastTradeForTicker.getId());
        trade.setPreviousAvgPrice(lastTradeForTicker.getPreviousAvgPrice());
        trade.setPreviousQty(lastTradeForTicker.getPreviousQty());
        trade.setTimestamp(new Date());

        updateStockRecordForTrade(trade);
        trade.setStatus(Constants.SUCCESS);
        trade.setErrorMessage(null);

        return tradeRepository.save(trade);
    }

    /**
     * This method will update stock details based on a given trade
     * It will process 2 types of trade - BUY/SELL
     * It will update qty and average buy price of a stock
     *
     * @param trade     -   Trade to be executed
     * @param stock     -   Current stock object in portfolio
     */
    private void updateStockDetails(Trade trade, Stock stock){
        double stockAveragePrice = stock.getAveragePrice();
        int stockQty = stock.getQty();

        int tradeQty = trade.getQty();
        double tradeAveragePrice = trade.getPrice();

        if (Utility.isTradeTypeBuy(trade)) {
            int totalQty = tradeQty + stockQty;
            double totalBuyAvgPrice = Utility.round(((tradeQty * tradeAveragePrice) + (stockAveragePrice * stockQty)) / totalQty, 2);
            stock.setAveragePrice(totalBuyAvgPrice);
            stock.setQty(totalQty);
        } else {
            int totalQty = stockQty - tradeQty;
            stock.setQty(totalQty);
        }
        stockRepository.save(stock);
    }

    /**
     * This method will validate a trade before making any entry
     * It will consider different conditions based on ticker symbol, type of trade, qty, price, etc
     *
     * @param trade                 -   trade input to validate
     */
    private void preValidateTrade(Trade trade){
        if(!Utility.isTickerValid(trade.getTicker()))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "This company is not listed. Valid listed companies are: " + Utility.alValidTicker);

        else if(!Utility.isTradeTypeBuy(trade) && !Utility.isTradeTypeSell(trade))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Type has to be BUY or SELL");

        else if (trade.getQty() <= 0)
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Quantity has to be greater than 0");

        else if (trade.getPrice() <= 0)
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Price has to be greater than 0");

    }

    /**
     * This method will perform rollback operation for an executed trade
     * @param trade     -   Trade entry to rollback
     */
    private void rollBackTrade(Trade trade){

        int previousQty = trade.getPreviousQty();
        double previousAvgPrice = trade.getPreviousAvgPrice();
        Stock stock = new Stock(trade.getTicker(), previousAvgPrice, previousQty);

        if(Utility.isTradeTypeSell(trade)) {
            stockRepository.save(stock);
        } else {
            //If user had some shares of this stock before last operation, update the qty and avgPrice to old values
            if(previousQty != 0)
                stockRepository.save(stock);
            //If user had 0 shares of this stock before last operation, remove the entry from stock db
            else
                stockRepository.delete(stock);
        }
    }
}