package com.smallcase.portfolio.services;

import com.smallcase.portfolio.models.*;

import java.util.List;

/**
 * @author utsav
 * This interface defines all services which portfolio platform will cater to
 */
public interface PortfolioService {

    /**
     * This method will return an aggregate view of all stocks in the portfolio with its
     * final quantity and average buy price
     *
     * @return List<Stock>
     */
    List<Stock> fetchPortfolio();

    /**
     * This method will fetch information about a particular stock in portfolio
     *
     * @param ticker    -   ticker symbol of stock
     * @return Stock
     */
    Stock fetchStockInfo(String ticker);

    /**
     * This method will execute the given trade on a portfolio
     * It will update the stock's quantity and average buy price as impacted
     *
     * @param trade     -   trade object containing details about the trade to be performed
     * @return Trade
     */
    Trade executeTrade(Trade trade);

    /**
     * This method will fetch all stocks and their respective trades
     *
     * @return List<StockTradeResponse>
     */
    StockTradeResponseList fetchAllTrades();

    /**
     * This method will calculate the current cumulative returns of a portfolio
     * based on average buy price and current price of stocks
     *
     * @return ReturnResponse
     */
    ReturnResponse fetchReturns();

    /**
     * This method will delete the last executed trade - FAILED/SUCCESS status
     * @param ticker        -   ticker symbol of stock
     * @return Trade
     */
    Trade deleteTrade(String ticker);

    /**
     * This method will update the last executed trade - FAILED/SUCCESS status
     * @param trade     -   trade object containing details about the trade to be updated
     * @return Trade
     */
    Trade updateTrade(Trade trade);
}
