package Protocol;

public class OutboundPro {

    public boolean outBound() {
        
        //��� ����
        //����� ������ 8byte
        
        
        
        int offset = 0;
        
        //magic number ó��
        byte mn = buffer.get(offset);
        offset++;
        
        if(mn != MAGIC_NUM) {
            return false;
        }
        
        //Type ó��
        byte type = buffer.get(offset);
        offset++;
        
        if(type == (byte) 0) {
            //Regist
        }
        else if(type == (byte) 0) {
            //unRegist
        }
        else if(type == (byte) 0) {
            //broadcast
        }
        else {
            return false;
        }
        
        //Resolved ó��
        byte r = buffer.get(offset);
        offset++;
        
        if(r == (byte) 0) {
            //������ ������
        }
        else if(r == (byte) 0) {
            //������ �޼��� �ǹ�
        }
        else {
            return false;
        }
        
        // Future ó�� - ���� 0
        byte f = buffer.get(offset);
        offset++;
        
        // DataLenth ó��
        int dl = buffer.getInt(offset);
        offset = offset + 4;
        
        byte [] body = new byte[dl];
        


        return true;
    }
    
}
