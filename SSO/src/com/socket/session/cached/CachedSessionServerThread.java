package com.socket.session.cached;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import com.cached.CachedSession;
import com.util.Config;

import net.sf.json.JSONObject;

public class CachedSessionServerThread implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(CachedSessionServerThread.class.getName());
	private Socket socket = null;

	public CachedSessionServerThread(Socket socket) {
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

			// 解析数据
			JSONObject json = JSONObject.fromObject(data);
			String SEND_TYPE = json.getString("SEND_TYPE");
			if (Config.CACHED_SESSION.equals(SEND_TYPE)) {// 缓存session
				CachedSession.addUserToSession(json);
				resultData = "{\"success\":\"ok\"}";
			} else if (Config.CHECK_SESSION.equals(SEND_TYPE)) {// 检查session
				String jsonDataStr = CachedSession.checkUserToSession(json);
				JSONObject jsonData = JSONObject.fromObject(jsonDataStr);
				String exist = jsonData.getString("exist");
				if ("true".equals(exist)) {
					resultData = "{\"success\":\"true\"}";
				} else if ("false".equals(exist)) {
					resultData = "{\"success\":\"false\"}";
				}
			} else if (Config.DESTROYED_SESSION.equals(SEND_TYPE)) {// session销毁执行
				CachedSession.destroyedSession(json.getString("siteSessionId"));
				resultData = "{\"success\":\"true\"}";
			}
			// 处理客户端数据
			// LOGGER.info("客户端发过来的内容:" + data);
			// 解析数据
			// 向客户端回复信息
			out.writeUTF(resultData);
			LOGGER.info("返回数据:" + resultData);

		} catch (Exception e) {
			LOGGER.info("服务器 run 异常: " + e.getMessage());// 向客户端回复信息
			resultData = "{\"success\":\"error\",\"code\":\"" + e.getMessage() + "\"}";
			try {
				out.writeUTF(resultData);
				LOGGER.info("验证数据:" + resultData);
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
