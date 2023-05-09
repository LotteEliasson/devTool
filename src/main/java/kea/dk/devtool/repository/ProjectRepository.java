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
			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);
				final String NEW_PROJECT = "INSERT INTO  projectdb.project(project_name, startdate, expected_enddate, due_date,project_manager,customer_name) VALUES(?,?,?,?,?,?) ";
				PreparedStatement preparedStatement = connection.prepareStatement(NEW_PROJECT);

			}catch (SQLException e){
				System.out.println("unable to add project to database");
				e.printStackTrace();
			}
		}
	}
