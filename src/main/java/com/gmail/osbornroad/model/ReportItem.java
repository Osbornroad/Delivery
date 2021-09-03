package com.gmail.osbornroad.model;

public class ReportItem {

    private String kitName;
    private Integer integerDate;
    private Integer quantity;

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public Integer getIntegerDate() {
        return integerDate;
    }

    public void setIntegerDate(Integer integerDate) {
        this.integerDate = integerDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ReportItem(String kitName, Integer integerDate, Integer quantity) {
        this.kitName = kitName;
        this.integerDate = integerDate;
        this.quantity = quantity;


    }
}
