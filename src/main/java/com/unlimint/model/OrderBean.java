package com.unlimint.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderBean {

    private int id;
    private int orderId;
    private double amount;
    private String comment;
    @JsonIgnore
    private String currenncy;
    private String filename;
    private int line;
    private String result;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(final int orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getCurrenncy() {
        return currenncy;
    }

    public void setCurrenncy(String currenncy) {
        this.currenncy = currenncy;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
