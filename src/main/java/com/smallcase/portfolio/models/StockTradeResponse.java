package com.smallcase.portfolio.models;

import java.util.List;

/**
 * @author utsav
 * This POJO is used to send portfolio detailed response back to user
 * It is used to hold stock and trade related information for every stock
 */
public class StockTradeResponse extends Stock{

    private List<Trade> trades;

    public StockTradeResponse(String ticker, double averagePrice, int qty) {
        super(ticker, averagePrice, qty);
    }

    public StockTradeResponse(Stock stock, List<Trade> trades) {
        super(stock.getTicker(), stock.getAveragePrice(), stock.getQty());
        this.trades = trades;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

}
