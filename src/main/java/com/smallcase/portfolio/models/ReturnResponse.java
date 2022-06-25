package com.smallcase.portfolio.models;

/**
 * @author utsav
 * This class is used to send response to a user about a portfolio's current profit/loss
 */
public class ReturnResponse {

    /* Current profit/loss of the portfolio in amount */
    private double returnAmount;

    /* Current profit/loss of the portfolio in percentage */
    private double returnPercentage;

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public double getReturnPercentage() {
        return returnPercentage;
    }

    public void setReturnPercentage(double returnPercentage) {
        this.returnPercentage = returnPercentage;
    }
}
