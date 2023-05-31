package kea.dk.devtool.repository;

import kea.dk.devtool.model.Project;
import kea.dk.devtool.model.ProjectStatus;
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

		public String checkProject(int projectID){
			String response="";
			final String QueryProcess="SELECT * FROM projectdb.processes WHERE projectID=?";
			final String QueryTasks="SELECT * FROM projectdb.task WHERE projectID=?";

			try{
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatementTask=connection.prepareStatement(QueryTasks);
				PreparedStatement preparedStatementProces=connection.prepareStatement(QueryProcess);
				preparedStatementTask.setInt(1,projectID);
				preparedStatementProces.setInt(1,projectID);
				ResultSet rsTask=preparedStatementTask.executeQuery();
				ResultSet rsProces=preparedStatementTask.executeQuery();
				if (rsProces.next()==false){
					response=response.concat("project");
				}
				if (rsTask.next()==false){
					response=response.concat("process");
				}
				response=response.concat(" task");

			}catch (SQLException e){
				System.out.println("could not query database: "+QueryProcess+" or query: "+ QueryTasks+" projectRepository");
			}
			return response;
		}

		public void addProject(Project newproject){
			final String NEW_PROJECT = "INSERT INTO  projectdb.project(project_name, startdate, expected_enddate, " +
					"due_date,project_manager,customer_name,project_manager_id) VALUES(?,?,?,?,?,?,?) ";
			try{
				Connection connection = ConnectionManager.getConnection(DB_URL, UID, PWD);

				PreparedStatement preparedStatement = connection.prepareStatement(NEW_PROJECT);
				preparedStatement.setString(1,newproject.getProjectName());
				preparedStatement.setDate(2, Date.valueOf(newproject.getStartDate()));
				preparedStatement.setDate(3, Date.valueOf(newproject.getExpectedEndDate()));
				preparedStatement.setDate(4, Date.valueOf(newproject.getDueDate()));
				preparedStatement.setString(5,newproject.getProjectManager());
				preparedStatement.setString(6,newproject.getCustomerName());
				preparedStatement.setInt(7,newproject.getProjectManagerID());

				preparedStatement.executeUpdate();

			}catch (SQLException e){
				System.out.println("unable to add project to database");
				e.printStackTrace();
			}
		}
		public List<Project> getMyProjects(int pmID){
			ArrayList<Project> myProjects=new ArrayList<>();
			final String QUERY_MYPROJECTS="SELECT * FROM projectdb.project WHERE project_manager_id=?";
			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(QUERY_MYPROJECTS);
				preparedStatement.setInt(1,pmID);
				ResultSet resultSet=preparedStatement.executeQuery();
				while (resultSet.next()){
					int projectId=resultSet.getInt(1);
					String projectName=resultSet.getString(2);
					LocalDate startdate= resultSet.getDate(3).toLocalDate();
					LocalDate expectedend= resultSet.getDate(4).toLocalDate();
					LocalDate duedate= resultSet.getDate(5).toLocalDate();
					String pmName=resultSet.getString(6);
					String customerName=resultSet.getString(7);
					//pmID=resultSet.getInt(8);
					ProjectStatus status=ProjectStatus.valueOf(resultSet.getString(9));
					Project myProject= new Project(projectId,projectName,startdate,expectedend,duedate,pmName,customerName,pmID,status);
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
					" expected_enddate=?, due_date=?, project_manager=?, customer_name=?, status=? WHERE projectID=?";
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
				ProjectStatus status=project.getStatus();
				preparedStatement.setString(1,project_name);
				preparedStatement.setDate(2,Date.valueOf(startdate));
				preparedStatement.setDate(3,Date.valueOf(expected_enddate));
				preparedStatement.setDate(4,Date.valueOf(due_date));
				preparedStatement.setString(5,project_manager);
				preparedStatement.setString(6,customer_name);
				preparedStatement.setString(7, String.valueOf(status));
				preparedStatement.setInt(8,id);
				preparedStatement.executeUpdate();

			}catch(SQLException e){
				System.out.println(" unable to update project");
				e.printStackTrace();
			}
		}
		public Project findProjectByID(int id){
			Project myProject=new Project();
			final String FIND_PROJECT="SELECT * FROM projectdb.project WHERE projectID=?";
			try{
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(FIND_PROJECT);
				preparedStatement.setInt(1,id);
				ResultSet resultSet= preparedStatement.executeQuery();
				while (resultSet.next()){
					myProject.setProjectId(id);
					myProject.setProjectName(resultSet.getString(2));
					myProject.setStartDate(resultSet.getDate(3).toLocalDate());
					myProject.setExpectedEndDate(resultSet.getDate(4).toLocalDate());
					myProject.setDueDate(resultSet.getDate(5).toLocalDate());
					myProject.setProjectManager(resultSet.getString(6));
					myProject.setCustomerName(resultSet.getString(7));
					myProject.setProjectManagerID(resultSet.getInt(8));
				}


			}catch(SQLException e){
				System.out.println("could not find project");
				e.printStackTrace();

			}
			return myProject;
		}
		public void deleteProjectByID(int id){
			final String DELETE_QUERY = "DELETE FROM projectdb.project WHERE projectID=?";

			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(DELETE_QUERY);
				preparedStatement.setInt(1,id);
				preparedStatement.executeUpdate();
			}catch (SQLException e){
				System.out.println("unable to delete project");
				e.printStackTrace();
			}
		}
		public void deleteProcessByProjecID(int id){
			final String DELETE_QUERY = "DELETE FROM projectdb.processes WHERE projectID=?";

			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(DELETE_QUERY);
				preparedStatement.setInt(1,id);
				preparedStatement.executeUpdate();
			}catch (SQLException e){
				System.out.println("unable to delete project's processes");
				e.printStackTrace();
			}
		}
		public void deleteTasksByProjecID(int id){
			final String DELETE_QUERY = "DELETE FROM projectdb.task WHERE projectID=?";

			try {
				Connection connection=ConnectionManager.getConnection(DB_URL,UID,PWD);
				PreparedStatement preparedStatement=connection.prepareStatement(DELETE_QUERY);
				preparedStatement.setInt(1,id);
				preparedStatement.executeUpdate();
			}catch (SQLException e){
				System.out.println("unable to delete project's tasks");
				e.printStackTrace();
			}
		}
	}
