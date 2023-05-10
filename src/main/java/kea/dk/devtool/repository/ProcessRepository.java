package kea.dk.devtool.repository;

import com.sun.jarsigner.ContentSignerParameters;
import kea.dk.devtool.model.Processes;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ProcessRepository {


    @Value("jdbc:mysql:${DB_URL}")
    private String DB_URL;
    @Value("${USER_IDE}")
    private String UID;
    @Value("${PASSW}")
    private String PWD;

    public void addProcess(Processes processes){
        try {
            Connection connection = ConnectionManager.getConnection(DB_URL, UID,PWD);

            final String SQL_ADD_PROCESS =  "INSERT INTO projectdb.processes(process_name, expected_start_date," +
                                            "expected_finish, start_after_task) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_PROCESS);
            preparedStatement.setString(1, processes.getProcessName());
            preparedStatement.setDate(2,processes.getExpectedStartDate());
            preparedStatement.setDate(3, processes.getExpectedFinish());
            preparedStatement.setInt(4, processes.getStartAfterTask());
            preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            System.out.println("can't add process");
            e.printStackTrace();
        }
    }
    public void updateProcess(Processes processes) {
        try{
            Connection connection = ConnectionManager.getConnection(DB_URL,UID,PWD);
            final String SQL_UPDATE_QUERY = "UPDATE projectdb.processes SET process_name = ?, expected_start_date = ?, " +
                    "                        expected_finish = ?, start_after_task = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_QUERY);

            String process_name= processes.getProcessName();
            Date expected_start_date = processes.getExpectedStartDate();
            Date expected_finish = processes.getExpectedFinish();
            int start_after_task = processes.getStartAfterTask();

            preparedStatement.setString(1,process_name);
            preparedStatement.setDate(2,expected_start_date);
            preparedStatement.setDate(3,expected_finish);
            preparedStatement.setInt(4,start_after_task);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println("can't update process");
            e.printStackTrace();
        }
    }
     public void findProcessById(){

     }
     public void deleteProcess(){

     }
}