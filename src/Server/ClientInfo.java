package Server;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientInfo {
    
    private static final int BUF_SIZE = 256;
    
    private String clientID;
    private String clientIP;
    private SocketChannel sc;
    
    ByteBuffer readBuffer = ByteBuffer.allocate(BUF_SIZE);
    ByteBuffer writeBuffer = ByteBuffer.allocate(BUF_SIZE);
    
    
    ClientInfo(String clientID, String clientIP, SocketChannel sc) {
        this.setClientID(clientID);
        this.setClientIP(clientIP);
        this.setSc(sc);
    }


    public String getClientID() {
        return clientID;
    }


    public void setClientID(String clientID) {
        this.clientID = clientID;
    }


    public String getClientIP() {
        return clientIP;
    }
    
    public SocketChannel getSockCh() {
        return this.sc;
    }


    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }


    public void setSc(SocketChannel sc) {
        this.sc = sc;
    }


}
