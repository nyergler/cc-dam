//////////////////////////////////////////////////////////////////////
//                                                                  //
//  UpdateMessage Class                                             //
//                                                                  //
//////////////////////////////////////////////////////////////////////

package org.cc;

public class UpdateMessage {
    public static final int LISTING = 0;
    public static final int QUERY = 1;
    public static final int CLEAR = 2;
    
    private int msgType;
    private Object payload;
    
    public UpdateMessage(int type, Object data) {
        this.msgType = type;
        this.payload = data;
    }
    
    public int getType() {
        return this.msgType;
    }
    
    public Object getPayload() {
        return this.payload;
    }
}