package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

    @FXML
    public AnchorPane registerSuccess;

    @FXML
    public AnchorPane registerPanel;

    public void initialize() {
        ChessClient.instance.windowController.lastSubController = this;
    }

    public void register(ActionEvent event) {
        final PseudoClass invalidClass = PseudoClass.getPseudoClass("invalid");
        if (password.getText().equals(repeatPassword.getText())) {
            if (password.getText().length() >= 8 && !username.getText().trim().isEmpty()) {
                byte[] register = new byte[2 +
                        DataType.getSize(DataType.SHORT) + username.getText().length() +
                        DataType.getSize(DataType.SHORT) + password.getText().length()];

                int pointer = 0;
                register[pointer++] = 0x72; // r
                register[pointer++] = 0x65; // e
                pointer = ByteUtils.writeBytes(register, pointer, username.getText());
                ByteUtils.writeBytes(register, pointer, password.getText());

                ChessClient.instance.connection.send(register);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registrierung");
                alert.setHeaderText("Fehler bei der Registrierung!");
                alert.setContentText("Die mindestlänge für ein Passwort beträgt 8 Zeichen!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registrierung");
            alert.setHeaderText("Fehler bei der Registrierung!");
            alert.setContentText("Die beiden Passwörter stimmen nicht überein!");
            alert.showAndWait();
            repeatPassword.pseudoClassStateChanged(invalidClass, true);
        }
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
