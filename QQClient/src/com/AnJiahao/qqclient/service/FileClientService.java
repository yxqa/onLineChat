package com.AnJiahao.qqclient.service;

import com.AnJiahao.common.Message;
import com.AnJiahao.common.MessageType;

import java.io.*;

/**
 * 文件传输类
 * 完成文件传输服务
 */


public class FileClientService {

    /**
     *
     * @param sender 发送者
     * @param getter 接收者
     * @param src 源地址
     * @param dest 接受地址
     */
    public void sendFileToOne(String sender, String getter, String src, String dest){

        //1. 将需要发送的封装为一个Message对象
        Message ms = new Message();
        ms.setSender(sender);
        ms.setGetter(getter);
        ms.setSrc(src);
        ms.setDest(dest);
        ms.setMesType(MessageType.MESSAGE_COMM_FILE_MES);

        //2.读取文件->创建输入流
        FileInputStream fileInputStream = null;
        //创建字节数组
        byte[] fileByte = new byte[(int)new File(src).length()];
        //创建输入流
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileByte);//将src下的文件读到fileByte中
            ms.setFileBytes(fileByte);//封装到Message对象中
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //关闭
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //提示信息
        System.out.println(sender + " 向 " + getter + "发送文件："+ src + "到对方目录" + dest +"下");

        //发送，输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.
                    getClinetConnectServer(sender).getSocket().getOutputStream());
            oos.writeObject(ms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
