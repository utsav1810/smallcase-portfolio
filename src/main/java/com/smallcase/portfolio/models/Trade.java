package com.smallcase.portfolio.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smallcase.portfolio.helpers.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.ConcurrentModificationException;
import java.util.Date;

/**
 * @author utsav
 * This POJO is a db model respresentation of postgres trade table
 * It is used for all db operations and in memory operation on trades
 */
@Entity
@Table(name = "trade")
public class Trade {

    /* Randomly generate Id for every trade transaction */
    @Id
    @Column(name = "id")
    private String id;


    @Column(name = "ticker")
    private String ticker;

    /* Error message in case of trade failure */
    @Column(name = "error_message")
    private String errorMessage;

    /* Status of a trade - STARTED / FAILED / SUCCESS / REMOVED */
    @Column(name = "status")
    private String status;

    /* Quantity on which order was placed in a trade */
    @Column(name = "qty")
    private int qty;

    /* Price on which order was placed in a trade */
    @Column(name = "price")
    private double price;

    /* Type of trade - BUY / SELL */
    @Column(name = "type")
    private String type;

    /* time stamp on which trade was executed */
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /* previous quantity of stock in portfolio when trade order was received */
    @Column(name = "prev_qty")
    @JsonIgnore
    private int previousQty;

    /* previous average buying price of stock in portfolio when trade order was received */
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.setStatus(Constants.FAILED);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id='" + id + '\'' +
                ", ticker='" + ticker + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", status='" + status + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", previousQty=" + previousQty +
                ", previousAvgPrice=" + previousAvgPrice +
                '}';
    }

    public void generateAutoId(){
        this.id = RandomStringUtils.randomAlphanumeric(10) + System.currentTimeMillis();
    }
}
