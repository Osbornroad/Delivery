package com.gmail.osbornroad.model;

public class MonthlyReportItem {

    private String kitName;
    private Integer yearMonth;
    private Integer quantity;

    public MonthlyReportItem(String kitName, Integer yearMonth, Integer quantity) {
        this.kitName = kitName;
        this.yearMonth = yearMonth;
        this.quantity = quantity;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public Integer getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
