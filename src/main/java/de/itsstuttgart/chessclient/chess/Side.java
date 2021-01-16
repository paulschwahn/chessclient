package de.itsstuttgart.chessclient.chess;

/**
 * created by paul on 16.01.21 at 18:28
 */
public enum Side {

    WHITE("w", ""),
    BLACK("b", "b");

    private final String fen;
    private final String imageAddon;

    Side(String fen, String imageAddon) {
        this.fen = fen;
        this.imageAddon = imageAddon;
    }

    public String getFen() {
        return fen;
    }

    public String getImageAddon() {
        return imageAddon;
    }

    public static Side fromFEN(String fenPart) {
        return fenPart.equals("w") ? WHITE : BLACK;
    }

    public static Side fromCase(boolean upperCase) {
        return upperCase ? WHITE : BLACK;
    }
}
