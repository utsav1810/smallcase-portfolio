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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class PortfolioServiceImpl implements PortfolioService{

    public static final List<String> alValidTicker = Arrays.asList(Constants.TCS, Constants.WIPRO, Constants.GODREJIND);

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
        if(!isTickerValid(ticker))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "This company is not listed. Valid listed companies are: " + alValidTicker);
        return stockRepository.findByTicker(ticker);
    }

    @Override
    public Trade executeTrade(Trade trade) {

        //Pre validation of a trade input
        prevalidateTrade(trade);

        //Create trade entry in DB
        trade.setStatus(Constants.STARTED);
        trade.setTimestamp(new Date());
        trade.generateAutoId();
        Trade tradeStarted = tradeRepository.save(trade);

        //Validate trade object and check for corner conditions
        TradeValidation tradeValidation = validateTrade(trade);
        if(!tradeValidation.isValid())
            return markTradeFailed(tradeStarted, tradeValidation.getErrorMessage());

        double stockAveragePrice = 0d;
        int stockQty = 0;
        boolean isStockOperationSuccessful;

        Stock stock = fetchStockInfo(trade.getTicker());

        //If user wants to sell shares but does not own the stock itself
        if(stock == null){
            if (Constants.SELL.equals(trade.getType()))
                return markTradeFailed(tradeStarted, "Buy some shares first");
            isStockOperationSuccessful = createStock(new Stock(trade.getTicker(), Utility.round(trade.getPrice(), 2), trade.getQty()));
        } else {
            stockAveragePrice = stock.getAveragePrice();
            stockQty = stock.getQty();

            //If user wants to sell more shares than they own
            if (Constants.SELL.equals(trade.getType()) && trade.getQty() > stockQty)
                return markTradeFailed(tradeStarted, "Not enough shares to sell. You can sell only " + stock.getQty() + " shares");

            //If user wants to sell all shares
            if (Constants.SELL.equals(trade.getType()) && trade.getQty() == stockQty)
                isStockOperationSuccessful = deleteStock(stock.getTicker());

            //If user wants to sell some shares or buy some shares
            else
                isStockOperationSuccessful = updateStockDetails(trade, stock);
        }

        if(!isStockOperationSuccessful)
            throw new PortfolioException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while executing your trade");

        trade.setPreviousAvgPrice(stockAveragePrice);
        trade.setPreviousQty(stockQty);
        trade.setStatus(Constants.SUCCESS);

        return tradeRepository.save(trade);
    }

    /**
     * This trade will be marked as FAILED in the DB with given error message
     *
     * @param trade             -       Existing trade object
     * @param errorMessage      -       Error message to store
     * @return Trade
     */
    private Trade markTradeFailed(Trade trade, String errorMessage) {
        System.out.println("Trade failed :" + trade + "\nReason: " + errorMessage);
        trade.setStatus(Constants.FAILED);
        trade.setErrorMessage(errorMessage);
        return tradeRepository.save(trade);
    }

    @Override
    public List<StockTradeResponse> fetchAllTrades() {

        List<Stock> stocks = stockRepository.findAll(Sort.by(Sort.Direction.ASC, "ticker"));
        List<StockTradeResponse> response = new ArrayList<>();

        for(Stock stock: stocks){
            List<Trade> trade = tradeRepository.findByTicker(stock.getTicker(), Sort.by(Sort.Direction.DESC, "timestamp"));
            response.add(new StockTradeResponse(stock, trade));
        }

        return response;
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

    /**
     * This method will update stock details based on a given trade
     * It will process 2 types of trade - BUY/SELL
     * It will update qty and average buy price of a stock
     *
     * @param trade     -   Trade to be executed
     * @param stock     -   Current stock object in portfolio
     * @return boolean  -   operation success = true/false
     */
    private boolean updateStockDetails(Trade trade, Stock stock){
        double stockAveragePrice = stock.getAveragePrice();
        int stockQty = stock.getQty();

        int tradeQty = trade.getQty();
        double tradeAveragePrice = trade.getPrice();

        if (Constants.BUY.equals(trade.getType())) {
            int totalQty = tradeQty + stockQty;
            double totalBuyAvgPrice = Utility.round(((tradeQty * tradeAveragePrice) + (stockAveragePrice * stockQty)) / totalQty, 2);
            stock.setAveragePrice(totalBuyAvgPrice);
            stock.setQty(totalQty);
        } else if (Constants.SELL.equals(trade.getType())) {
            int totalQty = stockQty - tradeQty;
            stock.setQty(totalQty);
        }
        return updateStock(stock);
    }

    /**
     * This method will update a stock entry in the database
     *
     * @param stock     -   Stock object to update in db
     * @return boolean  -   operation success = true/false
     */
    public boolean updateStock(Stock stock) {
        try {
            stockRepository.save(stock);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * This method will delete a stock entry from the database
     *
     * @param ticker        -   ticker symbol for which entry needs to be deleted
     * @return boolean      -   operation success = true/false
     */
    public Boolean deleteStock(String ticker) {
        try {
            stockRepository.deleteByTicker(ticker);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * This method will create a stock entry in the database
     *
     * @param stock     -   Stock object to create in db
     * @return boolean  -   operation success = true/false
     */
    public boolean createStock(Stock stock) {
        try {
            stockRepository.save(stock);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method will check if given ticker symbol is valid or not
     *
     * @param ticker        -   ticker symbol to verify
     * @return boolean      -   valid = true/false
     */
    private boolean isTickerValid(String ticker){
        return alValidTicker.contains(ticker);
    }

    /**
     * This method will validate a trade
     * It will consider different conditions based on price and qty
     *
     * @param trade                 -   trade input to validate
     * @return TradeValidation      -   TradeValidation object
     */
    private TradeValidation validateTrade(Trade trade){
        TradeValidation tradeValidation = new TradeValidation();
        tradeValidation.setValid(true);

        if (trade.getQty() <= 0)
            tradeValidation.setErrorMessage("Quantity has to be greater than 0");

        else if (trade.getPrice() <= 0)
            tradeValidation.setErrorMessage("Price has to be greater than 0");

        return tradeValidation;
    }

    /**
     * This method will validate a trade before making any entry
     * It will consider different conditions based on ticker symbol and type of trade
     *
     * @param trade                 -   trade input to validate
     */
    private void prevalidateTrade(Trade trade){
        if(!isTickerValid(trade.getTicker()))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "This company is not listed. Valid listed companies are: " + alValidTicker);

        else if(!Constants.SELL.equals(trade.getType()) && !Constants.BUY.equals(trade.getType()))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Type has to be BUY or SELL");

    }
}

