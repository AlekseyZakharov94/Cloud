package com.AlekseyZakharov.Cloud_Client;

import com.AlekseyZakharov.Cloud_Common.DataBaseHandler;
import com.AlekseyZakharov.Cloud_Common.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;


public class SignUpWindowController {
    public TextField fistNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField countryField;
    public TextField cityField;
    public TextField loginField;
    public PasswordField passwordField;
    public Button signUpBtn;

    public void signUp(ActionEvent actionEvent) {
        if (!fistNameField.getText().equals("") && !lastNameField.getText().equals("") && !emailField.getText().equals("") &&
                !countryField.getText().equals("") && !cityField.getText().equals("") && !loginField.getText().equals("") &&
                !passwordField.getText().equals("")) {
            signUpNewUser();//
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields must be filled in",
                    ButtonType.APPLY);
            alert.showAndWait();
        }
    }

    private void signUpNewUser() {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        String first_name = fistNameField.getText();
        String last_name = lastNameField.getText();
        String email = emailField.getText();
        String country = countryField.getText();
        String city = cityField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        User user = new User(first_name, last_name, email, country, city, login, password);
        dataBaseHandler.signUpUser(user);
        new SignInWindowController().startNewScene(signUpBtn, "/signInWindow.fxml", "Cloud SignIn");
    }
}

