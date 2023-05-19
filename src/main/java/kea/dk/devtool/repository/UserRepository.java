package kea.dk.devtool.repository;

import kea.dk.devtool.model.User;
import kea.dk.devtool.model.hasRole;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public User getUserById(int userId){
        User user = new User();
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


    public User loginUser(String userNames, String userPasswords){

        User user = new User();

        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            final String SQL_LOGIN = "SELECT * FROM projectdb.users WHERE user_name=? AND user_password=?";
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

    public String passwordCrypt(String password){
        BCryptPasswordEncoder cryptPasswordEncoder=new BCryptPasswordEncoder(10);
        return cryptPasswordEncoder.encode(password);
    }
    public User login(String username, String password){
        User user=new User;
        final String LOGIN="SELECT * FROM projectdb.users WHERE user_name=?";
        BCryptPasswordEncoder check=new BCryptPasswordEncoder(10);
        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            PreparedStatement preparedStatement=connection.prepareStatement(LOGIN);
            preparedStatement.setString(1,username);

            ResultSet resultSet= preparedStatement.executeQuery();

            //userId, String userName, String userPassword, hasRole role
            while (resultSet.next()) {
                String storedPWD=resultSet.getString(3);
                if(check.matches(password,storedPWD)) {
                    int userId = resultSet.getInt(1);
                    String username = resultSet.getString(2);
                    String role = resultSet.getString(4);
                    user.setRole(hasRole.valueOf(role));
                    user.setUserId(userId);
                    user.setUserName(username);
                }

            }


        }catch(SQLException e){
            System.out.println("unable to login");
            e.printStackTrace();
        }
        return user;
    }
}
