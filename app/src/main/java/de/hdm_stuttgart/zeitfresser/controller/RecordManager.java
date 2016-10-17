package de.hdm_stuttgart.zeitfresser.controller;


import de.hdm_stuttgart.zeitfresser.model.Record;

public class RecordManager {

    private RecordObserver recordObserver = new RecordObserver();

    public RecordManager() {
    }

    public Record createRecord() {
        Record record = new Record();
        record.addObserver(recordObserver);
        return record;
    }

    public void start(Record record){

    }

    public void stop(Record record){

    }

    public long getDuration(Record record){
        return record.getDuration();
    }
}
