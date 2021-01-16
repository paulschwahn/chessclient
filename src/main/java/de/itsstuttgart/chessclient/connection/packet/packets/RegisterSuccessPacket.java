package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.controllers.RegisterController;
import javafx.application.Platform;

/**
 * created by paul on 16.01.21 at 11:25
 */
@PacketHeader({0x72, 0x73})
public class RegisterSuccessPacket implements Packet {

    @Override
    public void process(byte[] data) {
        if (ChessClient.instance.windowController.lastSubController instanceof RegisterController) {
            RegisterController controller = (RegisterController) ChessClient.instance.windowController.lastSubController;
            Platform.runLater(() -> {
                controller.registerPanel.setVisible(false);
                controller.registerSuccess.setVisible(true);
            });
        }
    }
}
