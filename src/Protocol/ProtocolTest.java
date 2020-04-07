package Protocol;

import java.nio.ByteBuffer;

public class ProtocolTest {

    public static void main(String[] args) {
        
        ByteBuffer b = ByteBuffer.allocate(1024);
        Type type;
        
        String str = "53333";
        byte[] body = str.getBytes();
        
        int bl = body.length;
        //int¸¦ byte·Î ¹Ù²Þ
        
        b.put((byte) 15);
        b.put((byte) 0);
        b.put((byte) 1);
        b.put((byte) 0);
        b.putInt(bl);
        b.put(body);
        
       // System.out.println(b.get());
//        System.out.println(b.get(1));
//        System.out.println(b.get(2));
//        System.out.println(b.get(3));
//        System.out.println(b.get(4));
//        System.out.println(b.get(5));
//        System.out.println(b.get(6));
//        System.out.println(b.get(7));
//        System.out.println(b.get(8));
        
       // b.getInt(index)
        System.out.println(types.valueOf(get(1)));
        System.out.println(b.getInt(4));
        
        
       
//        if(b.get(0) == (byte) 14) {
//            System.out.println("ok");
//        }else {
//            System.out.println("no");
//        }
        
        //System.out.println(b.get(bodyLength));

    }


    private static String get(int i) {
        // TODO Auto-generated method stub
        return null;
    }

}
