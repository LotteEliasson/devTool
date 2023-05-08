package kea.dk.devtool.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
	private static Connection connection = null;

	//giv connection til database
	public static Connection getConnection(String db_url, String uid, String pwd){
		//connection initialiseret?
		if (connection == null) {
			//initialize connection
			try {
				connection = DriverManager.getConnection(db_url, uid, pwd);
			} catch (SQLException e) {
				System.out.println("Could not connect to database");
				e.printStackTrace();
			}
		}
		return connection;
	}
}