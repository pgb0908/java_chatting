package Protocol;


public class MyMessage {
    
    private boolean err;
    private myType type;
    private int resolve;
    private int end;
    private int f;
    private int dl;
    private byte [] body;
    
    public MyMessage() {
        this.err = false;
        this.end = 0;
        this.f = 0;
        this.dl = 0;
    }
    

    public void setErr(boolean b) {
        this.err = b;
        
    }

    public void setType(int i) {
        if(i == 0) {
            this.type = myType.REGIST;
        }
        else if(i == 1) {
            this.type = myType.UNREGIST;
        }
        if(i == 2) {
            this.type = myType.BROADCAST;
        }
        
    }

    public void setEnd(int i) {
        this.end = i;
        
    }

    public void setDataLength(int dl) {
        this.dl = dl;
        
    }

    public void setBody(byte[] body) {
        this.body = body;
        
    }

    
    //get
    public boolean getErr() {
        return this.err;
        
    }

    public myType getType() {
        return this.type;
        
    }

    public int getEnd() {
        return this.end;
        
    }

    public int getDataLength() {
        return this.dl;
    }

    public byte[] getBody() {
        return this.body;
        
    }



}
