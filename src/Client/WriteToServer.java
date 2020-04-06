package Client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class WriteToServer extends Thread {

    private SocketChannel sc;
    private int clientNum = 0;

    WriteToServer(SocketChannel sc, int clientNum) {
        this.sc = sc;
        this.clientNum = clientNum;
    }

    public void run() {
        while(true) {
            writeToServer();
        }

    }

    private void writeToServer() {

        try {
            
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            System.out.print("client_" + this.clientNum + " : ");
            Scanner scanner = new Scanner(System.in);;
            System.out.println("");
            String msg = scanner.next();
            
            buffer.clear();
            
            //Charset charset = Charset.forName("UTF-8");
            //buffer = charset.encode(msg);
            buffer.put(msg.getBytes());
            //printState(buffer);
            buffer.flip();
            sc.write(buffer);
            
        } catch (IOException e) {
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
