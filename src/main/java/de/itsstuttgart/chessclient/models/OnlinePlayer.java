package de.itsstuttgart.chessclient.models;

import java.util.Objects;
import java.util.UUID;

/**
 * created by paul on 16.01.21 at 14:18
 */
public class OnlinePlayer {

    private final UUID playerIdentifier;
    private final String playerName;

    private byte flags;

    public OnlinePlayer(UUID playerIdentifier, String playerName) {
        this.playerIdentifier = playerIdentifier;
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerIdentifier() {
        return playerIdentifier;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public boolean isInGame() {
        return ((this.flags >> 0) & 1) == 1;
    }

    public boolean isSelf() {
        return ((this.flags >> 1) & 1) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlinePlayer that = (OnlinePlayer) o;
        return Objects.equals(playerIdentifier, that.playerIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIdentifier);
    }
}
