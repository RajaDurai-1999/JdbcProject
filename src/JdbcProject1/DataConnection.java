package JdbcProject1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
	static String url = "jdbc:mysql://localhost:3306/student_management";
    static String username = "root";
    static String password = "Raja1504";
	public static Connection getConnection() throws SQLException {
		
        return DriverManager.getConnection(url, username, password);
    }

}
