package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.models.OnlinePlayer;
import de.itsstuttgart.chessclient.models.OnlinePlayerViewCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;

/**
 * created by paul on 16.01.21 at 12:51
 */
public class DashboardController {

    @FXML
    public Label welcomeMessage;

    @FXML
    public ListView<OnlinePlayer> onlineList;

    @FXML
    public void initialize() {
        ChessClient.instance.windowController.lastSubController = this;

        this.welcomeMessage.setText("Willkommen, " + ChessClient.instance.connection.username);

        // Init Online Player List
        this.onlineList.setItems(ChessClient.instance.connection.players);
        this.onlineList.setCellFactory(listView -> new OnlinePlayerViewCell());
    }

    public void switchToSettings(ActionEvent event) throws IOException {
        Parent settingsPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/settings.fxml"));
        ChessClient.instance.windowController.changePane(settingsPane);
    }
}
