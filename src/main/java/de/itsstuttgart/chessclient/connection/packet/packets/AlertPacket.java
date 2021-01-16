package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * created by paul on 16.01.21 at 11:20
 */
@PacketHeader({0x61, 0x6c})
public class AlertPacket implements Packet {

    public static final Alert[] alertMessages = new Alert[3];

    static {
        // Register user already exists
        alertMessages[0] = new Alert(Alert.AlertType.ERROR);
        alertMessages[0].setTitle("Registrierung");
        alertMessages[0].setHeaderText("Fehler bei der Registrierung!");
        alertMessages[0].setContentText("Der Benutzername ist bereits vergeben!");

        // Invalid credentials
        alertMessages[1] = new Alert(Alert.AlertType.WARNING);
        alertMessages[1].setTitle("Anmeldung");
        alertMessages[1].setHeaderText("Fehler bei der Anmeldung!");
        alertMessages[1].setContentText("Ungültiger Benutzername oder Passwort!");

        // User disconnected
        alertMessages[2] = new Alert(Alert.AlertType.INFORMATION);
        alertMessages[2].setTitle("Herausforderung");
        alertMessages[2].setHeaderText("Fehler bei der Spielinitialisierung");
        alertMessages[2].setContentText("Der Spieler hat das Programm geschlossen.");
    }

    @Override
    public void process(byte[] data) {
        byte messageType = data[0];
        Platform.runLater(() -> alertMessages[messageType].showAndWait());
    }
}
