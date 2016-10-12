package de.hdm_stuttgart.zeitfresser.model;

import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> tasks;

    public TaskList() {
        createTaskList();
    }

    public void createTaskList() {
        tasks = new ArrayList<>();
        tasks.add(new Task("task 1",1));
        tasks.add(new Task("task 2",2));
    }

    public void addTask(String name, long id) {

    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> names = new ArrayList<>();
        for(Task task : tasks){
            names.add(task.getName());
        }
        return names;
    }

    public void setTaskActive(String name) {

    }

    public void setTaskInactive(String name) {

    }

    public boolean isTaskActive(String name) {
        return false;
    }

    public Task getTaskForName(String name) {
        for(Task task: tasks){
            if(task.getName().equals(name)){
                return task;
            }
        }
        return null;
    }
}
