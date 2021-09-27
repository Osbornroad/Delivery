package com.gmail.osbornroad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="finishParts")
public class FinishPart extends BaseEntity{

    @NotEmpty
    private String finishPartNumber;

    private Set<Kit> kitSet = new HashSet<>();

    private Integer sortNum;

    private Set<PartQty> partQtySet = new HashSet<>();

    public FinishPart() {
    }

    public FinishPart(@NotEmpty String finishPartNumber, Set<Kit> kitSet, Integer sortNum, Set<PartQty> partQtySet) {
        this.finishPartNumber = finishPartNumber;
        this.kitSet = kitSet;
        this.sortNum = sortNum;
        this.partQtySet = partQtySet;
    }

    public FinishPart(Integer id, @NotEmpty String finishPartNumber, Set<Kit> kitSet, Integer sortNum, Set<PartQty> partQtySet) {
        super(id);
        this.finishPartNumber = finishPartNumber;
        this.kitSet = kitSet;
        this.sortNum = sortNum;
        this.partQtySet = partQtySet;
    }

    public FinishPart(@NotEmpty String finishPartNumber, Set<Kit> kitSet, Integer sortNum) {
        this.finishPartNumber = finishPartNumber;
        this.kitSet = kitSet;
        this.sortNum = sortNum;
    }

    public FinishPart(Integer id, @NotEmpty String finishPartNumber, Set<Kit> kitSet, Integer sortNum) {
        super(id);
        this.finishPartNumber = finishPartNumber;
        this.kitSet = kitSet;
        this.sortNum = sortNum;
    }

    public FinishPart(@NotEmpty String finishPartNumber, Integer sortNum) {
        this.finishPartNumber = finishPartNumber;
        this.sortNum = sortNum;
    }

    public FinishPart(Integer id, @NotEmpty String finishPartNumber, Integer sortNum) {
        super(id);
        this.finishPartNumber = finishPartNumber;
        this.sortNum = sortNum;
    }

    public FinishPart(@NotEmpty String finishPartNumber) {
        this.finishPartNumber = finishPartNumber;
    }

    @OneToMany(mappedBy = "finishPart", fetch = FetchType.EAGER)
    public Set<PartQty> getPartQtySet() {
        return partQtySet;
    }

    public void setPartQtySet(Set<PartQty> partQtySet) {
        this.partQtySet = partQtySet;
    }

    @Column(name = "finishPartNumber")
    public String getFinishPartNumber() {
        return finishPartNumber;
    }

    public void setFinishPartNumber(String finishPartNumber) {
        this.finishPartNumber = finishPartNumber;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable (name="kits_finish_parts",
            joinColumns = @JoinColumn(name="finish_parts_id"),
            inverseJoinColumns = @JoinColumn(name="kits_id"))
    public Set<Kit> getKitSet() {
        return kitSet;
    }

    public void setKitSet(Set<Kit> kitSet) {
        this.kitSet = kitSet;
    }

    @Column(name="sortNum")
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinishPart that = (FinishPart) o;

        if (finishPartNumber != null ? !finishPartNumber.equals(that.finishPartNumber) : that.finishPartNumber != null)
            return false;
        if (kitSet != null ? !kitSet.equals(that.kitSet) : that.kitSet != null) return false;
        if (sortNum != null ? !sortNum.equals(that.sortNum) : that.sortNum != null) return false;
        return partQtySet != null ? partQtySet.equals(that.partQtySet) : that.partQtySet == null;
    }

    @Override
    public int hashCode() {
        int result = finishPartNumber != null ? finishPartNumber.hashCode() : 0;
        result = 31 * result + (kitSet != null ? kitSet.hashCode() : 0);
        result = 31 * result + (sortNum != null ? sortNum.hashCode() : 0);
        result = 31 * result + (partQtySet != null ? partQtySet.hashCode() : 0);
        return result;
    }
}
