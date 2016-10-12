package de.hdm_stuttgart.zeitfresser.model;

import java.util.ArrayList;

public class Task {

    private long id;
    private boolean on;
    private String name;
    private ArrayList<Record> records;
    private Record activeRecord;

    public Task(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String getName() {
        return this.name;
    }

    public boolean isOn() {
        return this.on;
    }

    public long getId() {
        return this.id;
    }

    public ArrayList<Record> getRecords() {
        return this.records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public void setActiveRecord(Record record) {
        this.activeRecord = record;
    }

    public Record getActiveRecord() {
        return this.activeRecord;
    }
}
