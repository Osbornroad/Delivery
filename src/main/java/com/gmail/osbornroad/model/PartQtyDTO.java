package com.gmail.osbornroad.model;

import java.io.Serializable;


public class PartQtyDTO implements Serializable {

    private Integer partId;

    private Integer finishPartId;

    private String partNumber;

    private double qty;

    public PartQtyDTO() {
    }

    public PartQtyDTO(Integer partId, Integer finishPartId, double qty) {
        this.partId = partId;
        this.finishPartId = finishPartId;
        this.qty = qty;
    }

    public PartQtyDTO(Integer partId, Integer finishPartId, String partNumber, double qty) {
        this.partId = partId;
        this.finishPartId = finishPartId;
        this.partNumber = partNumber;
        this.qty = qty;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public Integer getFinishPartId() {
        return finishPartId;
    }

    public void setFinishPartId(Integer finishPartId) {
        this.finishPartId = finishPartId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
}
