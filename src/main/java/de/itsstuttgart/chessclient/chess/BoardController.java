package de.itsstuttgart.chessclient.chess;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.util.FinishReason;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * created by paul on 16.01.21 at 17:51
 */
public class BoardController {

    public Canvas canvasBoard;

    @FXML
    public Label selfName;

    @FXML
    public Label opponentName;

    @FXML
    public Button resignButton;

    @FXML
    public AnchorPane gameFinished;
    public Label winText;
    public Label winReason;

    public void initialize() {
        if (ChessClient.instance.board == null) {
            ChessClient.instance.board = new ChessBoard();
            this.selfName.setVisible(false);
            this.opponentName.setVisible(false);
            this.resignButton.setVisible(false);
        } else {
            this.selfName.setText(ChessClient.instance.connection.username + " (Sie)");
            this.opponentName.setText(ChessClient.instance.connection.opponentName);
        }

        ChessClient.instance.windowController.lastSubController = this;
        ChessClient.instance.board.drawBoard(canvasBoard.getGraphicsContext2D(), canvasBoard.getWidth() / 8);
        ChessClient.instance.board.updateNames();
    }

    public void selectPiece(MouseEvent event) {
        if (this.gameFinished.isVisible())
            return;

        int pX = (int) Math.floor(event.getX() / (canvasBoard.getWidth() / 8));
        int pY = (int) Math.floor(event.getY() / (canvasBoard.getWidth() / 8));
        ChessClient.instance.board.click(canvasBoard.getGraphicsContext2D(), canvasBoard.getWidth() / 8, pX, pY);
    }

    public void resign(ActionEvent event) {
        if (this.gameFinished.isVisible())
            return;

        ChessClient.instance.connection.finishGame(FinishReason.RESIGNATION);
    }

    public void toDashboard(ActionEvent event) throws IOException {
        Parent loginPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/dashboard.fxml"));
        ChessClient.instance.windowController.changePane(loginPane);
    }
}
