package com.gmail.osbornroad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="parts")
public class Part extends BaseEntity {

    @NotEmpty
    private String partNumber;

    private Set<PartQty> partQtySet = new HashSet<>();

    private Integer sortNum;

    private String partType;

    private Integer snp;

    public Part() {
    }

    public Part(@NotEmpty String partNumber, Integer sortNum, String partType, Integer snp) {
        this.partNumber = partNumber;
        this.sortNum = sortNum;
        this.partType = partType;
        this.snp = snp;
    }

    public Part(Integer id, @NotEmpty String partNumber, Integer sortNum, String partType, Integer snp) {
        super(id);
        this.partNumber = partNumber;
        this.sortNum = sortNum;
        this.partType = partType;
        this.snp = snp;
    }

    public Part(@NotEmpty String partNumber, Set<PartQty> partQtySet, Integer sortNum, String partType, Integer snp) {
        this.partNumber = partNumber;
        this.partQtySet = partQtySet;
        this.sortNum = sortNum;
        this.partType = partType;
        this.snp = snp;
    }

    public Part(Integer id, @NotEmpty String partNumber, Set<PartQty> partQtySet, Integer sortNum, String partType, Integer snp) {
        super(id);
        this.partNumber = partNumber;
        this.partQtySet = partQtySet;
        this.sortNum = sortNum;
        this.partType = partType;
        this.snp = snp;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @OneToMany(mappedBy = "part", fetch = FetchType.EAGER)
    @JsonIgnore
    public Set<PartQty> getPartQtySet() {
        return partQtySet;
    }

    public void setPartQtySet(Set<PartQty> partQtySet) {
        this.partQtySet = partQtySet;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public Integer getSnp() {
        return snp;
    }

    public void setSnp(Integer snp) {
        this.snp = snp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Part part = (Part) o;

        if (partNumber != null ? !partNumber.equals(part.partNumber) : part.partNumber != null) return false;
        if (sortNum != null ? !sortNum.equals(part.sortNum) : part.sortNum != null) return false;
        if (partType != null ? !partType.equals(part.partType) : part.partType != null) return false;
        return snp != null ? snp.equals(part.snp) : part.snp == null;
    }

    @Override
    public int hashCode() {
        int result = partNumber != null ? partNumber.hashCode() : 0;
        result = 31 * result + (sortNum != null ? sortNum.hashCode() : 0);
        result = 31 * result + (partType != null ? partType.hashCode() : 0);
        result = 31 * result + (snp != null ? snp.hashCode() : 0);
        return result;
    }
}
