package com.w4ma.soft.tamenly.CategoryActivities.Models;


public class CurrencyList {


    private Integer error;
    private String errorMessage;
    private Double amount;


    public CurrencyList() {
    }


    public CurrencyList(Integer error, String errorMessage, Double amount) {
        super();
        this.error = error;
        this.errorMessage = errorMessage;
        this.amount = amount;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}