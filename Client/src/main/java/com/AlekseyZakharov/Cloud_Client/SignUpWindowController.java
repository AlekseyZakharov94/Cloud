package com.AlekseyZakharov.Cloud_Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

public class SignUpWindowController {
    public TextField fistNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField countryField;
    public TextField cityField;
    public TextField loginField;
    public PasswordField passwordField;
    public Button signUpBtn;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    public void signUp(ActionEvent actionEvent) {
        if (!fistNameField.getText().equals("") && !lastNameField.getText().equals("") && !emailField.getText().equals("") &&
                !countryField.getText().equals("") && !cityField.getText().equals("") && !loginField.getText().equals("") &&
                !passwordField.getText().equals("")){
            dataBaseHandler.signUpUser(fistNameField.getText(), lastNameField.getText(), emailField.getText(),
                    countryField.getText(), cityField.getText(), loginField.getText(), passwordField.getText());

            signUpBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/signInWindow.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cloud SignIn");
            stage.setResizable(false);
            stage.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled in",
                    ButtonType.APPLY);
            alert.showAndWait();
        }
    }
}

