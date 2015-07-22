package com.socket.verifyuser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import com.cached.CachedCredentials;
import com.cached.CheckUser;
import com.util.DataParsing;
import com.util.ToKen;
import com.util.MD5.MD5Util;

/**
 * SocketVerifyUserServerThread 用户验证线程
 */
public class SocketVerifyUserServerThread implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(SocketVerifyUserServerThread.class.getName());
	private Socket socket = null;

	public SocketVerifyUserServerThread(Socket socket) {
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
			// 解析数据
			JSONObject json = DataParsing.strToJson(data);
			// 判断用户信息
			String code = CheckUser.verifyUser(json.getString("name"), json.getString("passwd"));
			String url = this.generate(json);
			// 向客户端回复信息
			resultData = "{\"success\":\"ok\",\"code\":\"" + code + "\",\"url\":\"" + url + "\"}";
			out.writeUTF(resultData);
			LOGGER.info("返回数据:" + resultData);

		} catch (Exception e) {
			LOGGER.info("服务器 run 异常: " + e.getMessage());// 向客户端回复信息
			resultData = "{\"success\":\"error\",\"code\":\"" + e.getMessage() + "\",\"url\":\"\"}";
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

	private String generate(JSONObject json) {
		// 解析数据
		String name = json.getString("name"), passwd = json.getString("passwd");
		String serverUrl = json.getString("serverUrl"), md5 = json.getString("md5"), toKen = ToKen.generate();
		String loginUrl = json.getString("loginUrl");
		String paramter = "&login=login&name=" + name + "&passwd=" + passwd;
		String url = null;
		boolean bool = MD5Util.validation(paramter, md5);
		if (bool) {
			CachedCredentials.addUserCredentials(name, passwd, toKen, md5);
			paramter += "&token=" + toKen + "&md5=" + md5 ;
			url = loginUrl + "?serverUrl=" + serverUrl + paramter;
		}
		return url;
	}
}
