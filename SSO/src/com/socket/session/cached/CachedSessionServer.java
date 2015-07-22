package com.socket.session.cached;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.util.Config;

/**
 * 缓存 用户session 10003
 * 
 */
public class CachedSessionServer {
	private static final Logger LOGGER = Logger.getLogger(CachedSessionServer.class.getName());

	public void listenerSocket() {
		LOGGER.info("Socket 缓存SESSION服务在启动...");
		Thread socketStart = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocket serverSocket = null;
				try {
					serverSocket = new ServerSocket(Config.CACHED_SESSION_PORT);
					while (true) {
						Socket socket = serverSocket.accept();
						new CachedSessionServerThread(socket);
					}
				} catch (Exception e) {
					LOGGER.info("can not SocketServerThread to:" + e);
				}
			}
		});
		socketStart.start();
		LOGGER.info("Socket 缓存SESSION服务启动完成");
	}
}
