package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.util.ByteUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * created by paul on 16.01.21 at 12:49
 */
@PacketHeader({0x6c, 0x73})
public class LoginSuccessPacket implements Packet {

    @Override
    public void process(byte[] data) {
        String username = ByteUtils.readString(data, 2, ByteUtils.readShort(data, 0));
        ChessClient.instance.connection.username = username;
        Platform.runLater(() -> {
            try {
                Parent dashboard = FXMLLoader.load(this.getClass().getResource("/assets/fxml/dashboard.fxml"));
                ChessClient.instance.windowController.changePane(dashboard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
