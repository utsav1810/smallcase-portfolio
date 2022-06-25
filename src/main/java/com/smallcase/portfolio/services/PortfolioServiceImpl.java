package com.smallcase.portfolio.services;

import com.smallcase.portfolio.dao.StockRepository;
import com.smallcase.portfolio.dao.TradeRepository;
import com.smallcase.portfolio.helpers.Constants;
import com.smallcase.portfolio.exception.PortfolioException;
import com.smallcase.portfolio.models.ReturnResponse;
import com.smallcase.portfolio.models.Stock;
import com.smallcase.portfolio.models.StockTradeResponse;
import com.smallcase.portfolio.models.Trade;
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

        validateTrade(trade);

        double stockAveragePrice = 0d;
        int stockQty = 0;

        Stock stock = fetchStockInfo(trade.getTicker());

        boolean isStockOperationSuccessful;

        if(stock == null){
            if (Constants.SELL.equals(trade.getType()))
                throw new PortfolioException(HttpStatus.BAD_REQUEST, "Buy some shares first.");
            isStockOperationSuccessful = createStock(new Stock(trade.getTicker(), trade.getPreviousAvgPrice(), trade.getQty()));
        } else {
            stockAveragePrice = stock.getAveragePrice();
            stockQty = stock.getQty();

            if (Constants.SELL.equals(trade.getType()) && trade.getQty() > stockQty)
                throw new PortfolioException(HttpStatus.BAD_REQUEST, "Not enough shares to sell. You can sell only " + stock.getQty() + " shares.");

            else if (Constants.SELL.equals(trade.getType()) && trade.getQty() == stockQty)
                isStockOperationSuccessful = deleteStock(stock.getTicker());

            else
                isStockOperationSuccessful = updateStockDetails(trade, stock);
        }

        if(!isStockOperationSuccessful)
            throw new PortfolioException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while executing your trade");

        trade.setPreviousAvgPrice(stockAveragePrice);
        trade.setPreviousQty(stockQty);
        trade.setStatus(Constants.SUCCESS);
        trade.setTimestamp(new Date());
        trade.generateAutoId();

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
        for(Stock stock: stocks){
            double returnAmount = (stock.getAveragePrice()-Constants.CURRENT_PRICE) * stock.getQty();
            double returnPercentage = returnAmount/Constants.CURRENT_PRICE * 100;
            returnResponse.setReturnAmount(returnAmount);
            returnResponse.setReturnPercentage(returnPercentage);
        }
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
            double totalBuyAvgPrice = ((tradeQty * tradeAveragePrice) + (stockAveragePrice * stockQty)) / totalQty;
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
     * It will consider different conditions based on ticker symbol, price, qty and type of trade
     *
     * @param trade     -   trade input to validate
     */
    private void validateTrade(Trade trade){
        if(!isTickerValid(trade.getTicker()))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "This company is not listed. Valid listed companies are: " + alValidTicker);

        if(!Constants.SELL.equals(trade.getType()) && !Constants.BUY.equals(trade.getType()))
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Type has to be BUY or SELL");

        if (trade.getQty() <= 0)
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Quantity has to be greater than 0");

        if (trade.getPrice() <= 0)
            throw new PortfolioException(HttpStatus.BAD_REQUEST, "Price has to be greater than 0");
    }
}

