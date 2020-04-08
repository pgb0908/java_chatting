package Client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import Protocol.MyMessage;
import Protocol.OutboundPro;

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
            
            ByteBuffer buffer = null;
            System.out.println("client-[" + this.clientID + "] : ");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            System.out.println(msg);
            
            //outbound message
            //parsing�ؼ� ��ɾ� �о��
            
            if (msg.isEmpty()) return;
            
            if(msg.charAt(0) == '/' && msg.length() <= 3) {
                System.out.println("[System] : ���� ���� �ʹ� ����");
                return;
            }
            
            String comand = msg.substring(0, 3);
            
            if(comand.equalsIgnoreCase("/b ")) {
                System.out.println("[client] : ��ε�ĳ���� Ȯ��");
                MyMessage mmsg = new MyMessage();
                
                String strMsg = msg.substring(3);
                byte [] byttMsg = strMsg.getBytes();
                
                
                mmsg.setErr(false);
                mmsg.setType(2);
                mmsg.setEnd(1);
                mmsg.setDataLength(byttMsg.length);
                mmsg.setBody(byttMsg);
                
                OutboundPro oPro = new OutboundPro(mmsg);
                buffer = oPro.clientProcess();
                
            }
            else {
                
                return;
            }
            
            buffer.flip();
            sc.write(buffer);
            System.out.println("[client] : ����!!");
            
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
