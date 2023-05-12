package kea.dk.devtool.model;

import java.sql.Date;
import java.time.LocalDate;

public class Project {
    private int projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate dueDate;
    private String projectManager;
    private String customerName;
    private int projectManagerID;

    public Project() {
    }

    public Project(int projectId, String projectName, LocalDate startDate, LocalDate expectedEndDate, LocalDate dueDate, String projectManager, String customerName,int projectManagerID) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.dueDate = dueDate;
        this.projectManager = projectManager;
        this.customerName = customerName;
        this.projectManagerID=projectManagerID;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", expectedEndDate=" + expectedEndDate +
                ", dueDate=" + dueDate +
                ", projectManager='" + projectManager + '\'' +
                ", costumerName='" + customerName + '\'' +
                " Project Manager ID="+projectManagerID+" }";
    }

    public int getProjectManagerID()
        {
            return projectManagerID;
        }

    public void setProjectManagerID(int projectManagerID)
        {
            this.projectManagerID = projectManagerID;
        }
}
