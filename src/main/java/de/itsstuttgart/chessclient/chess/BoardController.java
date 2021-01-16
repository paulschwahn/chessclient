package de.itsstuttgart.chessclient.chess;

import de.itsstuttgart.chessclient.ChessClient;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * created by paul on 16.01.21 at 17:51
 */
public class BoardController {

    public Canvas canvasBoard;

    private ChessBoard board;

    public void initialize() {
        ChessClient.instance.windowController.lastSubController = this;
        this.board = new ChessBoard();

        this.board.drawBoard(canvasBoard.getGraphicsContext2D(), canvasBoard.getWidth() / 8);
    }

    public void selectPiece(MouseEvent event) {
        int pX = (int) Math.floor(event.getX() / (canvasBoard.getWidth() / 8));
        int pY = (int) Math.floor(event.getY() / (canvasBoard.getWidth() / 8));
        this.board.click(canvasBoard.getGraphicsContext2D(), canvasBoard.getWidth() / 8, pX, pY);
    }
}
