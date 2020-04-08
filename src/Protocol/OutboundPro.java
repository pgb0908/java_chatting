package Protocol;

import java.nio.ByteBuffer;

public class OutboundPro {

    private static final byte MAGIC_NUM = 14;
    private MyMessage mmsg;
    ByteBuffer buffer;
    
    
    public OutboundPro(MyMessage mmsg) {
        this.mmsg = mmsg;
    }

    public ByteBuffer serverProcess() {
        buffer = ByteBuffer.allocate(mmsg.getDataLength()+8);
        //헤더 정보 추가
        //헤더 분석
        //헤더는 언제나 8byte

        //magic number 처리
        buffer.put((byte) MAGIC_NUM);

        //Type 처리
        //서버랑 클라가 상이함
        buffer.put((byte) 2);

        //Resolved 처리
        buffer.put((byte) 1);
        
        // Future 처리 - 현재 0
        buffer.put((byte) 0);

        // DataLenth 처리
        buffer.putInt(this.mmsg.getDataLength());
        
        //body 추가
        buffer.put(this.mmsg.getBody());

        return buffer;
    }
    
    public ByteBuffer clientProcess() {
        
        //헤더 정보 추가
        //헤더 분석
        //헤더는 언제나 8byte
        buffer = ByteBuffer.allocate(mmsg.getDataLength()+8);

        //magic number 처리
        buffer.put((byte) MAGIC_NUM);

        //Type 처리
        //서버랑 클라가 상이함
        switch(this.mmsg.getType()) {
            case 0:
                buffer.put((byte) 0);
                break;
            case 1:
                buffer.put((byte) 1);
                break;
            case 2:
                buffer.put((byte) 2);
                break;
        }
        
        //Resolved 처리
        switch(this.mmsg.getEnd()) {
            case 0:
                buffer.put((byte) 0);
                break;
            case 1:
                buffer.put((byte) 1);
                break;
        }
        
        // Future 처리 - 현재 0
        buffer.put((byte) 0);

        // DataLenth 처리
        // 현재 들어온 메세지 길이
        buffer.putInt(this.mmsg.getDataLength());
        
        //body 추가
        buffer.put(this.mmsg.getBody());

        return buffer;
    }


    
}
