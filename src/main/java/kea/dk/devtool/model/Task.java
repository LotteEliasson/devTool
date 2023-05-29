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
    private TaskStatus taskStatus;
    private String assignedname; // developer name

    // the taskId of the predecessor - 1 if it is supposed to start with proces. this task starts when the dependent task finishes
    private int taskDependencyNumber =-1;
    private int projectId;
    private int developerId;
    private LocalDate expectedFinish;

    public Task() {
    }

    public Task(int taskId, int processId, String taskName, int effort, LocalDate expectedStartDate, int minAllocation, TaskStatus taskStatus, String assignedname, int taskDependencyNumber, int projectId, int developerId) {
        this.taskId = taskId;
        this.processId = processId;
        this.taskName = taskName;
        this.effort = effort;
        this.expectedStartDate = expectedStartDate;
        this.minAllocation = minAllocation;
        this.taskStatus = taskStatus;
        this.assignedname = assignedname;
        this.taskDependencyNumber = taskDependencyNumber;
        this.projectId = projectId;
        this.developerId = developerId;
        this.expectedFinish=TimeAndEffort.calculateDate(this.expectedStartDate,TimeAndEffort.normalWorkdaysNeeded(this.effort,this.minAllocation));
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

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getAssignedname() {
        return assignedname;
    }

    public void setAssignedname(String assignedname) {
        this.assignedname = assignedname;
    }

    public int getTaskDependencyNumber() {
        return taskDependencyNumber;
    }

    public void setTaskDependencyNumber(int taskDependencyNumber) {
        this.taskDependencyNumber = taskDependencyNumber;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getDeveloperId()
        {
            return developerId;
        }

    public void setDeveloperId(int developerId)
        {
            this.developerId = developerId;
        }

    public LocalDate getExpectedFinish()
        {
            return expectedFinish;
        }

    public void setExpectedFinish(LocalDate expectedFinish)
        {
            this.expectedFinish = expectedFinish;
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
                ", assignedname='" + assignedname + '\'' +
                ", taskDependencyNumber=" + taskDependencyNumber +
                ", projectId=" + projectId +
                ", developerId="+ developerId +
              ", expected finish=" + expectedFinish+"}";
    }
    public int taskDaysNeeded(){
        return TimeAndEffort.normalWorkdaysNeeded(this.effort,this.minAllocation);
    }

}

