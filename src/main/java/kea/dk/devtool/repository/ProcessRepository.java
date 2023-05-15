package kea.dk.devtool.repository;

import kea.dk.devtool.model.Processes;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcessRepository {


    @Value("jdbc:mysql:${DB_URL}")
    private String DB_URL;
    @Value("${USER_IDE}")
    private String UID;
    @Value("${PASSW}")
    private String PWD;

    //Hent Liste over processer baseret på Projekt ID
    public List<Processes> getProcessByProjectId(int projectID) {
        ArrayList<Processes> processList = new ArrayList<>();
        final String SQL_QUERY = "SELECT * FROM projectdb.processes WHERE projectID = ?"; //+projectID;
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);
            //Statement statement =connection.createStatement();
           PreparedStatement preparedStatement=connection.prepareStatement(SQL_QUERY);
           preparedStatement.setInt(1,projectID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
               int processID = resultSet.getInt(1);
               String process_name = resultSet.getString(3);
               LocalDate expected_start_date = resultSet.getDate(4).toLocalDate();
               LocalDate expected_finish = resultSet.getDate(5).toLocalDate();
               int start_after_task = resultSet.getInt(6);

               Processes processes= new Processes(processID, projectID, process_name, expected_start_date, expected_finish, start_after_task);
               processList.add(processes);
            }
        }


        catch(SQLException e) {
            System.out.println("Can't connect to DB.");
        }
        return processList;
    }

    //tilføje Processer
    public void addProcess(Processes processes, int projectID){

        final String SQL_ADD_PROCESS =  "INSERT INTO projectdb.processes(projectID, process_name, expected_start_date," +
                                        "expected_finish, start_after_task) VALUES (?,?,?,?,?)";
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID,PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_PROCESS);
            preparedStatement.setInt(1, projectID);
            preparedStatement.setString(2, processes.getProcessName());
            preparedStatement.setDate(3, Date.valueOf(processes.getExpectedStartDate()));
            preparedStatement.setDate(4, Date.valueOf(processes.getExpectedFinish()));
            preparedStatement.setInt(5, processes.getStartAfterTask());
            preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            System.out.println("can't add process");
            e.printStackTrace();
        }
    }

    //Opdatere processer
    public void updateProcess(Processes processes) {

        final String SQL_UPDATE_QUERY = "UPDATE projectdb.processes SET process_name = ?, expected_start_date = ?, " +
                "                        expected_finish = ?, start_after_task = ?";
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_QUERY);

            String process_name= processes.getProcessName();
            LocalDate expected_start_date = processes.getExpectedStartDate();
            LocalDate expected_finish = processes.getExpectedFinish();
            int start_after_task = processes.getStartAfterTask();

            preparedStatement.setString(1,process_name);
            preparedStatement.setDate(2, Date.valueOf(expected_start_date));
            preparedStatement.setDate(3, Date.valueOf(expected_finish));
            preparedStatement.setInt(4,start_after_task);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println("can't update process");
            e.printStackTrace();
        }
    }
    //Find process på ID
     public Processes findProcessById(int id){
        final String FIND_QUERY = "SELECT * FROM projectdb.processes WHERE processID = ?";
        Processes process = new Processes();
        process.setProcessId(id);
         try{
            Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);

            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);

            preparedStatement.setInt(1,id);

             ResultSet resultSet = preparedStatement.executeQuery();
             resultSet.next();
             String process_name = resultSet.getString(2);
             LocalDate expected_start_date = resultSet.getDate(3).toLocalDate();
             LocalDate expected_finish = resultSet.getDate(4).toLocalDate();
             int start_after_task = resultSet.getInt(5);

             process.setProcessName(process_name);
             process.setExpectedStartDate(expected_start_date);
             process.setExpectedFinish(expected_finish);
             process.setStartAfterTask(start_after_task);
         }
         catch(SQLException e) {
             System.out.println("could not find process");
             e.printStackTrace();
         }
         return process;
     }

     //Slet Process på ID
     public void deleteProcessById(int processID){
        final String DELETE_QUERY = "DELETE FROM projectdb.processes WHERE processID = ?";

        try {
            Connection connection = ConnectionManager.getConnection(DB_URL,UID, PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1,processID);
            preparedStatement.executeUpdate();

        }
        catch(SQLException e) {
            System.out.println("could not delete process");
            e.printStackTrace();
         }
     }
    //Slet Tasks i enkelte process på ID
     public void deleteProcessTasksById(int processID){
         final String DELETE_QUERY = "DELETE FROM projectdb.task WHERE processID = ?";


         try {
             Connection connection = ConnectionManager.getConnection(DB_URL,UID, PWD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
             preparedStatement.setInt(1,processID);
             preparedStatement.executeUpdate();

         }
         catch(SQLException e) {
             System.out.println("could not delete process");
             e.printStackTrace();
         }
     }
}