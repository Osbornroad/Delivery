package com.gmail.osbornroad.model;

public class OrderCheck {

    private String partNumber;
    private int necessaryAmount;
    private int ordered;
    private int diff;

    public OrderCheck() {
    }

    public OrderCheck(String partNumber,
                      int necessaryAmount, int ordered, int diff) {
        this.partNumber = partNumber;
        this.necessaryAmount = necessaryAmount;
        this.ordered = ordered;
        this.diff = diff;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getNecessaryAmount() {
        return necessaryAmount;
    }

    public void setNecessaryAmount(int necessaryAmount) {
        this.necessaryAmount = necessaryAmount;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }
}
