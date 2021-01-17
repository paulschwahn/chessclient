package de.itsstuttgart.chessclient.connection.packet.packets.board;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.BoardController;
import de.itsstuttgart.chessclient.chess.ChessBoard;
import de.itsstuttgart.chessclient.chess.ChessPiece;
import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import de.itsstuttgart.chessclient.util.FinishReason;
import javafx.application.Platform;

/**
 * created by paul on 17.01.21 at 15:33
 */
@PacketHeader({0x2a, 0x6d})
public class BoardMovePacket implements Packet {

    @Override
    public void process(byte[] data) {
        ChessBoard board = ChessClient.instance.board;
        // from
        int fromCol = data[0] & 0xf;
        int fromRow = data[0] >> 4 & 0xf;

        // to
        int toCol = data[1] & 0xf;
        int toRow = data[1] >> 4 & 0xf;

        if (board != null && board.hasPiece(fromCol, fromRow)) {

            // captures
            if (board.hasPiece(toCol, toRow)) {
                board.getBoard().removeIf(p -> p.getColumn() == toCol && p.getRow() == toRow);
            }

            // move piece
            ChessPiece piece = board.getPiece(fromCol, fromRow);
            piece.setColumn(toCol);
            piece.setRow(toRow);

            if (board.isCheckMate(board.getMySide())) {
                ChessClient.instance.connection.finishGame(FinishReason.CHECKMATE);
            } else if (board.cantMove(board.getMySide())) {
                ChessClient.instance.connection.finishGame(FinishReason.STALEMATE);
            }

            if (ChessClient.instance.windowController.lastSubController instanceof BoardController) {
                BoardController controller = (BoardController) ChessClient.instance.windowController.lastSubController;
                Platform.runLater(() -> board.drawBoard(controller.canvasBoard.getGraphicsContext2D(), controller.canvasBoard.getWidth() / 8d));
            }

            board.flipSide();
        }
    }
}
