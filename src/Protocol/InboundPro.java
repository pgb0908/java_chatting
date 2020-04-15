package Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;



public class InboundPro {

    private static final byte MAGIC_NUM = 14;
    private ByteBuffer buffer;
    

    MyMessage myMsg = new MyMessage();
    
    
    public InboundPro(ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public MyMessage prcess() {
        
        System.out.println("inbound process start");
        //들어온 메시지를 분석
        //헤더 분석
        //헤더는 언제나 8byte
        int offset = 0;
        
        //magic number 처리
        byte mn = this.buffer.get(offset);
        offset++;
        
        if(mn != MAGIC_NUM) {
            //에러
            myMsg.setErr(true);
            System.out.println("[InboudPro] 에러 발생");
        }
        
        //Type 처리
        byte type = this.buffer.get(offset);
        offset++;
        
        //todo casting 할것...
        if(type == (byte) 0) {
            //Regist type = 0;
            myMsg.setType(0);
        }
        else if(type == (byte) 1) {
            //unRegist type = 1;
            myMsg.setType(1);
        }
        else if(type == (byte) 2) {
            //broadcast type = 2;
            myMsg.setType(2);
        }
        else {
            myMsg.setErr(true);
        }
        
        //Resolved 처리
        byte resolve = this.buffer.get(offset);
        offset++;
        
        if(resolve == (byte) 0) {
            //데이터 나눠짐 end = 0;
            myMsg.setEnd(0);
            
        }
        else if(resolve == (byte) 1) {
            //마지막 메세지 의미 end = 1;
            myMsg.setEnd(1);
        }
        else {
            myMsg.setErr(true);
        }
        
        // Future 처리 - 현재 0
        byte f = this.buffer.get(offset);
        offset++;
        
        // DataLenth 처리
        int dl = this.buffer.getInt(offset);
        myMsg.setDataLength(dl);
        offset = offset+4;
        
        if(dl > 0) {
            
            //System.out.println(dl);
            byte [] body = new byte[dl];
            //System.arraycopy(buffer, offset, body, 0, dl);
            int idx = 0;
            while(dl > 0) {
                body[idx] = this.buffer.get(offset);
                idx++;
                offset++;
                dl--;
            }
            //this.buffer.get(body, 0, dl);
            //buffer.get(body);
            //String s = new String(body);
            //System.out.println("body 원조 : " + s);
            myMsg.setBody(body);
        }
        
        return myMsg;
    }

}
