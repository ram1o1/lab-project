package com.gsv.utils;
import java.sql.*;

// Removed Admin, Professor, Student imports as they are no longer instantiated here.

public class LoginAuthentication {
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/gsv";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "4041";

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            throw new SQLException(e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    // New method to retrieve the student_id upon successful login
    public static Integer getAuthenticatedStudentId(String userName, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Integer studentId = null;

        String sql_query = "SELECT student_id FROM StudentLoginData WHERE email_id = ? AND password = ?";

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_query);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                studentId = rs.getInt(1); // Get the student_id
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return studentId; // Returns student_id or null
    }

    public static boolean StudentAuthentication(String userName, String password) {
        // Now just checks if a student ID is returned
        return getAuthenticatedStudentId(userName, password) != null;
    }

    // Professor and Admin authentication methods remain mostly the same for backward compatibility
    public static boolean ProfessorAuthentication(String userName, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isAuthenticated = false;

        String sql_query = "SELECT * FROM ProfessorsLoginData WHERE email_id = ? AND password = ?";

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_query);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            isAuthenticated = rs.next();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return isAuthenticated;
        
    }

    public static boolean AdministratorAuthentication(String userName, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isAuthenticated = false;

        String sql_query = "SELECT * FROM AdminLoginData WHERE username = ? AND password = ?";

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_query);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            isAuthenticated = rs.next();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return isAuthenticated;
    }
}