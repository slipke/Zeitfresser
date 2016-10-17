package de.hdm_stuttgart.zeitfresser.controller;


import java.util.Date;

import de.hdm_stuttgart.zeitfresser.model.Record;
import de.hdm_stuttgart.zeitfresser.model.Task;

public class TaskManager {

    private RecordManager recordManager = new RecordManager();

    public TaskManager() {
    }

    public void startTask(Task task){
        Record record = recordManager.createRecord();
        record.setStart(new Date());

        task.setOn(true);

        addRecordToTask(task, record);
    }

    private void addRecordToTask(Task task, Record record){
        task.setActiveRecord(record);
        task.getRecords().add(record);
    }

    public void stopTask(Task task){
        Record record = task.getActiveRecord();
        record.setEnd(new Date());

        task.setOn(false);
        task.setActiveRecord(null);
    }

    public boolean taskIsActive(Task task){
        return task.isOn();
    }

    public boolean hasRecords(Task task){
        return task.getRecords().size() > 0;
    }

    public long getOverallDuration(Task task){
        long overall = 0;
        for (Record record : task.getRecords()) {
            overall += record.getDuration();
        }
        return overall;
    }
}
