package com.AlekseyZakharov.Cloud_Common;

import com.AlekseyZakharov.Cloud_Common.DBConfigs;
import com.AlekseyZakharov.Cloud_Common.DBConst;
import com.AlekseyZakharov.Cloud_Common.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class DataBaseHandler extends DBConfigs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    public void
    signUpUser(User user) {
        String insert = "INSERT INTO " + DBConst.USERS_TABLE + " (" + DBConst.USERS_FIRST_NAME + ", " + DBConst.USERS_LAST_NAME +
                " ," + DBConst.USERS_EMAIL + ", " + DBConst.USERS_COUNTRY + ", " + DBConst.USERS_CITY + ", " +
                DBConst.USERS_LOGIN + ", " + DBConst.USERS_PASSWORD + ")" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = getDbConnection().prepareStatement(insert);
            statement.setString(1, user.getFirst_name());
            statement.setString(2, user.getLast_name());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getCountry());
            statement.setString(5, user.getCity());
            statement.setString(6, user.getLogin());
            statement.setString(7, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "This email or login is already existing",
                    ButtonType.APPLY);
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet findUser(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + DBConst.USERS_TABLE + " WHERE " + DBConst.USERS_LOGIN + " = ? AND " +
                DBConst.USERS_PASSWORD + " = ?";
        try {
            PreparedStatement statement = getDbConnection().prepareStatement(select);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            resultSet = statement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
