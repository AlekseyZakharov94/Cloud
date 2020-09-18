package com.AlekseyZakharov.Cloud_Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInWindowController {

    public Button signUpButton;
    public PasswordField passwordField;
    public TextField loginField;

    public void openSignUpWindow(ActionEvent actionEvent) {
        signUpButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/signUpWindow.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Cloud SignUp");
        stage.setResizable(false);
        stage.showAndWait();
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
    }
}

