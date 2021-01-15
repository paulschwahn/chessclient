package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * created by paul on 15.01.21 at 18:23
 */
public class RegisterController {

    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    @FXML
    public PasswordField repeatPassword;

    public void register(ActionEvent event) {
    }

    /**
     * Executed whenever a user clicks the "login" text, redirects back to the login page
     *
     * @param event event information
     * @throws IOException thrown when the login page fails to load
     */
    public void switchToLogin(MouseEvent event) throws IOException {
        Parent loginPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/login.fxml"));
        ChessClient.instance.windowController.changePane(loginPane);
    }
}
