package de.itsstuttgart.chessclient.connection.packet.packets;

import de.itsstuttgart.chessclient.connection.packet.Packet;
import de.itsstuttgart.chessclient.connection.packet.PacketHeader;

/**
 * created by paul on 01.02.20 at 15:06
 */
@PacketHeader({0x2d, 0x2d})
public class PongPacket implements Packet {

    @Override
    public void process(byte[] data) {
        //System.out.println("pong received");
    }
}
