package Server;

import Protocol.InboundPro;
import Protocol.MyMessage;
import Protocol.OutboundPro;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.sun.xml.internal.bind.v2.model.core.ClassInfo;

public class Server extends Thread {

    private static final int SERVER_PORT = 12000;
    private static final int BUF_SIZE = 128;
    private static final String EXIT = "exit";
    Selector selector;
    ServerSocketChannel ssc;
    private int cliNum = 0;

    private static ArrayList<ClientInfo> clientList = new ArrayList<ClientInfo>();

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

            // for (int i=0; i < 5; i++) {
            for (;;) {

                int keyCount = selector.select();

                if (keyCount == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {

                    SelectionKey selectionKey = iter.next();

                    if (selectionKey.isAcceptable()) {
                        acceptProcess(selector, ssc);
                        showClientList();
                    }

                    if (selectionKey.isReadable()) {
                        readFromClient(selectionKey);
                    }

                    if (selectionKey.isWritable()) {
                        writeToClient(selectionKey);
                    }

                    iter.remove();
                }
            }

        } catch (Exception e) {

            stopServer();
        }
    }

    private void writeToClient(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();

        for (ClientInfo clientInfo : clientList) {
            if (sc.equals(clientInfo.getSockCh())) {
                ByteBuffer buffer = clientInfo.writeBuffer;
                buffer.flip();
                sc.write(buffer);

                if (buffer.hasRemaining()) {
                    buffer.compact();
                } else {
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }

        }

    }

    public void readFromClient(SelectionKey selectionKey) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
        // byte [] cpy_byte = new byte[BUF_SIZE];

        SocketChannel client = (SocketChannel) selectionKey.channel();

        int n = client.read(buffer);

        if (n < 0) {
            /**
             * TODO ���� ó��
             */

            return;
        } else {
            buffer.flip();
            System.out.println("[Server] : inbound process");
            InboundPro inpro = new InboundPro(buffer);
            MyMessage mmsg = inpro.prcess();

            // switch�� ���� ����
            switch (mmsg.getType()) {

            case 0:
                // TODO - register
                // hash map Ž�� --> update
                // client���� �뺸??

                break;

            case 1:
                // TODO - unregister
                // fd�� Ű�� hash map
                // fd�� �̿��� �˻� �� ����
                break;

            case 2:
                // broadcast
                broadcast(mmsg, selectionKey);

                break;
            }

        }

    }

    private void broadcast(MyMessage mmsg, SelectionKey selectionKey) throws IOException {

        System.out.println("[Server] : broadcast process");
        OutboundPro outPro = new OutboundPro(mmsg);

        ByteBuffer msgBuf = outPro.process();

        // TODO read üũ
        msgBuf.flip();
        for (ClientInfo cl : clientList) {
            cl.getSockCh().write(msgBuf);

            if (msgBuf.hasRemaining()) {
                ByteBuffer remainBuffer = msgBuf.duplicate();
                // Ŭ���̾�Ʈ �����ϴ� ����Ʈ���ۿ� ���
                cl.writeBuffer.put(remainBuffer);
                selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }

            msgBuf.rewind();
            System.out.println("[Server] : ����!!");
        }

    }

    public void acceptProcess(Selector selector, ServerSocketChannel ssc) {

        if (serverConditionChk() == false) {
            /**
             * TODO ������ ~ �������� ���� ���� �Ұ��ϴٰ� �˸���
             * 
             */
            return;
        }

        if (acceptNewClient() == false) {
            return;
        }

        // System.out.println("accept : " + clientList.toString());

        System.out.println("[Server] : client is connected");
    }

    private boolean serverConditionChk() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean acceptNewClient() {

        try {
            // TODO
            // accept�� ��� >> �ش� Ŭ�����̳�Ʈ�� accept �Ǿ����� Ȯ���� �ʿ�!!
            // or ������ thread�� accept ó��
            SocketChannel clientFD = ssc.accept();
            if (clientFD != null) {

                clientFD.configureBlocking(false);
                clientFD.register(selector, SelectionKey.OP_READ);

                cliNum++;
                String clientID = "Client_" + cliNum;
                ClientInfo clientInfo = new ClientInfo(clientID, clientFD.getRemoteAddress().toString(), clientFD);
                clientList.add(clientInfo);

                return true;
            } else {
                // clientFD�� ����� accept ���� ����
                return false;

            }

        } catch (Exception e) {
            return false;
        }
    }

    public void stopServer() {
        try {

            if (ssc != null && ssc.isOpen()) {
                ssc.close();
            }

            if (selector != null && selector.isOpen()) {
                selector.close();
            }

        } catch (Exception e) {
            System.out.println("Server has Problem when shutting down server...");
        }
    }

    public void showClientList() {

        System.out.println("");
        System.out.println("--------- client list Start ------------");
        System.out.println("tota client : " + cliNum);

//		Iterator iter = clientList.iterator();
//		while (iter.hasNext()) {
//			ClientInfo ci = (ClientInfo) iter.next();
//			System.out.print(ci.getClientIP() + " " + ci.getClientID());
//			System.out.println("");
//		}
//		//iter.remove(); remove ���� �ȵȴ���

        for (ClientInfo ci : clientList) {
            System.out.print(ci.getClientIP() + " " + ci.getClientID());
            System.out.println();
        }
        System.out.println("--------- client list End ------------");
        System.out.println("");
    }
}
