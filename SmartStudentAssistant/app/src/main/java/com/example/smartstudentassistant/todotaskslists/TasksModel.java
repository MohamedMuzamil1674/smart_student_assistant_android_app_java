package com.example.smartstudentassistant.todotaskslists;

public class TasksModel {

    int taskId;
    String taskTitle;
    String taskDetail;
    String taskStatus;

    public TasksModel(){
        /* Default constructor required for calling */
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isChecked(){
        /* This is used to get task status string easily */
        return "Finished".equalsIgnoreCase(taskStatus);
    }
}
