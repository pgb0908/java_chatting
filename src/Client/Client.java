package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Client selector�� �̿��� ReadFromServer ���� (non block) thread�� �߰��Ͽ� WriteToServer
 * Ŭ���̾�Ʈ ����
 * 
 */

public class Client extends Thread {

    private static String address = "127.0.0.1";
    private static int port = 12000;
    private InetSocketAddress connectAddress = new InetSocketAddress(address, port);
    private static SocketChannel sc;
    private Selector selector;
    private Charset charset;
    private CharsetDecoder decoder = null;
    private String clientID;
    
    public Client(String clientID) {
        this.clientID = clientID;
    }


    public void run() {

        if(connectToServer() == false) {
            return;
        }
        
        clientAction();
    }

    private boolean connectToServer() {
        try {
            System.out.println("client ["+this.clientID +"] is connecting...");
            
            selector = Selector.open();
            sc = SocketChannel.open(connectAddress);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);

            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            
            
            return true;
        } catch (IOException e) {
            return false;
        }

    }


    private void clientAction() {
        

        try {
            
            new WriteToServer(sc, clientID).start();

            // selector�� �̿��� read
            for (;;) {

                int keyCount = selector.select();

                if (keyCount == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                //System.out.println("chenck loop1");

                while (iter.hasNext()) {

                    SelectionKey selectionKey = (SelectionKey) iter.next();

                    //System.out.println("chenck loop2");

                    if (!selectionKey.isValid()) {
                        continue;
                    }

                    if (selectionKey.isReadable()) {
                        
                        readFromServer(selectionKey);
                    }

                    iter.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromServer(SelectionKey selectionKey) throws IOException {
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel selectedChannel = (SocketChannel) selectionKey.channel();

        int size = selectedChannel.read(buffer);

        if (size == -1) {
            selectedChannel.close();
            selectionKey.cancel();
            selector.close();
            System.out.print("Client is disconnected!!");
            return;
            
            //to do
            //client close() �Լ� �����
            //writeToServer������ ���� �ǵ���...
        }

        buffer.flip();
        String data = decoder.decode(buffer).toString();
        System.out.print("Server form echo : ");
        System.out.println(data);

        // System.out.println("read result");
        //printState(buffer);
        buffer.clear();
        
    }


    private void printState(ByteBuffer buffer) {
        System.out.print("\tposition: " + buffer.position() + ", ");
        System.out.print("\tlimit: " + buffer.limit() + ", ");
        System.out.println("\tcapacity: " + buffer.capacity());
        
    }

}
