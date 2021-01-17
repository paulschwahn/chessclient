package de.itsstuttgart.chessclient.connection.packet.packets.board;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.BoardController;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.util.FinishReason;
import javafx.application.Platform;

/**
 * created by paul on 17.01.21 at 18:49
 */
@PacketHeader({0x2a, 0x66})
public class BoardFinishPacket implements Packet  {

    @Override
    public void process(byte[] data) {
        byte type = data[0];
        FinishReason reason = FinishReason.fromReason(data[1]);
        Platform.runLater(() -> {
            if (ChessClient.instance.windowController.lastSubController instanceof BoardController) {
                BoardController controller = (BoardController) ChessClient.instance.windowController.lastSubController;
                if (type == 0x0) {
                    controller.winText.setText("Gewonnen!");
                    controller.winReason.setText(reason.getWinText());
                } else if (type == 0x1) {
                    controller.winText.setText("Verloren :(");
                    controller.winReason.setText(reason.getLooseText());
                } else {
                    controller.winText.setText("Unentschieden");
                    controller.winReason.setText(reason.getDrawText());
                }
                controller.gameFinished.setVisible(true);
            }
        });
    }
}
