package com.smallcase.portfolio.services;

import com.smallcase.portfolio.models.ReturnResponse;
import com.smallcase.portfolio.models.Stock;
import com.smallcase.portfolio.models.StockTradeResponse;
import com.smallcase.portfolio.models.Trade;

import java.util.List;

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
     * @param ticker    -   ticker sybmol of stock
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
    List<StockTradeResponse> fetchAllTrades();

    /**
     * This method will calculate the current cumulative returns of a portfolio
     * based on average buy price and current price of stocks
     *
     * @return ReturnResponse
     */
    ReturnResponse fetchReturns();
}
