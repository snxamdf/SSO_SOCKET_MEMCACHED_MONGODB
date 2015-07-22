package com.socket.validation;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.util.Config;
/**
 * 验证凭证服务正 10002
 *
 */
public class SocketValidationServer {
	private static final Logger LOGGER = Logger.getLogger(SocketValidationServer.class.getName());

	public void listenerSocket() {
		LOGGER.info("Socket 验证凭证服务正在启动...");
		Thread socketStart = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(Config.FREE_VALIDATION_PORT);
					while (true) {
						Socket socket = serverSocket.accept();
						new SocketValidationServerThread(socket);
					}
				} catch (Exception e) {
					LOGGER.info("can not SocketServerThread to:" + e);
				}
			}
		});
		socketStart.start();
		LOGGER.info("Socket 验证凭证服务启动完成");
	}
}
