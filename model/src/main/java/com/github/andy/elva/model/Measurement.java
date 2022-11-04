package com.github.andy.elva.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Measurement {
    @Id @Temporal(TemporalType.TIMESTAMP)
    Date date;
    int power;
    int el;
    int va;

    public Measurement() {
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "timestamp=" + date +
                ", power=" + power +
                ", elTotal=" + el +
                ", vaTotal=" + va +
                '}';
    }

    public Measurement(Date timestamp, int power, int elTotal, int vaTotal) {
        this.date = timestamp;
        this.power = power;
        this.el = elTotal;
        this.va = vaTotal;
    }

    @JsonProperty
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @JsonProperty
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
    @JsonProperty
    public int getEl() {
        return el;
    }

    public void setEl(int el) {
        this.el = el;
    }
    @JsonProperty
    public int getVa() {
        return va;
    }

    public void setVa(int va) {
        this.va = va;
    }
}
