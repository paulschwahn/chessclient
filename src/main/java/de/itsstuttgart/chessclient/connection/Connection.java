package de.itsstuttgart.chessclient.connection;

import de.itsstuttgart.chessclient.connection.packet.PacketHandler;
import de.itsstuttgart.chessclient.models.OnlinePlayer;
import de.itsstuttgart.chessclient.util.ByteUtils;
import de.itsstuttgart.chessclient.util.DataType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;

/**
 * created by paul on 15.01.21 at 19:15
 */
public class Connection implements Runnable {

    private Socket socket;
    private Thread receiveThread, pingThread;
    private byte xorSecret;
    private PacketHandler packetHandler;

    public BufferedInputStream inputStream;
    public BufferedOutputStream outputStream;

    // Client variables
    public String username;
    public ObservableList<OnlinePlayer> players;

    public Connection(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);

            this.inputStream = new BufferedInputStream(this.socket.getInputStream());
            this.outputStream = new BufferedOutputStream(this.socket.getOutputStream());

            this.receiveThread = new Thread(this, "Receive Packets");
            this.receiveThread.start();

            this.packetHandler = new PacketHandler();
            this.players = FXCollections.observableArrayList();

            this.pingThread = new Thread(() -> {
                while (this.isConnected()) {
                    try {
                        //noinspection BusyWait
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    byte[] pingPacket = new byte[] {0x2b, 0x2b};
                    this.send(pingPacket);
                }
            }, "Ping");
            this.pingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            InputStream in = this.inputStream;
            this.xorSecret = (byte) in.read();
            while (this.isConnected()) {
                byte[] lengthBytes = new byte[DataType.getSize(DataType.INTEGER)];
                for (int i = 0; i < 4; i++)
                    lengthBytes[i] = (byte) in.read();
                int length = ByteUtils.readInteger(lengthBytes, 0);
                byte[] packet = new byte[length];
                int n = 0;
                while (n < length) {
                    int count = in.read(packet, n, length - n);
                    if (count < 0)
                        throw new EOFException();
                    n += count;
                }
                byte[] xorPacket = new byte[packet.length];
                for (int i = 0; i < packet.length; i++) {
                    xorPacket[i] = (byte) (packet[i] ^ this.xorSecret);
                }
                this.packetHandler.processPacket(xorPacket);
            }
        } catch (Exception e) {
            // TODO: Auto reconnect
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        try {
            byte[] packetSize = new byte[DataType.getSize(DataType.INTEGER)];
            ByteUtils.writeBytes(packetSize, 0, data.length);
            this.outputStream.write(packetSize);
            byte[] xorData = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                xorData[i] = (byte) (data[i] ^ this.xorSecret);
            }
            this.outputStream.write(xorData);
            this.outputStream.flush();
        } catch (Exception e) {
            // TODO: Auto reconnect
            e.printStackTrace();
        }
    }

    /**
     * Checks if the socket of this client is still connected to the server
     * @return connection status
     */
    public boolean isConnected() {
        return this.socket.isConnected() && !this.socket.isClosed() && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown();
    }

    public void disconnect() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}
