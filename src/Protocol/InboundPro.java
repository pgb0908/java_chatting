package Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class InboundPro {

    private static final byte MAGIC_NUM = 14;
    private static final int INT_SIZE = 4;
    private ByteBuffer buffer;

    MyMessage myMsg = new MyMessage();

    public InboundPro(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public MyMessage prcess() {

        System.out.println("inbound process start");
        // ���� �޽����� �м�
        // ��� �м�
        // ����� ������ 8byte
        int offset = 0;

        // magic number ó��
        byte mn = this.buffer.get(offset);
        offset++;

        if (mn != MAGIC_NUM) {
            // ����
            myMsg.setErr(true);
            System.out.println("[InboudPro] ���� �߻�");
        }

        // Type ó��
        byte type = this.buffer.get(offset);
        offset++;

        // todo casting �Ұ�...
        myMsg.setType((int) type);

        // Resolved ó��
        byte resolve = this.buffer.get(offset);
        offset++;

        myMsg.setEnd((int) resolve);

        // Future ó�� - ���� 0
        byte f = this.buffer.get(offset);
        offset++;

        // DataLenth ó��
        int dl = this.buffer.getInt(offset);
        myMsg.setDataLength(dl);
        offset = offset + INT_SIZE;

        if (dl > 0) {

            // System.out.println(dl);
            byte[] body = new byte[dl];
            // System.arraycopy(buffer, offset, body, 0, dl);
            int idx = 0;
            while (dl > 0) {
                body[idx] = this.buffer.get(offset);
                idx++;
                offset++;
                dl--;
            }
            myMsg.setBody(body);
        }

        return myMsg;
    }

}
