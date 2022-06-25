package com.smallcase.portfolio.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trade")
public class Trade {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "status")
    private String status;

    @Column(name = "qty")
    private int qty;

    @Column(name = "price")
    private double price;

    @Column(name = "type")
    private String type;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "prev_qty")
    @JsonIgnore
    private int previousQty;

    @Column(name = "prev_avg_price")
    @JsonIgnore
    private double previousAvgPrice;

    public Trade(){}

    public Trade(String id, String ticker, String status, int qty, double price, String type, Date timestamp, int previousQty, double previousAvgPrice) {
        this.id = id;
        this.ticker = ticker;
        this.status = status;
        this.qty = qty;
        this.price = price;
        this.type = type;
        this.timestamp = timestamp;
        this.previousQty = previousQty;
        this.previousAvgPrice = previousAvgPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getPreviousQty() {
        return previousQty;
    }

    public void setPreviousQty(int previousQty) {
        this.previousQty = previousQty;
    }

    public double getPreviousAvgPrice() {
        return previousAvgPrice;
    }

    public void setPreviousAvgPrice(double previousAvgPrice) {
        this.previousAvgPrice = previousAvgPrice;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", status=" + status +
                ", qty=" + qty +
                ", price=" + price +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", previousQty=" + previousQty +
                ", previousAvgPrice=" + previousAvgPrice +
                '}';
    }

    public void generateAutoId(){
        this.id = RandomStringUtils.randomAlphanumeric(10) + System.currentTimeMillis();
    }
}
