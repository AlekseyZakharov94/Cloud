package com.AlekseyZakharov.Cloud_Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInWindowController {

    public Button signUpButton;
    public PasswordField passwordField;
    public TextField loginField;
    public Button signInButton;

    public void openSignUpWindow(ActionEvent actionEvent) {
        startNewScene(signUpButton, "/signUpWindow.fxml", "Cloud SignUp");
    }

    public void signIn(ActionEvent actionEvent) {
        String loginText = loginField.getText().trim();
        String passwordText = passwordField.getText().trim();
        if (!loginText.equals("") && !passwordText.equals("")) {
            signInUser(loginText, passwordText);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Login and password fields shouldn't be empty",
                    ButtonType.APPLY);
            alert.showAndWait();
        }
    }

    private void signInUser(String loginText, String passwordText) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(passwordText);
        ResultSet resultSet = dataBaseHandler.findUser(user);
        int counter = 0;
       try {
           while (resultSet.next()){
               counter++;
           }
       }catch (SQLException e){
           e.printStackTrace();
       }
        if (counter > 0){
            startNewScene(signInButton, "/main.fxml", "Cloud user " + user.getLogin());
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "There is no users with this login and password." +
                    "Please, enter the correct data or sign up",
                    ButtonType.APPLY);
            alert.showAndWait();
        }

    }

    public void startNewScene(Control control, String name, String title) {
        control.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(name));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}

