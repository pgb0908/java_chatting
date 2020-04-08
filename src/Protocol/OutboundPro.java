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
        //��� ���� �߰�
        //��� �м�
        //����� ������ 8byte

        //magic number ó��
        buffer.put((byte) MAGIC_NUM);

        //Type ó��
        //������ Ŭ�� ������
        buffer.put((byte) 2);

        //Resolved ó��
        buffer.put((byte) 1);
        
        // Future ó�� - ���� 0
        buffer.put((byte) 0);

        // DataLenth ó��
        buffer.putInt(this.mmsg.getDataLength());
        
        //body �߰�
        buffer.put(this.mmsg.getBody());

        return buffer;
    }
    
    public ByteBuffer clientProcess() {
        
        //��� ���� �߰�
        //��� �м�
        //����� ������ 8byte
        buffer = ByteBuffer.allocate(mmsg.getDataLength()+8);

        //magic number ó��
        buffer.put((byte) MAGIC_NUM);

        //Type ó��
        //������ Ŭ�� ������
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
        
        //Resolved ó��
        switch(this.mmsg.getEnd()) {
            case 0:
                buffer.put((byte) 0);
                break;
            case 1:
                buffer.put((byte) 1);
                break;
        }
        
        // Future ó�� - ���� 0
        buffer.put((byte) 0);

        // DataLenth ó��
        // ���� ���� �޼��� ����
        buffer.putInt(this.mmsg.getDataLength());
        
        //body �߰�
        buffer.put(this.mmsg.getBody());

        return buffer;
    }


    
}
