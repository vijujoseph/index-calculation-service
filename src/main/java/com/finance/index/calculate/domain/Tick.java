package com.finance.index.calculate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Tick implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long tickId;

    private String instrument;
    private double price;
    private Long timestamp;
    private Date createdDate;
    private String createdUser;

    public Tick() {
    }

    public Tick(String instrument, double price, long timestamp, Date createdDate, String createdUser) {
        this.instrument = instrument;
        this.price = price;
        this.timestamp = timestamp;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
    }

    public Long getTickId() {
        return tickId;
    }

    public void setTickId(Long tickId) {
        this.tickId = tickId;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tick tick = (Tick) o;

        if (Double.compare(tick.price, price) != 0) return false;
        if (timestamp != tick.timestamp) return false;
        if (tickId != null ? !tickId.equals(tick.tickId) : tick.tickId != null) return false;
        if (instrument != null ? !instrument.equals(tick.instrument) : tick.instrument != null) return false;
        if (createdDate != null ? !createdDate.equals(tick.createdDate) : tick.createdDate != null) return false;
        return createdUser != null ? createdUser.equals(tick.createdUser) : tick.createdUser == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = tickId != null ? tickId.hashCode() : 0;
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (createdUser != null ? createdUser.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "tickId='" + tickId + '\'' +
                ", instrument='" + instrument + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                ", createdDate=" + createdDate +
                ", createdUser='" + createdUser + '\'' +
                '}';
    }
}
