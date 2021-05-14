package com.gmail.osbornroad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="notes")
public class Note extends BaseEntity{

    private Integer fieldKey;
    private String aPoint;
    private String sequence;
    private String modelVariant;
    private String series;
    private Integer number;
    private LocalDateTime planned;
    private String wib225;
    private String wib224;
    private LocalDateTime aPointDateTime;

    public Note() {
    }

    public Note(Integer id, Integer fieldKey, String aPoint, String sequence,
                String modelVariant, String series, Integer number,
                LocalDateTime planned, String wib225, String wib224, LocalDateTime aPointDateTime) {
        super(id);
        this.fieldKey = fieldKey;
        this.aPoint = aPoint;
        this.sequence = sequence;
        this.modelVariant = modelVariant;
        this.series = series;
        this.number = number;
        this.planned = planned;
        this.wib225 = wib225;
        this.wib224 = wib224;
        this.aPointDateTime = aPointDateTime;
    }

    public Note(Integer fieldKey, String aPoint, String sequence,
                String modelVariant, String series, Integer number,
                LocalDateTime planned, String wib225, String wib224, LocalDateTime aPointDateTime) {
        this.fieldKey = fieldKey;
        this.aPoint = aPoint;
        this.sequence = sequence;
        this.modelVariant = modelVariant;
        this.series = series;
        this.number = number;
        this.planned = planned;
        this.wib225 = wib225;
        this.wib224 = wib224;
        this.aPointDateTime = aPointDateTime;
    }

    @Column
    public Integer getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(Integer fieldKey) {
        this.fieldKey = fieldKey;
    }

    @Column
    public String getAPoint() {
        return aPoint;
    }

    public void setAPoint(String aPoint) {
        this.aPoint = aPoint;
    }

    @Column
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Column
    public String getModelVariant() {
        return modelVariant;
    }

    public void setModelVariant(String modelVariant) {
        this.modelVariant = modelVariant;
    }

    @Column
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Column
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Column
    public LocalDateTime getPlanned() {
        return planned;
    }

    public void setPlanned(LocalDateTime planned) {
        this.planned = planned;
    }

    @Column
    public String getWib225() {
        return wib225;
    }

    public void setWib225(String wib225) {
        this.wib225 = wib225;
    }

    @Column
    public String getWib224() {
        return wib224;
    }

    public void setWib224(String wib224) {
        this.wib224 = wib224;
    }

    @Column
    public LocalDateTime getAPointDateTime() {
        return aPointDateTime;
    }

    public void setAPointDateTime(LocalDateTime aPointDT) {
        this.aPointDateTime = aPointDT;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", fieldKey=" + fieldKey +
                ", aPoint='" + aPoint + '\'' +
                ", sequence='" + sequence + '\'' +
                ", modelVariant='" + modelVariant + '\'' +
                ", series='" + series + '\'' +
                ", number=" + number +
                ", planned=" + planned +
                ", wib225='" + wib225 + '\'' +
                ", wib224='" + wib224 + '\'' +
                ", aPointDateTime=" + aPointDateTime +
                '}';
    }
}
