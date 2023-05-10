package kea.dk.devtool.model;

import java.time.LocalDate;

public class Task {

    private int taskId;
    private int processId;
    private String taskName;
    private int effort;
    private LocalDate expectedStartDate;
    private int minAllocation;
    private String taskStatus;
    private String assignedId;
    private int taskSequenceNumber;

    public Task() {
    }

    public Task(int taskId, int processId, String taskName, int effort, LocalDate expectedStartDate, int minAllocation, String taskStatus, String assignedId, int taskSequenceNumber) {
        this.taskId = taskId;
        this.processId = processId;
        this.taskName = taskName;
        this.effort = effort;
        this.expectedStartDate = expectedStartDate;
        this.minAllocation = minAllocation;
        this.taskStatus = taskStatus;
        this.assignedId = assignedId;
        this.taskSequenceNumber = taskSequenceNumber;
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
                '}';
    }
}

