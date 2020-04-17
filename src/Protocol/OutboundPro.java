package Protocol;

import java.nio.ByteBuffer;

public class OutboundPro {

    private static final byte MAGIC_NUM = 14;
    private MyMessage mmsg;
    ByteBuffer buffer;

    public OutboundPro(MyMessage mmsg) {
        this.mmsg = mmsg;
    }

    public ByteBuffer process() {

        // 헤더 정보 추가
        // 헤더 분석
        // 헤더는 언제나 8byte
        buffer = ByteBuffer.allocate(mmsg.getDataLength() + 8);

        // magic number 처리
        buffer.put((byte) MAGIC_NUM);

        // Type 처리
        // 서버랑 클라가 상이함
        buffer.put((byte) this.mmsg.getType());

        // Resolved 처리
        buffer.put((byte) this.mmsg.getEnd());

        // Future 처리 - 현재 0
        buffer.put((byte) 0);

        // DataLenth 처리
        // 현재 들어온 메세지 길이
        buffer.putInt(this.mmsg.getDataLength());

        // body 추가
        buffer.put(this.mmsg.getBody());

        return buffer;
    }

}
