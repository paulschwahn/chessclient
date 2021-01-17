package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.ChessBoard;
import de.itsstuttgart.chessclient.chess.Side;
import de.itsstuttgart.chessclient.models.HistoryGame;
import de.itsstuttgart.chessclient.models.OnlinePlayer;
import de.itsstuttgart.chessclient.models.OnlinePlayerViewCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.PopupWindow;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * created by paul on 16.01.21 at 12:51
 */
public class DashboardController {

    @FXML
    public Label welcomeMessage;

    @FXML
    public ListView<OnlinePlayer> onlineList;

    public Canvas matchResults;
    public Canvas oldMatch1;
    public Label match1sub;

    public Canvas oldMatch2;
    public Label match2sub;

    public Canvas oldMatch3;
    public Label match3sub;

    @FXML
    public void initialize() {
        ChessClient.instance.windowController.lastSubController = this;

        this.welcomeMessage.setText("Willkommen, " + ChessClient.instance.connection.username);

        // Init Online Player List
        this.onlineList.setItems(ChessClient.instance.connection.players);
        this.onlineList.setCellFactory(listView -> new OnlinePlayerViewCell());

        GraphicsContext gc = matchResults.getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, matchResults.getWidth(), matchResults.getHeight());

        ChessClient.instance.connection.send(new byte[] {0x40, 0x64}); // get dashboard data
    }

    public void switchToSettings(ActionEvent event) throws IOException {
        Parent settingsPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/settings.fxml"));
        ChessClient.instance.windowController.changePane(settingsPane);
    }

    public void acceptData(int wins, int loses, int draws, List<HistoryGame> pastGames) {
        renderGraph(wins, loses, draws);

        if (pastGames.size() > 0) {
            HistoryGame game = pastGames.get(0);
            ChessBoard fenBoard = new ChessBoard(game.getFen(), Side.fromCase(game.hasPlayedWhite()));
            fenBoard.drawBoard(oldMatch1.getGraphicsContext2D(), oldMatch1.getWidth() / 8);
            this.match1sub.setText(game.getFinishReason().isDraw() ? "Unentschieden" : (game.isWin() ? "Gewonnen" : "Verloren"));
            Tooltip tooltip = new Tooltip("Gespielt am: " + new SimpleDateFormat().format(new Date(game.getGameEnd())) + "\n" +
                    "Ergebnis: " + (game.getFinishReason().isDraw() ? game.getFinishReason().getDrawText() : (game.isWin() ? game.getFinishReason().getWinText() : game.getFinishReason().getLooseText())));
            tooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
            this.match1sub.setTooltip(tooltip);
        }

        if (pastGames.size() > 1) {
            HistoryGame game = pastGames.get(1);
            ChessBoard fenBoard = new ChessBoard(game.getFen(), Side.fromCase(game.hasPlayedWhite()));
            fenBoard.drawBoard(oldMatch2.getGraphicsContext2D(), oldMatch2.getWidth() / 8);
            this.match2sub.setText(game.getFinishReason().isDraw() ? "Unentschieden" : (game.isWin() ? "Gewonnen" : "Verloren"));
            Tooltip tooltip = new Tooltip("Gespielt am: " + new SimpleDateFormat().format(new Date(game.getGameEnd())) + "\n" +
                    "Ergebnis: " + (game.getFinishReason().isDraw() ? game.getFinishReason().getDrawText() : (game.isWin() ? game.getFinishReason().getWinText() : game.getFinishReason().getLooseText())));
            tooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
            this.match2sub.setTooltip(tooltip);
        }

        if (pastGames.size() > 2) {
            HistoryGame game = pastGames.get(2);
            ChessBoard fenBoard = new ChessBoard(game.getFen(), Side.fromCase(game.hasPlayedWhite()));
            fenBoard.drawBoard(oldMatch3.getGraphicsContext2D(), oldMatch3.getWidth() / 8);
            this.match3sub.setText(game.getFinishReason().isDraw() ? "Unentschieden" : (game.isWin() ? "Gewonnen" : "Verloren"));
            Tooltip tooltip = new Tooltip("Gespielt am: " + new SimpleDateFormat().format(new Date(game.getGameEnd())) + "\n" +
                    "Ergebnis: " + (game.getFinishReason().isDraw() ? game.getFinishReason().getDrawText() : (game.isWin() ? game.getFinishReason().getWinText() : game.getFinishReason().getLooseText())));
            tooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
            this.match3sub.setTooltip(tooltip);
        }
    }

    private void renderGraph(int wins, int loses, int draws) {
        int sum = wins + loses + draws;
        double graphWidth = matchResults.getWidth();
        double gH = matchResults.getHeight();

        GraphicsContext gc = matchResults.getGraphicsContext2D();
        gc.clearRect(0, 0, graphWidth, gH);

        double relativeWins = ((double) wins) / ((double) sum);
        double relativeLoses = ((double) loses) / ((double) sum);
        double relativeDraws = ((double) draws) / ((double) sum);

        gc.setFill(new Color(0x4a / 255d, 0x8e / 255d, 0x24 / 255d, .8));
        gc.fillRect(0, 0, relativeWins * graphWidth, gH);
        gc.setFill(Color.WHITESMOKE);
        gc.fillText(String.valueOf(wins), 12, 19d);

        gc.setFill(new Color(0xc6 / 255d, 0x40 / 255d, 0x35 / 255d, .8));
        gc.fillRect(relativeWins * graphWidth, 0, relativeLoses * graphWidth, gH);
        gc.setFill(Color.WHITESMOKE);
        gc.fillText(String.valueOf(loses), relativeWins * graphWidth + 12, 19d);

        gc.setFill(new Color(0x23 / 255d, 0x23 / 255d, 0x23 / 255d, .8));
        gc.fillRect((relativeWins + relativeLoses) * graphWidth, 0, relativeDraws * graphWidth, gH);
        gc.setFill(Color.WHITESMOKE);
        gc.fillText(String.valueOf(draws), (relativeWins + relativeLoses) * graphWidth + 12, 19d);
    }
}
