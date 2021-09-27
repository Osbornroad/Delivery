package com.gmail.osbornroad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="part_qty")
public class PartQty {

    @EmbeddedId
    PartQtyKey id;

    @ManyToOne
    @MapsId("partId")
    @JsonIgnore
    @JoinColumn(name = "part_id")
    Part part;

    @ManyToOne
    @MapsId("finishPartId")
    @JsonIgnore
    @JoinColumn(name = "finishPart_id")
    FinishPart finishPart;

    int qty;

    public PartQty() {
    }

    public PartQty(PartQtyKey id, Part part, FinishPart finishPart) {
        this.id = id;
        this.part = part;
        this.finishPart = finishPart;
    }

    public PartQty(PartQtyKey id, Part part, FinishPart finishPart, int qty) {
        this.id = id;
        this.part = part;
        this.finishPart = finishPart;
        this.qty = qty;
    }

    public PartQtyKey getId() {
        return id;
    }

    public void setId(PartQtyKey id) {
        this.id = id;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public FinishPart getFinishPart() {
        return finishPart;
    }

    public void setFinishPart(FinishPart finishPart) {
        this.finishPart = finishPart;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
