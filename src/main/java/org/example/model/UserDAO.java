package org.example.model;

import org.example.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public boolean loginWithStatement(String username, String password) {
        String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            boolean loggedIn = rs.next();
            System.out.println("[Statement] Login con '" + username + "' y '" + password + "': " + loggedIn);
            return loggedIn;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método seguro con PreparedStatement (protegido contra SQL Injection)
    public boolean loginWithPreparedStatement(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            boolean loggedIn = rs.next();
            System.out.println("[PreparedStatement] Login con '" + username + "' y '" + password + "': " + loggedIn);
            return loggedIn;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para verificar si un usuario existe en la base de datos
    public boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Devuelve true si el usuario ya existe
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para insertar usuario usando PreparedStatement (SEGURO)
    public void insertUserWithPreparedStatement(User user) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            pstmt.executeUpdate();
            System.out.println("Usuario insertado con PreparedStatement.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener todos los usuarios
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
