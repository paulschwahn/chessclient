package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.models.OnlinePlayer;
import de.itsstuttgart.chessclient.util.ByteUtils;
import javafx.application.Platform;

import java.util.Optional;
import java.util.UUID;

/**
 * created by paul on 16.01.21 at 16:45
 */
@PacketHeader({0x63, 0x2b})
public class ChallengePacket implements Packet {

    @Override
    public void process(byte[] data) {
        long[] uuid = new long[2];
        ByteUtils.readLongs(data, 0, uuid);
        UUID playerIdentifier = new UUID(uuid[0], uuid[1]);

        Optional<OnlinePlayer> playerOptional = ChessClient.instance.connection.players.stream().filter(c -> c.getPlayerIdentifier().equals(playerIdentifier)).findFirst();
        if (playerOptional.isPresent()) {
            OnlinePlayer player = playerOptional.get();

            Platform.runLater(() -> {
                ChessClient.instance.windowController.inviteMessage.setText(player.getPlayerName() + " hat sie zu einer Patrie Schach eingeladen!");
                ChessClient.instance.windowController.chessRequest.setVisible(true);
            });
        }
    }
}
