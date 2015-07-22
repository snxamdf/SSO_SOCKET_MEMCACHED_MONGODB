package com.main;

import com.socket.freeurl.SocketFreeUrlServer;
import com.socket.validation.SocketValidationServer;
import com.socket.verifyuser.SocketVerifyUserServer;

public class MainStart {
	public static void main(String[] args) {
		// 启动SocketVerifyUserServer 用户验证服务
		SocketVerifyUserServer svus = new SocketVerifyUserServer();
		svus.listenerSocket();
		// 启动SocketFreeUrlServer 免登URL服务
		SocketFreeUrlServer sfus = new SocketFreeUrlServer();
		sfus.listenerSocket();
		// 启动SocketValidationServer 验证凭证服务正
		SocketValidationServer svs = new SocketValidationServer();
		svs.listenerSocket();
	}
}
