package com.AnJiahao.common;

public interface MessageType {

    String MESSAGE_SUCCEED = "1";//登录成功
    String MESSAGE_FAIL = "2";//登录失败
    String MESSAGE_COMM_MES = "3";//普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4";//要求返回在线列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";//客户端请求退出
    String MESSAGE_COMM_ALL_MES = "7";
    String MESSAGE_COMM_FILE_MES = "8";//发送文件



}
