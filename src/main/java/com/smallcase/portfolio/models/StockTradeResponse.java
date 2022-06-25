package com.smallcase.portfolio.models;

import java.util.List;

public class StockTradeResponse extends Stock{

    private List<Trade> trades;

    public StockTradeResponse(int id, String ticker, double averagePrice, int qty) {
        super(id, ticker, averagePrice, qty);
    }

    public StockTradeResponse(Stock stock, List<Trade> trades) {
        super(stock.getId(), stock.getTicker(), stock.getAveragePrice(), stock.getQty());
        this.trades = trades;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

}
