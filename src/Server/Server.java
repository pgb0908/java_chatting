package Server;

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
	private static final int BUF_SIZE = 256;
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
						accept(selector, ssc);
						showClientList();
					}

					if (selectionKey.isReadable()) {
						readFromClient(selectionKey);
					}

					iter.remove();
					
					//showClientList();
				}
			}

		} catch (Exception e) {

			stopServer();
		}
	}

	public void readFromClient(SelectionKey selectionKey) throws IOException {
		
		ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
		//byte [] cpy_byte = new byte[BUF_SIZE];
		
		SocketChannel client = (SocketChannel) selectionKey.channel();
		
		int n = client.read(buffer);
		

//		if (new String(buffer.array()).trim().contentEquals(EXIT)) {
//			client.close();
//			System.out.println("Client disconnect!!");
//		}
		
		if(n < 0) {
			/**to do
			 * 에러 처리
			 */
			return;
		}else {
			buffer.flip();
			broadcast(buffer);
		}
		
		//System.out.println("readFromClient!!");
//		buffer.flip();
//		client.write(buffer);
//		buffer.clear();
		// System.out.println("Server echo writting... ");
	}

	private void broadcast(ByteBuffer buffer) throws IOException {
		
		ByteBuffer msgBuf=buffer;
		for(SelectionKey key : selector.keys()) {
			if(key.isValid() && key.channel() instanceof SocketChannel) {
				SocketChannel sch=(SocketChannel) key.channel();
				sch.write(msgBuf);
				msgBuf.rewind();
			}
		}
		
	}

	public void accept(Selector selector, ServerSocketChannel ssc) {

		if (newClientCheck() == false) {
			/**
			 * to do 고객에게 ~ 조건으로 서버 진입 불가하다고 알리기
			 * 
			 */
			return;
		}

		if (acceptNewClient() == false) {
			stopServer();
			return;
		}

		//System.out.println("accept : " + clientList.toString());

		System.out.println("[Server] : client is connected");
	}

	public boolean acceptNewClient() {

		try {
			SocketChannel clientFD = ssc.accept();
			clientFD.configureBlocking(false);
			clientFD.register(selector, SelectionKey.OP_READ);

			cliNum++;
			String clientID = "Client_" + cliNum;
			ClientInfo clientInfo = new ClientInfo(clientID, clientFD.getRemoteAddress().toString(), clientFD);
			clientList.add(clientInfo);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	boolean newClientCheck() {

		// 최대 접속자 수 비교

		return true;
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
//		//iter.remove(); remove 쓰면 안된다잉
		
		
		for (ClientInfo ci : clientList) {
		    System.out.print(ci.getClientIP() + " " + ci.getClientID());
		    System.out.println();
		}
		System.out.println("--------- client list End ------------");
		System.out.println("");
	}
}
