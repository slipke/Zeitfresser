package de.hdm_stuttgart.zeitfresser.controller;


import java.util.Observable;
import java.util.Observer;

import de.hdm_stuttgart.zeitfresser.model.Record;

public class RecordObserver implements Observer {
    public RecordObserver() {
    }

    @Override
    public void update(Observable observable, Object obj){
        Record record = (Record) observable;
        long duration = record.getEnd().getTime()-record.getStart().getTime();
        record.setDuration(duration);
    }
}
