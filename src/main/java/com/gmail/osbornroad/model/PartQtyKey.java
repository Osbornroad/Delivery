package com.gmail.osbornroad.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PartQtyKey implements Serializable {

    @Column(name = "part_id")
    Integer partId;

    @Column(name = "finshPart_id")
    Integer finishPartId;

    public PartQtyKey() {
    }

    public PartQtyKey(Integer partId, Integer finishPartId) {
        this.partId = partId;
        this.finishPartId = finishPartId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartQtyKey that = (PartQtyKey) o;

        if (partId != null ? !partId.equals(that.partId) : that.partId != null) return false;
        return finishPartId != null ? finishPartId.equals(that.finishPartId) : that.finishPartId == null;
    }

    @Override
    public int hashCode() {
        int result = partId != null ? partId.hashCode() : 0;
        result = 31 * result + (finishPartId != null ? finishPartId.hashCode() : 0);
        return result;
    }
}
