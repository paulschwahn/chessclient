package de.itsstuttgart.chessclient.util;

import de.itsstuttgart.chessclient.ChessClient;
import de.itsstuttgart.chessclient.chess.Side;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * created by paul on 16.01.21 at 19:08
 */
public class ThemeManager {

    private final Map<String, Image> cache = new HashMap<>();
    private ObservableList<String> themes;
    private String currentTheme;

    public ThemeManager() {
        this.currentTheme = "lichess";
        this.themes = FXCollections.observableArrayList("blacknwhite", "fire", "lichess", "sandy");
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;

        byte[] newTheme = new byte[2 + DataType.getSize(DataType.SHORT) + currentTheme.length()];
        newTheme[0] = 0x75;
        newTheme[1] = 0x74;
        ByteUtils.writeBytes(newTheme, 2, currentTheme);
        ChessClient.instance.connection.send(newTheme);
    }

    public void setCurrentThemeDirect(String currentTheme) {
        this.currentTheme = currentTheme;
    }

    public ObservableList<String> getThemes() {
        return themes;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public Image getPieceImage(String name, Side side) {
        String path = "/assets/pieces/" + currentTheme + "/" + name + side.getImageAddon() + ".png";
        if (cache.containsKey(path))
            return cache.get(path);

        cache.put(path, new Image(this.getClass().getResourceAsStream(path)));
        return this.getPieceImage(name, side);
    }
}
