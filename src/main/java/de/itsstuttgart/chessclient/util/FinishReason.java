package de.itsstuttgart.chessclient.util;

import java.util.Arrays;

/**
 * created by paul on 17.01.21 at 18:37
 */
public enum FinishReason {

    RESIGNATION(0, "Gewonnen durch Rücktritt", "Verloren durch Rücktritt"),
    CHECKMATE(1, "Gewonnen durch Schachmatt", "Durch Schachmatt verloren"),
    STALEMATE(2, "Unentschieden durch Patt"),
    DRAWACCEPT(3, "Unentschieden durch Remisangebot");

    private final byte reason;
    private String drawText;

    private String winText;
    private String looseText;

    FinishReason(int reason, String winText, String looseText) {
        this.reason = (byte) reason;
        this.winText = winText;
        this.looseText = looseText;
    }

    FinishReason(int reason, String drawText) {
        this.reason = (byte) reason;
        this.drawText = drawText;
    }

    public byte getReason() {
        return reason;
    }

    public String getDrawText() {
        return drawText;
    }

    public String getWinText() {
        return winText;
    }

    public String getLooseText() {
        return looseText;
    }

    public static FinishReason fromReason(byte reason) {
        return Arrays.stream(values()).filter(s -> s.getReason() == reason).findFirst().orElse(RESIGNATION);
    }

    public boolean isDraw() {
        return this.drawText != null;
    }
}
