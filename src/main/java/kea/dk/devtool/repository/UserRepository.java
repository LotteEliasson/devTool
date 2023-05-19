package kea.dk.devtool.repository;

import kea.dk.devtool.model.Users;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserRepository {

    @Value("jdbc:mysql:${DB_URL}")
    private String DB_URL;
    @Value("${USER_IDE}")
    private String UID;
    @Value("${PASSW}")
    private String PWD;


    //Mangler at definere at det er en Project Manager!!!!!!!!!
    public Users getUserById(int userId){
        Users user = new Users();
        try{
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            Statement statement=connection.createStatement();

            final String SQL_USER="SELECT * FROM projectdb.users WHERE user_id="+userId;
            ResultSet resultSet=statement.executeQuery(SQL_USER);
            resultSet.next();
            int user_Id=resultSet.getInt(1);
            String userName=resultSet.getString(2);
            String userPassword=resultSet.getString(3);

            user.setUserId(user_Id);
            user.setUserName(userName);
            user.setUserPassword(userPassword);

        }catch(SQLException e){
            System.out.println("User could not be retrieved");
            e.printStackTrace();
        }
        return user;
    }


    public Users loginUser(String userNames, String userPasswords){

        Users user = new Users();

        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            final String SQL_LOGIN = "SLECT * FROM projectdb.users WHERE user_name=? AND user_password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_LOGIN);
            preparedStatement.setString(1,userNames);
            preparedStatement.setString(2,userPasswords);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                int user_Id=resultSet.getInt(1);
                String userName=resultSet.getString(2);
                String userPassword=resultSet.getString(3);
                user.setUserId(user_Id);
                user.setUserName(userName);
                user.setUserPassword(userPassword);
            }

        }catch(SQLException e){
            System.out.println("Unable to login - try again");
            e.printStackTrace();
        }
        return user;
    }
}
