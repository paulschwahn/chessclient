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
 * created by paul on 15.01.21 at 17:45
 */
public class LoginController {

    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    /**
     * Called whenever a user presses the login button
     *
     * @param event event information
     */
    public void login(ActionEvent event) {
    }

    /**
     * Called when a user clicks the "register" text
     *
     * @param event event information
     */
    public void switchToRegister(MouseEvent event) throws IOException {
        Parent loginPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/register.fxml"));
        ChessClient.instance.windowController.changePane(loginPane);
    }
}
