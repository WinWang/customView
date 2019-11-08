package com.winwang.myapplication;

import com.jds.im.proto.MessageProto;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by WinWang on 2019/9/2
 * Description->
 */
public class AcknowledgeBean implements ISendable {

    MessageProto.MsgProtocol authData;

    public void setAuthData(MessageProto.MsgProtocol authData) {
        this.authData = authData;
    }

    @Override
    public byte[] parse() {
        byte[] bytes = authData.toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(4 + bytes.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(bytes.length);
        bb.put(bytes);
        return bb.array();
    }
}
