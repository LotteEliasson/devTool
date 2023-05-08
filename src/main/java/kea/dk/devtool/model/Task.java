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
    private int assignedId;
    private int taskSequenceNumber;
}
