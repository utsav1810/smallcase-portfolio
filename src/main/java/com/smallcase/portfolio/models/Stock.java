package com.smallcase.portfolio.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    private int id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "qty")
    private int qty;

    @Column(name = "average_price")
    private double averagePrice;

    public Stock() {
    }

    public Stock(int id, String ticker, double averagePrice, int qty) {
        this.id = id;
        this.ticker = ticker;
        this.qty = qty;
        this.averagePrice = averagePrice;
    }

    public Stock(String ticker, double averagePrice, int qty) {
        this.ticker = ticker;
        this.qty = qty;
        this.averagePrice = averagePrice;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", ticket='" + ticker + '\'' +
                ", qty=" + qty +
                ", averagePrice=" + averagePrice +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
