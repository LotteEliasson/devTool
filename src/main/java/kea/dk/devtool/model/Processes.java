package kea.dk.devtool.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Processes {
    private int processId;
    private int projectId;
    private String processName;
    private LocalDate expectedStartDate;
    private LocalDate expectedFinish;
    private int startAfterTask; // taskid must be in project
    private List<Task> taskList;

    public Processes() {
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
        return expectedFinish;
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
                '}';
    }
}
