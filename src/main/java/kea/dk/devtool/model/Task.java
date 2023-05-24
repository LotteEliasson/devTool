package kea.dk.devtool.model;

import kea.dk.devtool.utility.TimeAndEffort;

import java.time.LocalDate;

public class Task {

    private int taskId;
    private int processId;
    private String taskName;
    private int effort; // man-hours needed to complete task
    private LocalDate expectedStartDate;
    private int minAllocation; // assigned man-hours per day
    private String taskStatus;
    private String assignedId;
    private int taskSequenceNumber;
    private int projectId;

    public Task() {
    }

    public Task(int taskId, int processId, String taskName, int effort, LocalDate expectedStartDate, int minAllocation, String taskStatus, String assignedId, int taskSequenceNumber, int projectId) {
        this.taskId = taskId;
        this.processId = processId;
        this.taskName = taskName;
        this.effort = effort;
        this.expectedStartDate = expectedStartDate;
        this.minAllocation = minAllocation;
        this.taskStatus = taskStatus;
        this.assignedId = assignedId;
        this.taskSequenceNumber = taskSequenceNumber;
        this.projectId = projectId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public LocalDate getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(LocalDate expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public int getMinAllocation() {
        return minAllocation;
    }

    public void setMinAllocation(int minAllocation) {
        this.minAllocation = minAllocation;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId;
    }

    public int getTaskSequenceNumber() {
        return taskSequenceNumber;
    }

    public void setTaskSequenceNumber(int taskSequenceNumber) {
        this.taskSequenceNumber = taskSequenceNumber;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", processId=" + processId +
                ", taskName='" + taskName + '\'' +
                ", effort=" + effort +
                ", expectedStartDate=" + expectedStartDate +
                ", minAllocation=" + minAllocation +
                ", taskStatus='" + taskStatus + '\'' +
                ", assignedId='" + assignedId + '\'' +
                ", taskSequenceNumber=" + taskSequenceNumber +
                ", projectId=" + projectId +
                '}';
    }
    public int taskDaysNeeded(){
        return TimeAndEffort.normalWorkdaysNeeded(this.effort,this.minAllocation);
    }
}

