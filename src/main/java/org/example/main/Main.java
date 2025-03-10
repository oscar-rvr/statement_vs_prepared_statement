package org.example.main;

import org.example.model.User;
import org.example.model.UserDAO;

public class Main {
    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        String testUsername = "secureUser";
        if (!userDAO.userExists(testUsername)) {
            userDAO.insertUserWithPreparedStatement(new User(testUsername, "safePassword"));
            System.out.println("Usuario '" + testUsername + "' agregado a la base de datos.");
        } else {
            System.out.println("Usuario '" + testUsername + "' ya existe en la base de datos.");
        }

        System.out.println("Datos en la db");
        System.out.println(userDAO.getAllUsers());

        System.out.println("\n Intentando login NORMAL con datos correctos...");
        userDAO.loginWithStatement(testUsername, "safePassword");
        userDAO.loginWithPreparedStatement(testUsername, "safePassword");

        System.out.println("\n Intentando login NORMAL con datos incorrectos...");
        userDAO.loginWithStatement(testUsername, "wrongPassword");
        userDAO.loginWithPreparedStatement(testUsername, "wrongPassword");

        System.out.println("\n Intentando SQL Injection con Statement...");
        userDAO.loginWithStatement("secureUser' --", "anything");

        System.out.println("\n Intentando SQL Injection con PreparedStatement...");
        userDAO.loginWithPreparedStatement("secureUser' --", "anything");

    }
}
