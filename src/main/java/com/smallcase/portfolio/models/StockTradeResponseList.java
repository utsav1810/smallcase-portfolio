package com.smallcase.portfolio.models;

import java.util.List;

public class StockTradeResponseList {

    private List<StockTradeResponse> stockTradeResponse;

    public List<StockTradeResponse> getStockTradeResponse() {
        return stockTradeResponse;
    }

    public void setStockTradeResponse(List<StockTradeResponse> stockTradeResponse) {
        this.stockTradeResponse = stockTradeResponse;
    }

    @Override
    public String toString() {
        return "StockTradeResponseList{" +
                "stockTradeResponse=" + stockTradeResponse +
                '}';
    }
}
