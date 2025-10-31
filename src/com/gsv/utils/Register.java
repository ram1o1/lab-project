package com.gsv.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register {
    
    // Database constants copied from other utility/data classes
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
    
    /**
     * Registers a new course for a student by updating the StudentData table.
     * * @param studentId The ID of the student.
     * @param newCourses The new comma-separated string of all courses (including the newly registered one).
     * @return true if the database update was successful, false otherwise.
     */
    public static boolean registerStudentCourse(int studentId, String newCourses) {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql_update = "UPDATE StudentData SET courses = ? WHERE student_id = ?";
        boolean success = false;

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_update);
            stmt.setString(1, newCourses);
            stmt.setInt(2, studentId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException ex) {
            System.err.println("Database error during course registration for student ID: " + studentId);
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }

    /**
     * Drops a course for a student by updating the StudentData table.
     * @param studentId The ID of the student.
     * @param newCourses The new comma-separated string of all courses (excluding the dropped one).
     * @return true if the database update was successful, false otherwise.
     */
    public static boolean dropStudentCourse(int studentId, String newCourses) {
        Connection con = null;
        PreparedStatement stmt = null;
        String sql_update = "UPDATE StudentData SET courses = ? WHERE student_id = ?";
        boolean success = false;

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_update);
            // Set to null if the resulting string is empty
            stmt.setString(1, newCourses.isEmpty() ? null : newCourses); 
            stmt.setInt(2, studentId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException ex) {
            System.err.println("Database error during course drop for student ID: " + studentId);
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }
}