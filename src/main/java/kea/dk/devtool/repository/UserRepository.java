package kea.dk.devtool.repository;

import kea.dk.devtool.model.*;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Value("jdbc:mysql:${DB_URL}")
    private String DB_URL;
    @Value("${USER_IDE}")
    private String UID;
    @Value("${PASSW}")
    private String PWD;


    public User getUserById(int userId){
        User user = new User();
        final String SQL_USER="SELECT * FROM projectdb.users WHERE user_id=?";
        try{
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            //Statement statement=connection.createStatement();
            PreparedStatement preparedStatement=connection.prepareStatement(SQL_USER);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            int user_Id=resultSet.getInt(1);
            String userName=resultSet.getString(2);
            String userPassword=resultSet.getString(3);
            HasRole role=HasRole.valueOf(resultSet.getString(4));

            user.setUserId(user_Id);
            user.setUserName(userName);
            user.setUserPassword(userPassword);
            user.setRole(role);

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
    // encrypt password - used when creating user
    public String passwordCrypt(String password){
        BCryptPasswordEncoder cryptPasswordEncoder=new BCryptPasswordEncoder(10);
        return cryptPasswordEncoder.encode(password);
    }


    public User login(String username, String password){
        User user=new User();
        final String LOGIN="SELECT * FROM projectdb.users WHERE user_name=? ";
        BCryptPasswordEncoder check=new BCryptPasswordEncoder(10);
        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            PreparedStatement preparedStatement=connection.prepareStatement(LOGIN);
            preparedStatement.setString(1,username);

            ResultSet resultSet= preparedStatement.executeQuery();
//
            while (resultSet.next()) {
                String storedPWD=resultSet.getString(3);
                if(check.matches(password,storedPWD)) {
                    int userId = resultSet.getInt(1);
                   // String username = resultSet.getString(2);
                    String role = resultSet.getString(4);
                    user.setRole(HasRole.valueOf(role));
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

    // create User
    public void createUser(User newUser){
        final String USER_CREATE="INSERT INTO projectdb.users (user_name, user_password) VALUES(?,?)";

        try{
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            PreparedStatement preparedStatement=connection.prepareStatement(USER_CREATE);
            preparedStatement.setString(1,newUser.getUserName());
            preparedStatement.setString(2,newUser.getUserPassword());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("Error creating new User");
            e.printStackTrace();
        }

    }

    // delete user
    public void deleteUserByID(int userID){
        final String DELETE_USER="DELETE FROM projectdb.users WHERE user_id=?";
        try{
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            PreparedStatement preparedStatement=connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1,userID);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("unable to delete user");
            e.printStackTrace();
        }
    }


    // update User
    public void editUser(User updateUser){
        final String UPDATE_USER="UPDATE projectdb.users SET user_name=?, has_role=? WHERE user_id=?";
       try{
           Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
           PreparedStatement preparedStatement=connection.prepareStatement(UPDATE_USER);
           preparedStatement.setString(1,updateUser.getUserName());
           preparedStatement.setString(2, String.valueOf(updateUser.getRole()));
           preparedStatement.setInt(3,updateUser.getUserId());
           preparedStatement.executeUpdate();
       }catch (SQLException e){
           System.out.println("unable to update user");
           e.printStackTrace();
       }
    }

    //show all users (for admin to assign role)
    public List<User> getAllUsersByRole(String usertype){
        List<User> users=new ArrayList<>();
        final String USERS_BY_ROLE="SELECT * FROM projectdb.users where has_role=?";
        final String ALL="SELECT * FROM projectdb.users";

        try{
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            ResultSet resultSet;
            if (usertype.isEmpty()||usertype.equals("ALL")){
                PreparedStatement preparedStatement=connection.prepareStatement(ALL);
                resultSet=preparedStatement.executeQuery();
            }
            else{
                PreparedStatement preparedStatement=connection.prepareStatement(USERS_BY_ROLE);
                preparedStatement.setString(1,usertype);
                 resultSet= preparedStatement.executeQuery();
            }
            while (resultSet.next()){
                int userid=resultSet.getInt(1);
                String name=resultSet.getString(2);
                String password=resultSet.getString(3);
                HasRole role=HasRole.valueOf(resultSet.getString(4));

                User newUser= new User(userid,name,password,role);
                users.add((newUser));
            }


        }catch (SQLException e){
            System.out.println("unable to get users");
        }
        return users;
    }
    public void updatePassword(User user){
        final String UPDATE_PASSWORD="UPDATE projectdb.users SET user_password=? WHERE user_id=?";
        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            PreparedStatement preparedStatement=connection.prepareStatement(UPDATE_PASSWORD);
            preparedStatement.setString(1,user.getUserPassword());
            preparedStatement.setInt(2,user.getUserId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("unable to change password");
        }
    }
    public List<Project> getAllProjects(){
        ArrayList<Project> allProjects=new ArrayList<>();
        final String QUERY_MYPROJECTS="SELECT * FROM projectdb.project";
        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            Statement statement=connection.createStatement();

            ResultSet resultSet=statement.executeQuery(QUERY_MYPROJECTS);
            while (resultSet.next()){
                int projectId=resultSet.getInt(1);
                String projectName=resultSet.getString(2);
                LocalDate startdate= resultSet.getDate(3).toLocalDate();
                LocalDate expectedend= resultSet.getDate(4).toLocalDate();
                LocalDate duedate= resultSet.getDate(5).toLocalDate();
                String pmName=resultSet.getString(6);
                String customerName=resultSet.getString(7);
                int pmID=resultSet.getInt(8);
                ProjectStatus status=ProjectStatus.valueOf(resultSet.getString(9));

                Project myProject= new Project(projectId,projectName,startdate,expectedend,duedate,pmName,customerName,pmID,status);
                allProjects.add(myProject);
            }

        }catch (SQLException e){
            System.out.println("could not retrieve list of projects");
            e.printStackTrace();
        }
        return allProjects;
    }
    public List<Task> getMyTasks(int id){
        ArrayList<Task> myTasks=new ArrayList<>();
        final String QUERYDEVELOPER="SELECT * FROM projectdb.task WHERE developerID=?";
        try {
            Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
            PreparedStatement preparedStatement=connection.prepareStatement(QUERYDEVELOPER);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Task task=new Task();
                task.setTaskId(resultSet.getInt(1));
                task.setProcessId(resultSet.getInt(2));
                task.setTaskName(resultSet.getString(3));
                task.setEffort(resultSet.getInt(4));
                task.setExpectedStartDate(resultSet.getDate(5).toLocalDate());
                task.setMinAllocation(resultSet.getInt(6));
                task.setTaskStatus(TaskStatus.valueOf(resultSet.getString(7)));
                task.setAssignedname(resultSet.getString(8));
                task.setTaskDependencyNumber(resultSet.getInt(9));
                task.setProjectId(resultSet.getInt(10));
                task.setDeveloperId(id);
                myTasks.add(task);

            }

        }catch (SQLException e){
            System.out.println("could not retrieve developers tasks");
            e.printStackTrace();
        }
        return myTasks;
    }

}
