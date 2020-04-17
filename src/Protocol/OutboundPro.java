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

        // ��� ���� �߰�
        // ��� �м�
        // ����� ������ 8byte
        buffer = ByteBuffer.allocate(mmsg.getDataLength() + 8);

        // magic number ó��
        buffer.put((byte) MAGIC_NUM);

        // Type ó��
        // ������ Ŭ�� ������
        buffer.put((byte) this.mmsg.getType());

        // Resolved ó��
        buffer.put((byte) this.mmsg.getEnd());

        // Future ó�� - ���� 0
        buffer.put((byte) 0);

        // DataLenth ó��
        // ���� ���� �޼��� ����
        buffer.putInt(this.mmsg.getDataLength());

        // body �߰�
        buffer.put(this.mmsg.getBody());

        return buffer;
    }

}
