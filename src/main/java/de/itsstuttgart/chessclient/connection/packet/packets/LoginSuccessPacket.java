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
        ChessClient.instance.connection.username = ByteUtils.readString(data, 2, ByteUtils.readShort(data, 0));
        int add = ChessClient.instance.connection.username.length();
        String theme = ByteUtils.readString(data, 4 + add, ByteUtils.readShort(data, 2 + add));
        System.out.println("Received server theme: " + theme);
        ChessClient.instance.themeManager.setCurrentThemeDirect(theme);
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
