package com.socket.freeurl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.util.Config;

/**
 * 启动SocketFreeUrlServer 免登URL服务 
 * 10001
 */
public class SocketFreeUrlServer {
	private static final Logger LOGGER = Logger.getLogger(SocketFreeUrlServer.class.getName());

	public void listenerSocket() {
		LOGGER.info("Socket 免登URL服务正在启动...");
		Thread socketStart = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(Config.FREE_URL_PORT);
					while (true) {
						Socket socket = serverSocket.accept();
						new SocketFreeUrlServerThread(socket);
					}
				} catch (Exception e) {
					LOGGER.info("can not SocketServerThread to:" + e);
				}
			}
		});
		socketStart.start();
		LOGGER.info("Socket 免登URL服务启动完成");
	}
}
