package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.Side;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;

/**
 * created by paul on 17.01.21 at 20:18
 */
public class SettingsController {

    public ChoiceBox<String> themeSelector;
    public Canvas preview;

    public void initialize() {
        ChessClient.instance.windowController.lastSubController = this;

        this.themeSelector.setItems(ChessClient.instance.themeManager.getThemes());
        this.themeSelector.setValue(ChessClient.instance.themeManager.getCurrentTheme());

        this.preview.getGraphicsContext2D().setImageSmoothing(false);
        this.paintPreview();

        this.themeSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            ChessClient.instance.themeManager.setCurrentTheme(ChessClient.instance.themeManager.getThemes().get(newVal.intValue()));
            this.paintPreview();
        });
    }

    private void paintPreview() {
        this.preview.getGraphicsContext2D().clearRect(0, 0, this.preview.getWidth(), this.preview.getHeight());

        double small = this.preview.getWidth() * 0.85;
        double left = (this.preview.getWidth() - small) / 2d;

        this.preview.getGraphicsContext2D().drawImage(ChessClient.instance.themeManager.getPieceImage("rook", Side.WHITE), left, left, small, small);
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent dashboard = FXMLLoader.load(this.getClass().getResource("/assets/fxml/dashboard.fxml"));
        ChessClient.instance.windowController.changePane(dashboard);
    }
}
