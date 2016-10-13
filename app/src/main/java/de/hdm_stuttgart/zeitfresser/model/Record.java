package de.hdm_stuttgart.zeitfresser.model;

import java.util.Date;
import java.util.Observable;


public class Record extends Observable{

    private long id;
    private long duration;
    private Date start;
    private Date end;

    public Record() {

    }

    public long getId() {
        return id;
    }

    public long getDuration() {
        return duration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getStart() {
        return start;
    }

    public Date getStartTime() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Date getEndTime() {
        return end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
        notifyObservers();
    }
}
