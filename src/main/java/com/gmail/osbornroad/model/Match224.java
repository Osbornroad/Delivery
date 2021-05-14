package com.gmail.osbornroad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="matching224")
public class Match224 extends BaseEntity {

    private String concat;
    private String wib224;

    public Match224() {
    }

    public Match224(String concat, String wib224) {
        this.concat = concat;
        this.wib224 = wib224;
    }

    public Match224(Integer id, String concat, String wib224) {
        super(id);
        this.concat = concat;
        this.wib224 = wib224;
    }

    @Column
    public String getConcat() {
        return concat;
    }

    public void setConcat(String concatSer_225_129) {
        this.concat = concatSer_225_129;
    }

    @Column
    public String getWib224() {
        return wib224;
    }

    public void setWib224(String wib224) {
        this.wib224 = wib224;
    }

    @Override
    public String toString() {
        return "Match224{" +
                "concat='" + concat + '\'' +
                ", wib224='" + wib224 + '\'' +
                ", id=" + id +
                '}';
    }
}
