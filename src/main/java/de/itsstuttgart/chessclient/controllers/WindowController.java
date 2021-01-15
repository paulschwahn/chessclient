package de.itsstuttgart.chessclient.controllers;

import de.itsstuttgart.chessclient.ChessClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * created by paul on 15.01.21 at 12:13
 */
public class WindowController {

    @FXML
    public AnchorPane titleBar;

    // Current Window Content
    @FXML
    public Pane content;

    // Drag Controls
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() throws IOException {
        // Set Instance
        ChessClient.instance.windowController = this;

        // Drag-Able
        this.titleBar.setOnMousePressed(event -> {
            this.xOffset = event.getSceneX();
            this.yOffset = event.getSceneY();
        });
        this.titleBar.setOnMouseDragged(event -> {
            this.titleBar.getScene().getWindow().setX(event.getScreenX() - this.xOffset);
            this.titleBar.getScene().getWindow().setY(event.getScreenY() - this.yOffset);
        });

        Parent loginPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/login.fxml"));
        this.changePane(loginPane);
    }

    public void requestShutdown(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void changePane(Parent newPane) {
        this.content.getChildren().clear();
        this.content.getChildren().add(newPane);
    }
}
