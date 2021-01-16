package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.models.OnlinePlayer;
import de.itsstuttgart.chessclient.models.OnlinePlayerViewCell;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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
        this.welcomeMessage.setText("Willkommen, " + ChessClient.instance.connection.username);

        // Init Online Player List
        this.onlineList.setItems(ChessClient.instance.connection.players);
        this.onlineList.setCellFactory(listView -> new OnlinePlayerViewCell());
    }
}
