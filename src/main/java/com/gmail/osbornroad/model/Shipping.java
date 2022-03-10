package com.gmail.osbornroad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="shipping")
public class Shipping extends BaseEntity{

    private Integer noteId;
    private LocalDateTime dateTime;
    private Integer userId;
    private Integer fkShipping;

    public Shipping() {
    }

    public Shipping(Integer noteId, LocalDateTime dateTime, Integer userId, Integer fkShipping) {
        this.noteId = noteId;
        this.dateTime = dateTime;
        this.userId = userId;
        this.fkShipping = fkShipping;
    }

    public Shipping(Integer id, Integer noteId, LocalDateTime dateTime, Integer userId, Integer fkShipping) {
        super(id);
        this.noteId = noteId;
        this.dateTime = dateTime;
        this.userId = userId;
        this.fkShipping = fkShipping;
    }

    @Column
    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    @Column
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column
    public Integer getFkShipping() {
        return fkShipping;
    }

    public void setFkShipping(Integer fkShipping) {
        this.fkShipping = fkShipping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shipping shipping = (Shipping) o;

        if (noteId != null ? !noteId.equals(shipping.noteId) : shipping.noteId != null) return false;
        return dateTime != null ? dateTime.equals(shipping.dateTime) : shipping.dateTime == null;
    }

    @Override
    public int hashCode() {
        int result = noteId != null ? noteId.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
}
