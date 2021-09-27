package com.gmail.osbornroad.model;

import java.io.Serializable;


public class PartQtyDTO implements Serializable {

    private Integer partId;

    private Integer finishPartId;

    private String partNumber;

    private Integer qty;

    public PartQtyDTO() {
    }

    public PartQtyDTO(Integer partId, Integer finishPartId, Integer qty) {
        this.partId = partId;
        this.finishPartId = finishPartId;
        this.qty = qty;
    }

    public PartQtyDTO(Integer partId, Integer finishPartId, String partNumber, Integer qty) {
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
}
