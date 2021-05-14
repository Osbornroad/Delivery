package com.gmail.osbornroad.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "kits")
public class Kit extends BaseEntity{

    private String kitName;
    private String wib224;
    private String series;
    private Set<FinishPart> finishPartSet;
    private Integer sortNum;
    private boolean current = true;

    public Kit() {
    }

    public Kit(String kitName, String wib224, String series, Set<FinishPart> finishPartSet, Integer sortNum, boolean current) {
        this.kitName = kitName;
        this.wib224 = wib224;
        this.series = series;
        this.finishPartSet = finishPartSet;
        this.sortNum = sortNum;
        this.current = current;
    }

    public Kit(Integer id, String kitName, String wib224, String series, Set<FinishPart> finishPartSet, Integer sortNum, boolean current) {
        super(id);
        this.kitName = kitName;
        this.wib224 = wib224;
        this.series = series;
        this.finishPartSet = finishPartSet;
        this.sortNum = sortNum;
        this.current = current;
    }

    @Column(name = "kitName")
    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    @Column(name = "wib224")
    public String getWib224() {
        return wib224;
    }

    public void setWib224(String wib224) {
        this.wib224 = wib224;
    }

    @Column(name = "series")
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="kits_finish_parts",
            joinColumns = @JoinColumn(name="kits_id"),
            inverseJoinColumns = @JoinColumn(name="finish_parts_id"))
    public Set<FinishPart> getFinishPartSet() {
        return finishPartSet;
    }

    public void setFinishPartSet(Set<FinishPart> finishPartSet) {
        this.finishPartSet = finishPartSet;
    }

    @Column(name="sortNum")
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    @Column(name="current")
    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kit kit = (Kit) o;

        if (kitName != null ? !kitName.equals(kit.kitName) : kit.kitName != null) return false;
        if (wib224 != null ? !wib224.equals(kit.wib224) : kit.wib224 != null) return false;
        if (series != null ? !series.equals(kit.series) : kit.series != null) return false;
        return sortNum != null ? sortNum.equals(kit.sortNum) : kit.sortNum == null;
    }

    @Override
    public int hashCode() {
        int result = kitName != null ? kitName.hashCode() : 0;
        result = 31 * result + (wib224 != null ? wib224.hashCode() : 0);
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (sortNum != null ? sortNum.hashCode() : 0);
        return result;
    }
}
