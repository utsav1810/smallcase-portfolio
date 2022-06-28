package com.smallcase.portfolio.helpers;

import com.smallcase.portfolio.models.Trade;

import java.util.Arrays;
import java.util.List;

/**
 * @author utsav
 * This is a utility helper class
 * We define al trivial utility functions here
 */
public class Utility {

    public static final List<String> alValidTicker = Arrays.asList(Constants.TCS, Constants.WIPRO, Constants.GODREJIND);

    /**
     * This method is used to round up values for given decimals
     *
     * @param value         -   number to round off
     * @param places        -   number of decimal places
     * @return double       -   rounded up values
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * This method return if trade type is BUY
     * @param trade     -   trade object with type info
     * @return boolean
     */
    public static boolean isTradeTypeBuy(Trade trade){
        return Constants.BUY.equals(trade.getType());
    }

    /**
     * This method return if trade type is SELL
     * @param trade     -   trade object with type info
     * @return boolean
     */
    public static boolean isTradeTypeSell(Trade trade){
        return Constants.SELL.equals(trade.getType());
    }

    /**
     * This method will check if given ticker symbol is valid or not
     *
     * @param ticker        -   ticker symbol to verify
     * @return boolean      -   valid = true/false
     */
    public static boolean isTickerValid(String ticker){
        return alValidTicker.contains(ticker);
    }
}
