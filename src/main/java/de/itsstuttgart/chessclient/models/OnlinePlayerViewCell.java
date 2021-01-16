package de.itsstuttgart.chessclient.models;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
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
            OnlinePlayer player = itemProperty().get();
            byte[] challenge = new byte[2 + DataType.getSize(DataType.LONG) * 2];
            int pointer = 0;
            pointer = ByteUtils.writeBytes(challenge, pointer, (byte) 0x63);
            pointer = ByteUtils.writeBytes(challenge, pointer, (byte) 0x2b);
            pointer = ByteUtils.writeBytes(challenge, pointer, player.getPlayerIdentifier().getMostSignificantBits());
            ByteUtils.writeBytes(challenge, pointer, player.getPlayerIdentifier().getLeastSignificantBits());

            ChessClient.instance.connection.send(challenge);
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
            if (!item.isSelf()) {
                if (item.isInGame()) {
                    setText(item.getPlayerName() + " (Im Spiel)");
                } else {
                    setText(item.getPlayerName());
                }
            } else {
                setContextMenu(null);
                setText(item.getPlayerName() + " (Sie)");
            }
        }
    }
}
