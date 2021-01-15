package de.itsstuttgart.chessclient.connection.packet;

import de.itsstuttgart.chessclient.connection.packet.packets.PongPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * created by paul on 01.02.20 at 15:07
 */
public class PacketHandler {

    private List<Packet> processablePackets;

    public PacketHandler() {
        this.processablePackets = new ArrayList<>();
        this.processablePackets.add(new PongPacket());
    }

    public List<Packet> getProcessablePackets() {
        return processablePackets;
    }

    public void processPacket(byte[] packet) {
        for (Packet p : this.getProcessablePackets()) {
            byte[] header = p.getClass().getAnnotation(PacketHeader.class).value();

            if (packet.length >= header.length) {
                boolean validPacket = false;
                for (int i = 0; i < header.length; i++) {
                    if (header[i] == packet[i]) {
                        validPacket = true;
                        break;
                    }
                }
                if (validPacket) {
                    byte[] packetWithoutHeader = new byte[packet.length - header.length];
                    System.arraycopy(packet, header.length, packetWithoutHeader, 0, packetWithoutHeader.length);
                    p.process(packetWithoutHeader);
                }
            }
        }
    }
}
