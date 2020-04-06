package Server;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientInfo {
    
    private static final int BUF_SIZE = 256;
    
    private int clienID;
    private String clientName;
    SocketChannel sc;
    
    ByteBuffer readBuffer = ByteBuffer.allocate(BUF_SIZE);
    ByteBuffer writeBuffer = ByteBuffer.allocate(BUF_SIZE);
    
    
    ClientInfo(int clienID, String clientName, SocketChannel sc) {
        this.setClienID(clienID);
        this.setClientName(clientName);
        this.sc = sc;
    }


    public String getClientName() {
        return clientName;
    }


    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public int getClienID() {
        return clienID;
    }


    public void setClienID(int clienID) {
        this.clienID = clienID;
    }



}
