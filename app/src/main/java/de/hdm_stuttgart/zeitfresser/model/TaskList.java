package de.hdm_stuttgart.zeitfresser.model;

import java.util.ArrayList;

import de.hdm_stuttgart.zeitfresser.controller.TaskManager;

public class TaskList {

    private ArrayList<Task> tasks;
    private TaskManager taskManager = new TaskManager();

    public TaskList() {
        createTaskList();
    }

    private void createTaskList() {
        tasks = new ArrayList<>();
        addTask("Lesen", 1);
        addTask("Zocken", 2);
        addTask("Kochen", 3);
    }

    private void addTask(String name, long id) {
        tasks.add(new Task(name, id));
    }

    public ArrayList<String> getAllNames() {
        ArrayList<String> names = new ArrayList<>();
        for(Task task : tasks){
            names.add(task.getName());
        }
        return names;
    }

    public void setTaskActive(String name) {
        Task task = getTaskForName(name);
        taskManager.startTask(task);
    }

    public void setTaskInactive(String name) {
        Task task = getTaskForName(name);
        taskManager.stopTask(task);
    }

    public boolean isTaskActive(String name) {
        Task task = getTaskForName(name);
        return taskManager.taskIsActive(task);
    }

    private Task getTaskForName(String name) {
        for(Task task: tasks){
            if(task.getName().equals(name)){
                return task;
            }
        }
        return null;
    }

    public long getOverallDuration(Task task) {
        return taskManager.getOverallDuration(task);
    }

    public long getOverallDuration(String taskName) {
        Task task = getTaskForName(taskName);
        return taskManager.getOverallDuration(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
