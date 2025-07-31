package com.AnJiahao.qqclient.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *实现消息相关方法
 * 群发、私聊
 */


public class MessageClientService {

    /**
     *
     * @param content 消息
     * @param senderId 发送者
     * @param getterId 接收者
     */
    public void sendMessageToOne(String content,String senderId,String getterId){

        //构建Message对象
        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_COMM_MES);
        ms.setContent(content);
        ms.setSender(senderId);
        ms.setGetter(getterId);
        ms.setSendTime(new java.util.Date().toString());

        System.out.println(senderId+" 给"+getterId+" 发送了消息:"+content);

        //将消息上传到服务端(输出流)
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClinetConnectServer(senderId).getSocket().getOutputStream());
            oos.writeObject(ms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *消息群发
     * @param content 群发内容
     * @param sender 发送者
     */

    public void sendMessageToAll(String content,String sender){

        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_COMM_ALL_MES);
        ms.setContent(content);
        ms.setSender(sender);

        //发送数据到服务端，输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClinetConnectServer(sender).getSocket().getOutputStream());
            oos.writeObject(ms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
