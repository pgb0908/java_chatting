package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Server extends Thread{
    
    private static final int SERVER_PORT = 12000;
    private static final int BUF_SIZE = 256;
    private static final String EXIT = "exit";
    Selector selector;
    ServerSocketChannel ssc;
    private int cliNum = 0;
    private int cliID;

    Hashtable clientList = new Hashtable();
    
    public void run() {
        startServer();
    }

    public void startServer() {
        System.out.println("Server Socket is starting...");
        
        try {
            
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(SERVER_PORT));
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
            
            for(;;) {
                
                int keyCount = selector.select();
                
                if (keyCount == 0) {
                    continue;
                }
                
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                
                while(iter.hasNext()) {
                    SelectionKey selectionKey = iter.next();
                    
                    if(selectionKey.isAcceptable()) {
                        accept(selector, ssc);
                    }
                    
                    if(selectionKey.isReadable()) {
                        answerWithEcho(buffer, selectionKey);
                    }
                    
                    iter.remove();
                }
            }
            
        }catch(Exception e) {
            
            stopServer();
        }
    }

    private void answerWithEcho(ByteBuffer buffer, SelectionKey selectionKey) throws IOException {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        client.read(buffer);
        
        if(new String(buffer.array()).trim().contentEquals(EXIT)) {
            client.close();
            System.out.println("Client disconnect!!");
        }
        
        buffer.flip();
        client.write(buffer);
        buffer.clear();
        //System.out.println("Server echo writting... ");
    }

    private void accept(Selector selector, ServerSocketChannel ssc) {
            
        if(newClientCheck() == false) {
            return;
        }
        
        if(acceptNewClient(selector, ssc) == false) {
            stopServer();
            return;
        }

    
        System.out.println("[Server] : client_" + cliID + " is connected");
        showClientList();
    }

    private boolean acceptNewClient(Selector selector, ServerSocketChannel ssc) {
        
        try {
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
            
            cliID = cliNum;
            ClientInfo clientInfo = new ClientInfo(cliID, "client", sc);
            cliNum++;
            clientList.put(cliID, clientInfo);
            
            return true;
            
        }catch(Exception e) {
            return false;
        }
    }

    private boolean newClientCheck() {
        // TODO Auto-generated method stub
        return true;
    }

    void stopServer() {
        try {
            
            if(ssc != null && ssc.isOpen()) {
                ssc.close();
            }
            
            if(selector != null && selector.isOpen()) {
                selector.close();
            }
            
        }catch (Exception e) {
            System.out.println("Server has Problem when shutting down server...");
        }
    }
    
    void showClientList() {
        
        Iterator iter = clientList.keySet().iterator();
        
        System.out.println("");
        System.out.println("--------- client list Start ------------");
        while(iter.hasNext()) {
            int key = (int) iter.next();
            System.out.print(key + " ");
            System.out.print(((ClientInfo) clientList.get(key)).getClientName());
            System.out.println("");
            //iter.next();
        }
        iter.remove();
        System.out.println("--------- client list End ------------");
    }
}
