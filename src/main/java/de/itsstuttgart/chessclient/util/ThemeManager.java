package de.itsstuttgart.chessclient.util;

import de.itsstuttgart.chessclient.chess.Side;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * created by paul on 16.01.21 at 19:08
 */
public class ThemeManager {

    private final Map<String, Image> cache = new HashMap<>();
    public String currentTheme;

    public ThemeManager() {
        this.currentTheme = "blacknwhite";
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }

    public Image getPieceImage(String name, Side side) {
        String path = "/assets/pieces/" + currentTheme + "/" + name + side.getImageAddon() + ".png";
        if (cache.containsKey(path))
            return cache.get(path);

        cache.put(path, new Image(this.getClass().getResourceAsStream(path)));
        return this.getPieceImage(name, side);
    }
}
