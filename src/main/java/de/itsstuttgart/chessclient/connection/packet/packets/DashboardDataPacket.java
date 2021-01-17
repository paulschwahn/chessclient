package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.BoardController;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.controllers.DashboardController;
import de.itsstuttgart.chessclient.models.HistoryGame;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * created by paul on 17.01.21 at 21:52
 */
@PacketHeader({0x40, 0x64})
public class DashboardDataPacket implements Packet {

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void process(byte[] data) {
        int pointer = 0;

        int wins = ByteUtils.readInteger(data, pointer);
        pointer += DataType.getSize(DataType.INTEGER);
        int loses = ByteUtils.readInteger(data, pointer);
        pointer += DataType.getSize(DataType.INTEGER);
        int draws = ByteUtils.readInteger(data, pointer);
        pointer += DataType.getSize(DataType.INTEGER);

        List<HistoryGame> games = new ArrayList<>();

        int gamesSent = ByteUtils.readByte(data, pointer);
        pointer += DataType.getSize(DataType.BYTE);
        for (int i = 0; i < gamesSent; i++) {
            long msbMatchId = ByteUtils.readLong(data, pointer);
            pointer += DataType.getSize(DataType.LONG);
            long lsbMatchId = ByteUtils.readLong(data, pointer);
            pointer += DataType.getSize(DataType.LONG);
            UUID matchId = new UUID(msbMatchId, lsbMatchId);

            short fenLength = ByteUtils.readShort(data, pointer);
            pointer += DataType.getSize(DataType.SHORT);
            String fen = ByteUtils.readString(data, pointer, fenLength);

            pointer += fenLength;
            long date = ByteUtils.readLong(data, pointer);
            pointer += DataType.getSize(DataType.LONG);

            boolean win = ByteUtils.readBoolean(data, pointer);
            pointer += DataType.getSize(DataType.BOOLEAN);

            byte reason = ByteUtils.readByte(data, pointer);
            pointer += DataType.getSize(DataType.BYTE);

            boolean playedWhite = ByteUtils.readBoolean(data, pointer);
            pointer += DataType.getSize(DataType.BOOLEAN);

            games.add(new HistoryGame(matchId, fen, date, win, reason, playedWhite));
        }

        Platform.runLater(() -> {
            if (ChessClient.instance.windowController.lastSubController instanceof DashboardController) {
                DashboardController controller = (DashboardController) ChessClient.instance.windowController.lastSubController;
                controller.acceptData(wins, loses, draws, games);
            }
        });
    }
}
