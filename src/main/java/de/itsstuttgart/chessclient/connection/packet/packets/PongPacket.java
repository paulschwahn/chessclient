package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.models.OnlinePlayer;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * created by paul on 01.02.20 at 15:06
 */
@PacketHeader({0x2d, 0x2d})
public class PongPacket implements Packet {

    @Override
    public void process(byte[] data) {
        if (data.length == 0) return;
        Platform.runLater(() -> {
            int pointer = 0;
            int length = ByteUtils.readInteger(data, pointer);
            pointer += DataType.getSize(DataType.INTEGER);

            List<String> players = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                short nameLength = ByteUtils.readShort(data, pointer);
                pointer += DataType.getSize(DataType.SHORT);
                String name = ByteUtils.readString(data, pointer, nameLength);
                pointer += nameLength;

                if (ChessClient.instance.connection.players.stream().noneMatch(p -> p.getPlayerName().equals(name)))
                    ChessClient.instance.connection.players.add(new OnlinePlayer(name));
                players.add(name);
            }

            ChessClient.instance.connection.players.removeIf(p -> players.stream().noneMatch(s -> s.equals(p.getPlayerName())));
        });
    }
}
