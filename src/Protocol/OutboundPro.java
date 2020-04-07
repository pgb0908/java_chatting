package Protocol;

public class OutboundPro {

    public boolean outBound() {
        
        //헤더 제작
        //헤더는 언제나 8byte
        
        
        
        int offset = 0;
        
        //magic number 처리
        byte mn = buffer.get(offset);
        offset++;
        
        if(mn != MAGIC_NUM) {
            return false;
        }
        
        //Type 처리
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
        
        //Resolved 처리
        byte r = buffer.get(offset);
        offset++;
        
        if(r == (byte) 0) {
            //데이터 나눠짐
        }
        else if(r == (byte) 0) {
            //마지막 메세지 의미
        }
        else {
            return false;
        }
        
        // Future 처리 - 현재 0
        byte f = buffer.get(offset);
        offset++;
        
        // DataLenth 처리
        int dl = buffer.getInt(offset);
        offset = offset + 4;
        
        byte [] body = new byte[dl];
        


        return true;
    }
    
}
