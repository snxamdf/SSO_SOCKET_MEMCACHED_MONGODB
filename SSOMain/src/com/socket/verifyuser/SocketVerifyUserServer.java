package com.socket.verifyuser;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.util.Config;

/**
 * 启动SocketVerifyUserServer 用户验证服务
 * 10000
 */
public class SocketVerifyUserServer {
	private static final Logger LOGGER = Logger.getLogger(SocketVerifyUserServer.class.getName());

	public void listenerSocket() {
		LOGGER.info("Socket 用户验证服务正在启动...");
		Thread socketStart = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(Config.VERIFY_USER_PORT);
					while (true) {
						Socket socket = serverSocket.accept();
						new SocketVerifyUserServerThread(socket);
					}
				} catch (Exception e) {
					LOGGER.info("can not SocketServerThread to:" + e);
				}
			}
		});
		socketStart.start();
		LOGGER.info("Socket 用户验证服务启动完成");
	}
}
