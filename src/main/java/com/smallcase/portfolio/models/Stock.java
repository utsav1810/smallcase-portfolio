package com.smallcase.portfolio.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author utsav
 * This POJO is a db model respresentation of postgres stock table
 * It is used for all db operations and in memory operation on stocks
 */
@Entity
@Table(name = "stock")
public class Stock {

    /* Ticker symbol of a stock
    * Every listed security has a unique ticker symbol,
    * facilitating the vast array of trade orders that flow through the financial markets every day.
    */
    @Id
    @Column(name = "ticker")
    private String ticker;

    /* Quantity of stock in a portfolio */
    @Column(name = "qty")
    @JsonProperty("quantity")
    private int qty;

    /* Average buying price of stock */
    @Column(name = "average_price")
    @JsonProperty("averageBuyPrice")
    private double averagePrice;

    public Stock() {
    }

    public Stock(String ticker, double averagePrice, int qty) {
        this.ticker = ticker;
        this.qty = qty;
        this.averagePrice = averagePrice;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "ticket='" + ticker + '\'' +
                ", qty=" + qty +
                ", averagePrice=" + averagePrice +
                '}';
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }
}
