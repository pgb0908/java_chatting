package Protocol;

import java.nio.ByteBuffer;

public class Test {
    

    public static void main(String[] args) {
        
        
        ByteBuffer b = ByteBuffer.allocate(1024);
        String str = "53333";
        byte[] body = str.getBytes();
        int bl = body.length;
        
        b.put((byte) 14);
        b.put((byte) 0);
        b.put((byte) 1);
        b.put((byte) 0);
        b.putInt(bl);
        b.put(body);
        
        
        InboundPro ip = new InboundPro(b);
        MyMessage m = ip.prcess();
        System.out.println("getDataLength() : " + m.getDataLength());
        System.out.println("getType() : " + m.getType());
        System.out.println("getErr() : " + m.getErr());
        byte[] body2 = m.getBody();
        System.out.println("body: " + new String(body2));
        
    }

}
