package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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

    public void initialize() {
        ChessClient.instance.windowController.lastSubController = this;
    }

    /**
     * Called whenever a user presses the login button
     *
     * @param event event information
     */
    public void login(ActionEvent event) {
        if (!username.getText().trim().isEmpty() && !password.getText().trim().isEmpty()) {
            byte[] login = new byte[2 +
                    DataType.getSize(DataType.SHORT) + username.getText().length() +
                    DataType.getSize(DataType.SHORT) + password.getText().length()];

            int pointer = 0;
            login[pointer++] = 0x6c; // l
            login[pointer++] = 0x6f; // o
            pointer = ByteUtils.writeBytes(login, pointer, username.getText());
            ByteUtils.writeBytes(login, pointer, password.getText());

            ChessClient.instance.connection.send(login);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Anmeldung");
            alert.setHeaderText("Fehler bei der Anmeldung!");
            alert.setContentText("Benutzername und Passwort dürfen nicht leer sein!");
            alert.showAndWait();
        }
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
