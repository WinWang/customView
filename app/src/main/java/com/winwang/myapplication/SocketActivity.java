package com.winwang.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jds.im.proto.MessageProto;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.util.HashMap;
import java.util.UUID;

public class SocketActivity extends AppCompatActivity {

    private IConnectionManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        initData();

    }

    private void initData() {
        ConnectionInfo info = new ConnectionInfo("192.168.55.174", 6666);
        mManager = OkSocket.open(info);
        OkSocketOptions option = mManager.getOption();
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(option);
        mManager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                super.onSocketConnectionSuccess(info, action);
                System.out.println("连接成功》》》》》》》》》》》》》》》》");
                HashMap<String, String> map = new HashMap<>();
                map.put("channel", "test");
                map.put("device", "ios");
                MessageProto.MsgProtocol message = MessageProto.MsgProtocol.newBuilder()
                        .setMsgId(UUID.randomUUID().toString())
                        .setUserId("we")
                        .putAllHeaders(map)
                        .setType(MessageProto.MsgProtocol.Type.CONNECT)
                        .build();
                AcknowledgeBean acknowledgeBean = new AcknowledgeBean();
                acknowledgeBean.setAuthData(message);
                OkSocket.open(info).send(acknowledgeBean);
            }

            @Override
            public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
                super.onSocketConnectionFailed(info, action, e);
                System.out.println("连接失败》》》》》》》》》》》》》》》》" + e.toString());
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                try {
                    MessageProto.MsgProtocol msgProtocol = MessageProto.MsgProtocol.parseFrom(data.getBodyBytes());
                    System.out.println("msgProtocol信息>>>>>>" + msgProtocol.toString());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
                super.onSocketWriteResponse(info, action, data);
                System.out.println("写入信息结果反馈啊》》》》》" + action);
//                MessageProto.MsgProtocol message = MessageProto.MsgProtocol.newBuilder()
//                        .setMsgId(UUID.randomUUID().toString())
//                        .putHeaders("group_id", "1")
//                        .putHeaders(MSG_TYPE, String.valueOf(MsgTypeEnum.GROUP_MSG.value()))
//                        .setType(MessageProto.MsgProtocol.Type.PUBLISH)
//                        .setPayload(ByteString.copyFromUtf8(line))
//                        .build();
            }


            @Override
            public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
                super.onPulseSend(info, data);
            }
        });
        mManager.connect();
    }
}
