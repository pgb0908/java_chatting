package doTest;

import Server.Server;
import Client.Client;

public class DoTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        Server server = new Server();
        server.start();
        
        
        //System.out.println("Client is initing...");
        Client client1 = new Client("pgb0908");
        client1.start();
        
        //Client client2 = new Client(2);
        //client2.start();

    }

}
