package de.itsstuttgart.chessclient.models;

import java.util.Objects;

/**
 * created by paul on 16.01.21 at 14:18
 */
public class OnlinePlayer {

    private final String playerName;

    public OnlinePlayer(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlinePlayer that = (OnlinePlayer) o;
        return Objects.equals(playerName, that.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName);
    }
}
