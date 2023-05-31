package kea.dk.devtool.model;

import kea.dk.devtool.utility.TimeAndEffort;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static kea.dk.devtool.utility.TimeAndEffort.procesEnddate;

public class Processes {
    private int processId;
    private int projectId;
    private String processName;
    private LocalDate expectedStartDate;
    private LocalDate expectedFinish;
    private int startAfterTask =-1; // taskid must be in project - will determine the expected startdate. if =-1 then starts with project
    private List<Task> taskList;//will determine the expected enddate

    public Processes() {
    }

    public Processes(int processId, int projectId, String processName, LocalDate expectedStartDate, LocalDate expectedFinish, int startAfterTask,List<Task> taskList) {
        this.processId = processId;
        this.projectId = projectId;
        this.processName = processName;
        this.expectedStartDate = expectedStartDate;
        this.expectedFinish = expectedFinish;
        this.startAfterTask = startAfterTask;
        this.taskList=taskList;
    }
    public Processes(int processId, int projectId, String processName, LocalDate expectedStartDate, LocalDate expectedFinish, int startAfterTask) {
        this.processId = processId;
        this.projectId = projectId;
        this.processName = processName;
        this.expectedStartDate = expectedStartDate;
        this.expectedFinish = expectedFinish;
        this.startAfterTask = startAfterTask;

    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public LocalDate getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(LocalDate expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public LocalDate getExpectedFinish() {
        return this.expectedFinish;
    }

    public void setExpectedFinish(LocalDate expectedFinish) {
        this.expectedFinish = expectedFinish;
    }

    public int getStartAfterTask() {
        return startAfterTask;
    }

    public void setStartAfterTask(int startAfterTask) {
        this.startAfterTask = startAfterTask;
    }


    public List<Task> getTaskList()
        {
            return taskList;
        }

    public void setTaskList(List<Task> taskList)
        {
            this.taskList = taskList;
        }

    @Override
    public String toString() {
        return "ProjectProcess{" +
                "processId=" + processId +
                ", projectId=" + projectId +
                ", processName='" + processName + '\'' +
                ", expectedStartDate=" + expectedStartDate +
                ", expectedFinish=" + expectedFinish +
                ", startAfterTask=" + startAfterTask +
                "taskList: "+taskList+ '}';
    }
    public int getProcessDays(){
        return TimeAndEffort.daysBetween(this.expectedStartDate,this.expectedFinish);
    }
    public int getProcessWorkdays(){
        return TimeAndEffort.workingDaysBetween(this.expectedStartDate,this.expectedFinish);

    }
    public LocalDate getProcessEndDate(){
        LocalDate end= TimeAndEffort.procesEnddate(this);
        this.setExpectedFinish(end);
        return end;
    }

}
