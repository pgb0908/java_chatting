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

import Protocol.InboundPro;
import Protocol.MyMessage;

/**
 * Client selector를 이용해 ReadFromServer 실행 (non block) thread를 추가하여 WriteToServer
 * 클라이언트 종료
 * 
 */

public class Client extends Thread {

    private static final int BUF_SIZE = 128;
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

        if (connectToServer() == false) {
            return;
        }

        clientAction();
    }

    private boolean connectToServer() {
        try {
            System.out.println("client [" + this.clientID + "] is connecting...");

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

            // selector를 이용해 read
            for (;;) {

                int keyCount = selector.select();

                if (keyCount == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {

                    SelectionKey selectionKey = (SelectionKey) iter.next();

                    if (!selectionKey.isValid()) {
                        continue;
                    }

                    if (selectionKey.isReadable()) {
                        System.out.print("[client] : 수신 중...");
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

        ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
        SocketChannel selectedChannel = (SocketChannel) selectionKey.channel();
        int size = selectedChannel.read(buffer);

        if (size == -1) {
            selectedChannel.close();
            selectionKey.cancel();
            selector.close();
            System.out.print("Client is disconnected!!");
            return;

            // to do
            // client close() 함수 만들기
            // writeToServer에서도 동작 되도록...
        }

        InboundPro ipro = new InboundPro(buffer);
        MyMessage mmsg = ipro.prcess();

        // 타입별
        if (mmsg.getErr() != true && mmsg.getType() == 2) {
            // buffer.flip();
            // String data = decoder.decode(buffer).toString();
            // read utf 찾아걸것
            String str = new String(mmsg.getBody());
            System.out.print("Server form echo : ");
            System.out.println(str);
            buffer.clear();

        }
    }

    private void printState(ByteBuffer buffer) {
        System.out.print("\tposition: " + buffer.position() + ", ");
        System.out.print("\tlimit: " + buffer.limit() + ", ");
        System.out.println("\tcapacity: " + buffer.capacity());

    }

}
