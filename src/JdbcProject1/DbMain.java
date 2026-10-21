package JdbcProject1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DbMain {
	static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println(".....STUDENT MANAGEMENT SYSTEM....");
            System.out.println("1. Add Student");
            System.out.println("2. Display Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Add Course");
            System.out.println("7. Display Courses");
            System.out.println("8. Enroll Student");
            System.out.println("9. Display Enrollments");
            System.out.println("10. Mark Attendance");
            System.out.println("11. Display Attendance");
            System.out.println("12. Enter Marks");
            System.out.println("13. Generate Results");
            System.out.println("14. Custom Queries");
            System.out.println("0. Exit");

            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        displayStudents();
                        break;
                    case 3:
                        searchStudent();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        deleteStudent();
                        break;
                    case 6:
                        addCourse();
                        break;
                    case 7:
                        displayCourses();
                        break;
                    case 8:
                        enrollStudent();
                        break;
                    case 9:
                        displayEnrollments();
                        break;
                    case 10:
                        markAttendance();
                        break;
                    case 11:
                        displayAttendance();
                        break;
                    case 12:
                        enterMarks();
                        break;
                    case 13:
                        generateResults();
                        break;
                    case 14:
                        customQueries();
                        break;
                    case 0:
                        System.out.println("Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }

    static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(sc.nextLine());

                if (value < 0) {
                    System.out.println("Enter a positive number or zero.");
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter numbers only.");
            }
        }
    }
    
    static String readText(String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot  empty!");
        }
    }

    static String readEmail() {
        while (true) {
            String email = readText("Enter email: ");

            if (email.contains("@") && email.contains(".")) {
                return email;
            }

            System.out.println("Enter a valid email!");
        }
    }
    
    static String readPhone() {
        while (true) {
            String phone = readText("Enter phone number: ");

            if (phone.matches("[0-9]")) {
                return phone;
            }

            System.out.println("Enter 10 to 15 digits only!");
        }
    }

    static void addStudent() throws SQLException {

        String sql = """
        		insert into students(name, email, phone, department)
                        values(?, ?, ?, ?)
                        """;

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String name = readText("Enter name: ");
            String email = readEmail();
            String phone = readPhone();
            String department = readText("Enter department: ");

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, department);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student added successfully!");
            }
        }
    }

    static void displayStudents() throws SQLException {

        String sql = "select * from students";

        try (Connection con = DataConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | Name | Email | Phone | Department");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("student_id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("phone") + " | " +
                        rs.getString("department"));
            }
        }
    }

    static void searchStudent() throws SQLException {

        int id = readInt("Enter student ID: ");

        String sql = "select * from students where student_id = ?";

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("ID: " + rs.getInt("student_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Phone: " + rs.getString("phone"));
                    System.out.println("Department: " +rs.getString("department"));
                } else {
                    System.out.println("Student not found!");
                }
            }
        }
    }

    static void updateStudent() throws SQLException {

        int id = readInt("Enter student ID: ");
        String name = readText("Enter new name: ");

        String sql = "update students set name = ? where student_id = ?";

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student not found!");
            }
        }
    }

    static void deleteStudent() throws SQLException {

        int id = readInt("Enter student ID: ");

        String sql = "delete from students where student_id = ?";

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found!");
            }
        }
    }

    static void addCourse() throws SQLException {

        String courseName = readText("Enter course name: ");
        int duration = readInt("Enter duration in months: ");

        if (duration == 0) {
            System.out.println("Duration must be greater than zero!");
            return;
        }

        String sql = "insert into courses(course_name, duration_months) values(?, ?)";
                  

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseName);
            ps.setInt(2, duration);

            ps.executeUpdate();

            System.out.println("Course added successfully!");
        }
    }

    static void displayCourses() throws SQLException {

        String sql = "select * from courses";

        try (Connection con = DataConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | Course Name | Duration");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("course_id") + " | " +
                        rs.getString("course_name") + " | " +
                        rs.getInt("duration_months") + " months");
            }
        }
    }

    static void enrollStudent() throws SQLException {

        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");

        String sql = """
        		   insert into enrollments"
                   (student_id, course_id, enrollment_date)
                     values(?, ?, curdate())
                     """;

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            ps.executeUpdate();

            System.out.println("Student enrolled successfully!");
        }
    }

    static void displayEnrollments() throws SQLException {

        String sql = """
        		select s.name, c.course_name, e.enrollment_date
        		from enrollments e 
        		join students s on e.student_id = s.student_id
        		join courses c on e.course_id = c.course_id
        		""";
        		

        try (Connection con = DataConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nStudent | Course | Enrollment Date");

            while (rs.next()) {
                System.out.println(
                        rs.getString("name") + " | " +
                        rs.getString("course_name") + " | " +
                        rs.getDate("enrollment_date"));
            }
        }
    }

    static void markAttendance() throws SQLException {

        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");

        String date = readText("Enter date (yyyy-mm-dd): ");

        try {
            Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format!");
            return;
        }

        String status = readText("Enter status (present/absent): ")
                .toLowerCase();

        if (!status.equals("present") && !status.equals("absent")) {
            System.out.println("Status must be present or absent!");
            return;
        }

        String sql = "insert into attendance (student_id, course_id, attendance_date, status)values(?, ?, ?, ?)";

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDate(3, Date.valueOf(date));
            ps.setString(4, status);

            ps.executeUpdate();

            System.out.println("Attendance marked successfully!");
        }
    }
    
    static void displayAttendance() throws SQLException {

        String sql ="""
        		select s.name, c.course_name,a.attendance_date, a.status
        		from attendance a
        		join students s on a.student_id = s.student_id
        		join courses c on a.course_id = c.course_id
        		""";

        try (Connection con = DataConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nStudent | Course | Date | Status");

            while (rs.next()) {
                System.out.println(
                        rs.getString("name") + " | " +
                        rs.getString("course_name") + " | " +
                        rs.getDate("attendance_date") + " | " +
                        rs.getString("status"));
            }
        }
    }

    static void enterMarks() throws SQLException {

        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        int total = readInt("Enter total marks: ");
        int obtained = readInt("Enter obtained marks: ");

        if (total == 0) {
            System.out.println("Total marks must be greater than zero!");
            return;
        }

        if (obtained > total) {
            System.out.println("Obtained marks cannot exceed total marks!");
            return;
        }

        String sql = "insert into marks (student_id, course_id, total_marks, obtained_marks)values(?, ?, ?, ?)";

        try (Connection con = DataConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setInt(3, total);
            ps.setInt(4, obtained);

            ps.executeUpdate();

            System.out.println("Marks added successfully!");
        }
    }

    static void generateResults() throws SQLException {

        String sql = """
        		    select s.name, c.course_name, m.total_marks, m.obtained_marks 
                   from marks m 
                   join students s on m.student_id = s.student_id 
                   join courses c on m.course_id = c.course_id
                   """;

        try (Connection con = DataConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("Student | Course | Marks | Percentage | Result");

            while (rs.next()) {

                int total = rs.getInt("total_marks");
                int obtained = rs.getInt("obtained_marks");

                double percentage = (obtained * 100.0) / total;

                String result;

                if (percentage >= 40) {
                    result = "Pass";
                } else {
                    result = "Fail";
                }

                System.out.println(
                        rs.getString("name") + " | " +
                        rs.getString("course_name") + " | " +
                        obtained + "/" + total + " | " +
                        String.format("%.2f", percentage) + "% | " +
                        result);
            }
        }
    }
    
    static void customQueries() throws SQLException {

        System.out.println("......CUSTOM QUERIES.....");
        System.out.println("1. Students and Courses");
        System.out.println("2. Students Without Enrollment");
        System.out.println("3. Average Marks by Course");
        System.out.println("4. Students Scoring Above 80%");
        System.out.println("5. Attendance Count by Student");
        System.out.println("6. Students Enrolled in Multiple Courses");

        int choice = readInt("Enter choice: ");

        String sql = "";

        switch (choice) {

            case 1:
                sql = """
                        select s.name, c.course_name
                        from students s
                        join enrollments e on s.student_id = e.student_id
                        join courses c on e.course_id = c.course_id
                        """;
                break;

            case 2:
                sql = """
                        select s.student_id, s.name
                        from students s
                        left join enrollments e on s.student_id = e.student_id
                        where e.student_id is null
                        """;
                break;

            case 3:
                sql = """
                        select c.course_name, avg(m.obtained_marks) as average_marks
                        from courses c
                        join marks m on c.course_id = m.course_id
                        group by c.course_id, c.course_name
                        """;
                break;

            case 4:
                sql = """
                        select s.name, c.course_name, m.obtained_marks, m.total_marks
                        from marks m
                        join students s on m.student_id = s.student_id
                        join courses c on m.course_id = c.course_id
                        where m.obtained_marks * 100.0 / m.total_marks > 80
                        """;
                break;

            case 5:
                sql =  """
                        select s.name, count(a.attendance_id) as attendance_count
                        from students s
                        left join attendance a on s.student_id = a.student_id
                        group by s.student_id, s.name
                        """;
                break;

            case 6:
                sql = """
                        select s.name, count(e.course_id) as course_count
                        from students s
                        join enrollments e on s.student_id = e.student_id
                        group by s.student_id, s.name
                        having count(e.course_id) > 1
                        """;
                break;

            default:
                System.out.println("Invalid choice!");
                return;
        }

        try (Connection con = DataConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            for (int i = 1; i <= columns; i++) {
                System.out.print(md.getColumnLabel(i) + "\t");
            }

            System.out.println();

            boolean found = false;

            while (rs.next()) {
                found = true;

                for (int i = 1; i <= columns; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }

                System.out.println();
            }

            if (!found) {
                System.out.println("No records found!");
            }
        }
    }
}


