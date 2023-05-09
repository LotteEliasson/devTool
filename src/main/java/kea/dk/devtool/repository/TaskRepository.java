package kea.dk.devtool.repository;

import kea.dk.devtool.model.Task;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    @Value("jdbc:mysql:${DB_URL}")
    private String DB_URL;
    @Value("${UID}")
    private String UID;
    @Value("${PWD}")
    private String PWD;

    //Get Tasks by ID
    public void getTaskById(int processId) {
        List<Task> task = new ArrayList<>();

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();

        } catch (SQLException e) {
            System.out.println("Could not create new Task");
            e.printStackTrace();
        }
    }

    //Add new Tasks
    public void addTask(Task task, int processId) {

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();


        } catch (SQLException e) {
            System.out.println("Could not create new Task");
            e.printStackTrace();
        }
    }

   //Update Tasks
   public void updateTask(Task task) {

        try {
           Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();


       } catch (SQLException e) {
           System.out.println("Could not create new Task");
           e.printStackTrace();
       }
   }

    //Find Tasks by ID
    public void findTaskById(int taskId) {

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();

        } catch (SQLException e) {
            System.out.println("Could not find Task");
            e.printStackTrace();
        }
    }
    //Delete Tasks
    public void deleteTask(int taskId) {

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();


        } catch (SQLException e) {
            System.out.println("Could not delete Task");
            e.printStackTrace();
        }
    }

}

