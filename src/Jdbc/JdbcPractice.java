package Jdbc;

import java.sql.*;


public class JdbcPractice {

	public static void main(String[] args) throws Exception {
//		readRecords();
//		insertRecords();
//		insertVar();
//		insertPst();
//		deleteId();
		updatePst();

	}
	public static void readRecords() throws Exception{
	String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
	String username="root";
	String pass="Raja1504";
	String query="select * from students";
	
	Connection con=DriverManager.getConnection(url,username,pass);
	Statement st=con.createStatement();
	ResultSet rs=st.executeQuery(query);
	while(rs.next()) {
		System.out.println(rs.getInt(1));
		System.out.println(rs.getString(2));
		System.out.println(rs.getFloat(3));
	}
	con.close();
	}
	public static void insertRecords() throws Exception{
		String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
		String username="root";
		String pass="Raja1504";
		String query="insert into students values (5,'Raja',8.8)" ;
		
		Connection con=DriverManager.getConnection(url,username,pass);
		Statement st=con.createStatement();
		int rows=st.executeUpdate(query);
		System.out.println("no of rows Affected :"+rows);
		
		con.close();
		}
	public static void insertVar() throws Exception{
		String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
		String username="root";
		String pass="Raja1504";
		int id=6;
		String name="Durai";
		float gpa=7.7f;
		
		String query="insert into students values (" +id+",'"+name+"',"+gpa+");" ;
		
		Connection con=DriverManager.getConnection(url,username,pass);
		Statement st=con.createStatement();
		int rows=st.executeUpdate(query);
		System.out.println("no of rows Affected :"+rows);
		
		con.close();
		}
	public static void insertPst() throws Exception{
		String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
		String username="root";
		String pass="Raja1504";
		int id=7;
		String name="Kamal";
		float gpa=7.9f;
		
		String query="insert into students values (?,?,?);";
		
		Connection con=DriverManager.getConnection(url,username,pass);
		PreparedStatement pst=con.prepareStatement(query);
		pst.setInt(1, id);
		pst.setString(2, name);
		pst.setFloat(3, gpa);
		int rows=pst.executeUpdate();
		System.out.println("no of rows Affected :"+rows);
		
		con.close();
		}
	public static void deleteId() throws Exception{
		String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
		String username="root";
		String pass="Raja1504";
		int id=7;
		
		
		String query="delete from students where student_id="+id ;
		
		Connection con=DriverManager.getConnection(url,username,pass);
		Statement st=con.createStatement();
		int rows=st.executeUpdate(query);
		System.out.println("no of rows Affected :"+rows);
		
		con.close();
		}
	public static void update() throws Exception{
		String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
		String username="root";
		String pass="Raja1504";
		int id=6;
		
		
		String query="update students set gpa=8.5 where student_id="+id ;
		
		Connection con=DriverManager.getConnection(url,username,pass);
		Statement st=con.createStatement();
		int rows=st.executeUpdate(query);
		System.out.println("no of rows Affected :"+rows);
		
		con.close();
		}
	public static void updatePst() throws Exception{
		String url="jdbc:mysql://localhost:3306/jdbc_practice_db";
		String username="root";
		String pass="Raja1504";
		float gpa=9.5f;
		int id=5;
		
		
		String query="update students set gpa=? where student_id=?" ;
		
		Connection con=DriverManager.getConnection(url,username,pass);
		PreparedStatement pst=con.prepareStatement(query);
		pst.setFloat(1,gpa);
		pst.setInt(2, id);
		int rows=pst.executeUpdate();
		System.out.println("no of rows Affected :"+rows);
		
		con.close();
		}
	
	
}
