package Client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class WriteToServer extends Thread {

    private SocketChannel sc;
    private String clientID;

    WriteToServer(SocketChannel sc, String clientID) {
        this.sc = sc;
        this.clientID = clientID;
    }

    public void run() {
        while(true) {
            writeToServer();
        }

    }

    public void writeToServer() {

        try {
            
            Thread.sleep(500);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            System.out.println("client-[" + this.clientID + "] : ");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.next();
            System.out.println("");
            
            buffer.clear();
            
            //Charset charset = Charset.forName("UTF-8");
            //buffer = charset.encode(msg);
            buffer.put(msg.getBytes());
            //printState(buffer);
            buffer.flip();
            sc.write(buffer);
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }

//    private void printState(ByteBuffer buffer2) {
//        System.out.print("\tposition: " + buffer.position() + ", ");
//        System.out.print("\tlimit: " + buffer.limit() + ", ");
//        System.out.println("\tcapacity: " + buffer.capacity());
//        
//    }
}
