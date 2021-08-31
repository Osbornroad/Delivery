package com.gmail.osbornroad.model;


import java.time.LocalDate;

public class UnitEDI {

    private String partNumber;
    private Integer quantity;
    private LocalDate date;

    public UnitEDI(String partNumber, Integer quantity, LocalDate date) {
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.date = date;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitEDI unitEDI = (UnitEDI) o;

        if (partNumber != null ? !partNumber.equals(unitEDI.partNumber) : unitEDI.partNumber != null) return false;
        return date != null ? date.equals(unitEDI.date) : unitEDI.date == null;
    }

    @Override
    public int hashCode() {
        int result = partNumber != null ? partNumber.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
