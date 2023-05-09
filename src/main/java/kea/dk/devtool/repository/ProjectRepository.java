package kea.dk.devtool.repository;

import kea.dk.devtool.model.Project;
import kea.dk.devtool.utility.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ProjectRepository
	{
		@Value("jdbc:mysql:${DB_URL}")
		private String DB_URL;
		@Value("${USER_IDE}")
		private String UID;
		@Value("${PASSW}")
		private String PWD;
		public void addProject(Project project){
		public void addProject(Project newproject){
			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String NEW_PROJECT = "INSERT INTO  projectdb.project(project_name, startdate, expected_enddate, " +
						"due_date,project_manager,customer_name) VALUES(?,?,?,?,?,?) ";
				PreparedStatement preparedStatement = connection.prepareStatement(NEW_PROJECT);
				preparedStatement.setString(1,newproject.getProjectName());
				preparedStatement.setDate(2,newproject.getStartDate());
				preparedStatement.setDate(3,newproject.getExpectedEndDate());
				preparedStatement.setDate(4,newproject.getDueDate());
				preparedStatement.setString(5,newproject.getProjectManager());
				preparedStatement.setString(6,newproject.getCustomerName());

				preparedStatement.executeUpdate();

			}catch (SQLException e){
				System.out.println("unable to add project to database");
				e.printStackTrace();
			}
		}
	}
