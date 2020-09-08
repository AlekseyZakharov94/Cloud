package com.AlekseyZakharov.Cloud_Client;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseHandler extends DBConfigs {
    Connection dbConnection;

    public Connection getDbConnection()throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName; //jdbc:mysql://localhost:3306/mysql
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    public void signUpUser(String first_name, String last_name, String email, String country, String city,
                           String login, String password){
        String insert = "INSERT INTO " + DBConst.USERS_TABLE + " (" + DBConst.USERS_FIRST_NAME + ", " + DBConst.USERS_LAST_NAME +
                 " ," + DBConst.USERS_EMAIL + ", " + DBConst.USERS_COUNTRY + ", " + DBConst.USERS_CITY + ", " +
                DBConst.USERS_LOGIN + ", " + DBConst.USERS_PASSWORD + ")" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = getDbConnection().prepareStatement(insert);
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            statement.setString(4, country);
            statement.setString(5, city);
            statement.setString(6, login);
            statement.setString(7, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "This email or login is already existing",
                    ButtonType.APPLY);
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
