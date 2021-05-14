package com.gmail.osbornroad.model;

import java.time.LocalDate;

public class ReportA090 {

    private String kitName;
    private LocalDate aPointDate;
    private Integer quantity;

    public ReportA090(String kitName, LocalDate aPointDate, Integer quantity) {
        this.kitName = kitName;
        this.aPointDate = aPointDate;
        this.quantity = quantity;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public LocalDate getaPointDate() {
        return aPointDate;
    }

    public void setaPointDate(LocalDate aPointDate) {
        this.aPointDate = aPointDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
