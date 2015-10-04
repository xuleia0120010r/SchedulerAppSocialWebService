package sg.edu.astar.ihpc.schedulerapp.socialwebservice.utility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtility {
	
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	static{
		try {
			Properties props = new Properties();
			props.load(DBUtility.class.getClassLoader().getResourceAsStream(
					"db.properties"));
			driver = props.getProperty("driver");
			url = props.getProperty("url");
			user = props.getProperty("user");
			password = props.getProperty("password");
			Class.forName(driver);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws Exception{	
		Connection conn = null;
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	public static void close(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
