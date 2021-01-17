package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.ChessBoard;
import de.itsstuttgart.chessclient.chess.Side;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.UUID;

/**
 * created by paul on 17.01.21 at 11:42
 */
@PacketHeader({0x73, 0x67})
public class MatchStartPacket implements Packet {

    @Override
    public void process(byte[] data) {
        long[] uuid = new long[2];
        ByteUtils.readLongs(data, 0, uuid);
        UUID matchIdentifier = new UUID(uuid[0], uuid[1]);

        Side side = Side.fromFEN(Character.toString((char) data[DataType.getSize(DataType.LONG) * 2]));

        short opponentNameLength = ByteUtils.readShort(data, DataType.getSize(DataType.LONG) * 2 + 1);
        ChessClient.instance.connection.opponentName = ByteUtils.readString(data, DataType.getSize(DataType.LONG) * 2 + 3, opponentNameLength);
        ChessClient.instance.board = new ChessBoard(matchIdentifier, side);
        Platform.runLater(() -> {
            try {
                Parent gameBoard = FXMLLoader.load(this.getClass().getResource("/assets/fxml/game.fxml"));
                ChessClient.instance.windowController.changePane(gameBoard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
