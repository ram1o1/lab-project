package com.gsv.utils;
import java.sql.*;

import com.gsv.admin.Admin;
import com.gsv.professor.Professor;
import com.gsv.student.Student;

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
    
    public static boolean StudentAuthentication(String userName, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isAuthenticated = false;

        String sql_query = "SELECT * FROM StudentLoginData WHERE email_id = ? AND password = ?";

        try {
            con = getConnection();
            stmt = con.prepareStatement(sql_query);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            isAuthenticated = rs.next();

            if (isAuthenticated) {
                Student stud = new Student(rs.getInt(1));
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

        return isAuthenticated;
    }

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

            if (isAuthenticated) {
                Professor proff = new Professor(rs.getInt(1));
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

            if (isAuthenticated) {
                Admin admin = new Admin(rs.getInt(1));
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

        return isAuthenticated;
    }
}
