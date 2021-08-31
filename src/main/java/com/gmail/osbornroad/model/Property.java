package com.gmail.osbornroad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "properties")
public class Property extends BaseEntity{

    private String propName;
    private String propValue;

    public Property() {
    }

    public Property(Integer id, String propName, String propValue) {
        super(id);
        this.propName = propName;
        this.propValue = propValue;
    }

    @Column(name = "propname")
    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    @Column(name = "propvalue")
    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
}
