package com.util.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketUtil {
	private static final Logger LOGGER = Logger.getLogger(SocketUtil.class.getName());

	/**
	 * 发送验证请求
	 */
	public static String sendData(String data, String IP, int port) {
		Socket socket = null;
		String result = null;
		DataOutputStream out = null;
		DataInputStream input = null;
		try {
			//创建一个流套接字并将其连接到指定主机上的指定端口号  
			socket = new Socket(IP, port);

			//向服务器端发送数据    
			out = new DataOutputStream(socket.getOutputStream());

			//读取服务器端数据    
			input = new DataInputStream(socket.getInputStream());

			out.writeUTF(data);

			result = input.readUTF();
			LOGGER.info("服务器端返回过来的是: " + result);
		} catch (Exception e) {
			LOGGER.info("客户端异常:" + e.getMessage());
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					out = null;
					LOGGER.info("客户端 finally out 异常:" + e.getMessage());
				}
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					input = null;
					LOGGER.info("客户端 finally input异常:" + e.getMessage());
				}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					LOGGER.info("客户端 finally 异常:" + e.getMessage());
				}
			}
		}
		return result;
	}

}
