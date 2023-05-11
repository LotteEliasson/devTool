package kea.dk.devtool.repository;

import kea.dk.devtool.model.Project;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository
	{
		@Value("jdbc:mysql:${DB_URL}")
		private String DB_URL;
		@Value("${USER_IDE}")
		private String UID;
		@Value("${PASSW}")
		private String PWD;

		public void addProject(Project newproject){
			final String NEW_PROJECT = "INSERT INTO  projectdb.project(project_name, startdate, expected_enddate, " +
					"due_date,project_manager,customer_name) VALUES(?,?,?,?,?,?) ";
			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);

				PreparedStatement preparedStatement = connection.prepareStatement(NEW_PROJECT);
				preparedStatement.setString(1,newproject.getProjectName());
				preparedStatement.setDate(2, Date.valueOf(newproject.getStartDate()));
				preparedStatement.setDate(3, Date.valueOf(newproject.getExpectedEndDate()));
				preparedStatement.setDate(4, Date.valueOf(newproject.getDueDate()));
				preparedStatement.setString(5,newproject.getProjectManager());
				preparedStatement.setString(6,newproject.getCustomerName());

				preparedStatement.executeUpdate();

			}catch (SQLException e){
				System.out.println("unable to add project to database");
				e.printStackTrace();
			}
		}
		public List<Project> getMyProjects(String pmName){
			ArrayList<Project> myProjects=new ArrayList<>();
			final String QUERY_MYPROJECTS="SELECT * FROM projectdb.project WHERE project_manager=?";
			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(QUERY_MYPROJECTS);
				preparedStatement.setString(1,pmName);
				ResultSet resultSet=preparedStatement.executeQuery();
				while (resultSet.next()){
					int projectId=resultSet.getInt(1);
					String projectName=resultSet.getString(2);
					LocalDate startdate= resultSet.getDate(3).toLocalDate();
					LocalDate expectedend= resultSet.getDate(4).toLocalDate();
					LocalDate duedate= resultSet.getDate(5).toLocalDate();
					String customerName=resultSet.getString(7);
					Project myProject= new Project(projectId,projectName,startdate,expectedend,duedate,pmName,customerName);
					myProjects.add(myProject);
				}

			}catch (SQLException e){
				System.out.println("could not retrieve list of projects");
				e.printStackTrace();
			}
			return myProjects;
		}
		public void updateProject(Project project){
			//UPDATE wishlist.wish_list SET wish_list_name = ?, occation = ? WHERE wish_list_id = ?";
			//project_name, startdate, expected_enddate, due_date,project_manager,customer_name
			final String SQL_UPDATEPROJECT="UPDATE projectdb.project SET project_name=?, startdate=?," +
					" expected_enddate=?, due_date=?, project_manager=?, customer_name=? WHERE projectID=?";
			try{
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(SQL_UPDATEPROJECT);
				String project_name=project.getProjectName();
				LocalDate startdate=project.getStartDate();
				LocalDate expected_enddate=project.getExpectedEndDate();
				LocalDate due_date= project.getDueDate();
				String project_manager=project.getProjectManager();
				String customer_name=project.getCustomerName();
				int id=project.getProjectId();
				preparedStatement.setString(1,project_name);
				preparedStatement.setDate(2,Date.valueOf(startdate));
				preparedStatement.setDate(3,Date.valueOf(expected_enddate));
				preparedStatement.setDate(4,Date.valueOf(due_date));
				preparedStatement.setString(5,project_manager);
				preparedStatement.setString(6,customer_name);
				preparedStatement.setInt(7,id);
				preparedStatement.executeUpdate();

			}catch(SQLException e){
				System.out.println(" unable to update project");
				e.printStackTrace();
			}
		}
	}
