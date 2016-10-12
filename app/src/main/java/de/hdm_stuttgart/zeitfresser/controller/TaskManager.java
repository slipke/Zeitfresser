package de.hdm_stuttgart.zeitfresser.controller;


import android.util.Log;

import java.util.Date;

import de.hdm_stuttgart.zeitfresser.model.Record;
import de.hdm_stuttgart.zeitfresser.model.Task;

public class TaskManager {

    public TaskManager() {
    }

    public void startTask(Task task){
        Record record = new Record();
        record.setStart(new Date());

        task.setOn(true);
        task.setActiveRecord(record);
        task.getRecords().add(record);
    }

    private void addRecordToTask(Task task, Record record){

    }

    public void stopTask(Task task){
        Record record = task.getActiveRecord();
        record.setEnd(new Date());

    }

    public boolean taskIsActive(Task task){
        return task.isOn();
    }

    public boolean hasRecords(Task task){
        return false;
    }

    public long getOverallDuration(Task task){
        return 1L;
    }
}
