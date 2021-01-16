package de.itsstuttgart.chessclient.models;

import de.itsstuttgart.chessclient.ChessClient;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

/**
 * created by paul on 16.01.21 at 14:21
 */
public class OnlinePlayerViewCell extends ListCell<OnlinePlayer> {

    public OnlinePlayerViewCell() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem challengeItem = new MenuItem();
        challengeItem.textProperty().bind(Bindings.format("%s herausfordern", textProperty()));
        challengeItem.setOnAction(event -> {

        });
        contextMenu.getItems().add(challengeItem);

        emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
            if (isNowEmpty) {
                setContextMenu(null);
            } else {
                setContextMenu(contextMenu);
            }
        });
    }

    @Override
    protected void updateItem(OnlinePlayer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
            setContextMenu(null);
        } else {
            if (!item.getPlayerName().equals(ChessClient.instance.connection.username)) {
                setText(item.getPlayerName());
            } else {
                setContextMenu(null);
                setText(item.getPlayerName() + " (Sie)");
            }
        }
    }
}
