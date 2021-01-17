package de.itsstuttgart.chessclient;

import de.itsstuttgart.chessclient.chess.ChessBoard;
import de.itsstuttgart.chessclient.connection.Connection;
import de.itsstuttgart.chessclient.controllers.WindowController;
import de.itsstuttgart.chessclient.util.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * created by paul on 15.01.21 at 11:54
 */
public class ChessClient extends Application {

    public static ChessClient instance;

    /**
     * Main Method, Client Entry Point
     *
     * @param args Console provided arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Main stage window, instance set within controller class
     */
    public WindowController windowController;

    public Connection connection;

    public ThemeManager themeManager;

    public ChessBoard board;

    /**
     * JavaFX Entry Point
     *
     * @param primaryStage Windows instance
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;

        this.connection = new Connection("localhost", 53729);
        this.themeManager = new ThemeManager();

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Chess");

        Parent rootPane = FXMLLoader.load(this.getClass().getResource("/assets/fxml/chess.fxml"));
        Scene mainScene = new Scene(rootPane);
        mainScene.setFill(Color.TRANSPARENT);

        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Called when the JavaFX windows was closed
     *
     * @param event window / event data
     */
    private void closeWindowEvent(WindowEvent event) {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
