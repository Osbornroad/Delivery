package com.gmail.osbornroad.model.jdbc;

import java.time.LocalDateTime;

//BD_DELIVERY

public class Loading {
    private Integer fieldKey;            //FIELD_KEY
    private String aPoint;               //APOINT
    private LocalDateTime sequenceDate;  //SEQUESNCE_DATE
    private String sequenceNum;          //SEQUESNCE_NUM
    private String modelVariant;         //MODELVARIANT
    private String series;               //SERIES
    private Integer number;              //NUMBER
    private LocalDateTime planned;       //PLANNED
    private String tcwi225;              //TCWI225
    private String tcwi129;              //TCWI129
    private LocalDateTime aPointDT;      //APOINT_DT

    public Loading() {
    }

    public Loading(Integer fieldKey, String aPoint, LocalDateTime sequenceDate,
                   String sequenceNum, String modelVariant, String series, Integer number,
                   LocalDateTime planned, String tcwi225, String tcwi129, LocalDateTime aPointDT) {
        this.fieldKey = fieldKey;
        this.aPoint = aPoint;
        this.sequenceDate = sequenceDate;
        this.sequenceNum = sequenceNum;
        this.modelVariant = modelVariant;
        this.series = series;
        this.number = number;
        this.planned = planned;
        this.tcwi225 = tcwi225;
        this.tcwi129 = tcwi129;
        this.aPointDT = aPointDT;
    }

    public Integer getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(Integer fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getaPoint() {
        return aPoint;
    }

    public void setaPoint(String aPoint) {
        this.aPoint = aPoint;
    }

    public LocalDateTime getSequenceDate() {
        return sequenceDate;
    }

    public void setSequenceDate(LocalDateTime sequenceDate) {
        this.sequenceDate = sequenceDate;
    }

    public String getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(String sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public String getModelVariant() {
        return modelVariant;
    }

    public void setModelVariant(String modelVariant) {
        this.modelVariant = modelVariant;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDateTime getPlanned() {
        return planned;
    }

    public void setPlanned(LocalDateTime planned) {
        this.planned = planned;
    }

    public String getTcwi225() {
        return tcwi225;
    }

    public void setTcwi225(String tcwi225) {
        this.tcwi225 = tcwi225;
    }

    public String getTcwi129() {
        return tcwi129;
    }

    public void setTcwi129(String tcwi129) {
        this.tcwi129 = tcwi129;
    }

    public LocalDateTime getaPointDT() {
        return aPointDT;
    }

    public void setaPointDT(LocalDateTime aPointDT) {
        this.aPointDT = aPointDT;
    }

    @Override
    public String toString() {
        return "Loading{" +
                "fieldKey=" + fieldKey +
                ", aPoint='" + aPoint + '\'' +
                ", sequenceDate=" + sequenceDate +
                ", sequenceNum='" + sequenceNum + '\'' +
                ", modelVariant='" + modelVariant + '\'' +
                ", series='" + series + '\'' +
                ", number=" + number +
                ", planned=" + planned +
                ", tcwi225='" + tcwi225 + '\'' +
                ", tcwi129='" + tcwi129 + '\'' +
                ", aPointDT=" + aPointDT +
                '}';
    }
}
