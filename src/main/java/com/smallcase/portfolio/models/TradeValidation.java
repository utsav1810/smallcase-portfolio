package com.smallcase.portfolio.models;

public class TradeValidation {
    private boolean valid;
    private String errorMessage;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.setValid(false);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "TradeValidation{" +
                "valid=" + valid +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
