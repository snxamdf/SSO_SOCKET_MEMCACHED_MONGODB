package com.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.sf.json.JSONObject;

import com.util.Config;
import com.util.socket.SocketUtil;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent session) {
		System.out.println("SESSION 创建:" + session.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent session) {
		System.out.println("SESSION 消毁:" + session.getSession().getId());
		Map<String, String> map = new HashMap<String, String>();
		map.put("siteSessionId", session.getSession().getId());//站点Session
		map.put("SEND_TYPE", Config.DESTROYED_SESSION);
		String jsonDataStr = SocketUtil.sendData(JSONObject.fromObject(map).toString(), Config.CACHED_SESSION_IP, Config.CACHED_SESSION_PORT);
		System.out.println(jsonDataStr);
	}
}
