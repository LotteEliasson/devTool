package kea.dk.devtool.repository;

import kea.dk.devtool.model.*;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    @Value("jdbc:mysql:${DB_URL}")
    private String DB_URL;
    @Value("${USER_IDE}")
    private String UID;
    @Value("${PASSW}")
    private String PWD;

  //  Henter opgaver(task) fra databasen ud fra et givent proces id, som opgaven ligger under.
    public List<Task> getTaskById(int taskProcessId ) {
        List<Task> tasks = new ArrayList<>();

        try {
            //Connection til databasen
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();
            //Henter data fra databasen
            final String SQL_GETTASKS = "SELECT * FROM projectdb.task WHERE processID=" + taskProcessId;
            ResultSet resultSet = statement.executeQuery(SQL_GETTASKS);

            //Loop der kalder alle opgaver indtil der ikke er flere for et givent proces id
            while (resultSet.next()){
                int taskId = resultSet.getInt(1);
                int processId = resultSet.getInt(2);
                String taskName = resultSet.getString(3);
                int effort = resultSet.getInt(4);
                LocalDate expectedStartDate = resultSet.getDate(5).toLocalDate();
                int minAllocation = resultSet.getInt(6);
                TaskStatus taskStatus = TaskStatus.valueOf(resultSet.getString(7));
                String assignedname = resultSet.getString(8);
                int taskSequenceNumber = resultSet.getInt(9);
                int projectId = resultSet.getInt(10);
                int developerId=resultSet.getInt(11);
                Task newTask = new Task(taskId, processId,taskName,effort,expectedStartDate,minAllocation,taskStatus,assignedname,taskSequenceNumber, projectId,developerId);
                tasks.add(newTask);
            }

        } catch (SQLException e) {
            System.out.println("Could not get Tasks");
            e.printStackTrace();
        }
        return tasks;
    }

    //Add new Tasks
    public void addTask(Task task, int processId) {

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            final String SQL_addTask = "INSERT INTO projectdb.task(processID, task_name, effort, expected_startdate, " +
                    "min_allocation, task_status, assignedname, tasksequencenumber, projectID) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_addTask);

            //Sætter variable for en ny opgave.
            preparedStatement.setInt(1, processId);
            preparedStatement.setString(2, task.getTaskName());
            preparedStatement.setInt(3, task.getEffort());
            preparedStatement.setDate(4, Date.valueOf(task.getExpectedStartDate()));
            preparedStatement.setInt(5, task.getMinAllocation());
            preparedStatement.setString(6, String.valueOf(task.getTaskStatus()));
            preparedStatement.setString(7, task.getAssignedname());
            preparedStatement.setInt(8, task.getTaskDependencyNumber());
            preparedStatement.setInt(9, task.getProjectId());

            //Sender ny opgave til databasen.
            preparedStatement.executeUpdate();

            } catch (SQLException e) {
            System.out.println("Could not create new Task");
            e.printStackTrace();
        }
    }

   //Update Tasks
   public void updateTask(Task task) {
        //Henter parametre ud fra et specifikt task id i databasen
        final String UPDATE_task = "UPDATE projectdb.task SET task_name=?, effort=?, expected_startdate=?, min_allocation=?, task_status=?, assignedname=?, tasksequencenumber=? WHERE taskID=?";

        try {
           Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
           PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_task);

           //Henter variable i model task
            String taskName = task.getTaskName();
            int effort = task.getEffort();
             LocalDate expectedStartDate = task.getExpectedStartDate();
            int minAllocation = task.getMinAllocation();
            TaskStatus taskStatus = task.getTaskStatus();
            String assignedId = task.getAssignedname();
            int taskSequenceNumber = task.getTaskDependencyNumber();
            //Sætter nye parametre for en task
            preparedStatement.setString(1, taskName);
            preparedStatement.setInt(2, effort);
            preparedStatement.setDate(3, Date.valueOf(expectedStartDate));
            preparedStatement.setInt(4, minAllocation);
            preparedStatement.setString(5, String.valueOf(taskStatus));
            preparedStatement.setString(6, assignedId);
            preparedStatement.setInt(7, taskSequenceNumber);
            preparedStatement.setInt(8,task.getTaskId());
            //Opdaterer databasen
            preparedStatement.executeUpdate();

       } catch (SQLException e) {
           System.out.println("Could not update Task");
           e.printStackTrace();
       }
   }

    //Find Tasks by ID
    public Task findTaskById(int tasksId) {
        final String FIND_task = "SELECT * FROM projectdb.task WHERE taskID=?";

        //Opretter nyt objekt
        Task task = new Task();
        task.setTaskId(tasksId);

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_task);
            preparedStatement.setInt(1, tasksId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            String taskName = resultSet.getString(3);
            int effort = resultSet.getInt(4);
            LocalDate expectedStartDate = resultSet.getDate(5).toLocalDate();
            int minAllocation = resultSet.getInt(6);
            TaskStatus taskStatus = TaskStatus.valueOf(resultSet.getString(7)) ;
            String assignedId = resultSet.getString(8);
            int taskSequenceNumber = resultSet.getInt(9);

            task.setTaskName(taskName);
            task.setEffort(effort);
            task.setExpectedStartDate(expectedStartDate);
            task.setMinAllocation(minAllocation);
            task.setTaskStatus(taskStatus);
            task.setAssignedname(assignedId);
            task.setTaskDependencyNumber(taskSequenceNumber);

        } catch (SQLException e) {
            System.out.println("Could not find Task");
            e.printStackTrace();
        }
        return task;
    }
    //Delete Tasks
    public void deleteTask(int taskId) {
        final String DELETE_task = "DELETE FROM projectdb.task WHERE taskID=?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_task);

            preparedStatement.setInt(1, taskId);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Could not delete Task");
            e.printStackTrace();
        }
    }
    //Henter alle tasks der er i et givent projekt inkl developer id og project id for task.
    public List<Task> getProjectTasks(int projectId){
       List<Task> projectTasks=new ArrayList<>();
       final String QUERYPROJECTTASK="SELECT * FROM projectdb.task WHERE projectID=?";
       try {
          Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
          PreparedStatement preparedStatement=connection.prepareStatement(QUERYPROJECTTASK);
          preparedStatement.setInt(1,projectId);
          ResultSet resultSet=preparedStatement.executeQuery();
          while (resultSet.next()){
             int taskId = resultSet.getInt(1);
             int processId = resultSet.getInt(2);
             String taskName = resultSet.getString(3);
             int effort = resultSet.getInt(4);
             LocalDate expectedStartDate = resultSet.getDate(5).toLocalDate();
             int minAllocation = resultSet.getInt(6);
             TaskStatus taskStatus = TaskStatus.valueOf(resultSet.getString(7));
             String assignedname = resultSet.getString(8);
             int taskSequenceNumber = resultSet.getInt(9);

             int developerId=resultSet.getInt(11);
             Task newTask = new Task(taskId, processId,taskName,effort,expectedStartDate,minAllocation,taskStatus,assignedname,taskSequenceNumber, projectId,developerId);
             projectTasks.add(newTask);
          }

       }catch (SQLException e){
          System.out.println("unable to retrieve projects task");
          e.printStackTrace();
       }

       return projectTasks;
    }
    // Developers task:
//    public List<Task> getMyTasks(int userid){
//       ArrayList<Task> myTasks=new ArrayList<>();
//       final String TASKLIST="SELECT * FROM projectdb.task WHERE assignedID=?"
//    }

}

