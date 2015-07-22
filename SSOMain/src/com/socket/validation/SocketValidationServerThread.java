package com.socket.validation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import com.cached.CachedCredentials;

public class SocketValidationServerThread implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(SocketValidationServerThread.class.getName());
	private Socket socket = null;

	public SocketValidationServerThread(Socket socket) {
		this.socket = socket;
		new Thread(this).start();
	}

	@Override
	public void run() {
		DataOutputStream out = null;
		DataInputStream input = null;
		String resultData = null;
		try {
			// 向客户端回复信息
			out = new DataOutputStream(socket.getOutputStream());

			// 读取客户端数据
			input = new DataInputStream(socket.getInputStream());
			// 这里要注意和客户端输出流的写方法对应,否则会抛EOFException
			String data = input.readUTF();

			// 处理客户端数据
			// LOGGER.info("客户端发过来的内容:" + data);
			boolean bool = CachedCredentials.checkUserCredentials(data);
			// 判断凭证信息
			// 向客户端回复信息
			if (bool) {
				resultData = "true";
			} else {
				resultData = "false";
			}
			out.writeUTF(resultData);
			LOGGER.info("返回数据:" + resultData);

		} catch (Exception e) {
			LOGGER.info("服务器 run 异常: " + e.getMessage());// 向客户端回复信息
			resultData = "false";
			try {
				out.writeUTF(resultData);
				LOGGER.info("返回数据:" + resultData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					out = null;
					LOGGER.info("服务端 finally out异常:" + e.getMessage());
				}
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					input = null;
					LOGGER.info("服务端 finally input异常:" + e.getMessage());
				}
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
					socket = null;
					LOGGER.info("服务端 finally 异常:" + e.getMessage());
				}
			}
		}
	}

}
