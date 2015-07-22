package com.session;

import javax.servlet.http.HttpSession;

public class LoginSession {

	public static void login(String userId, HttpSession session) {
		session.setAttribute("USERS_SESSION", userId);
	}

	/**
	 * 判断是否登录
	 * 1 判断session
	 * 2 判断SSO登录服务器缓存
	 */
	public static boolean isLogin(String name, String site, HttpSession session) {
		boolean bool = false;
		bool = session.getAttribute("USERS_SESSION") != null ? true : false;
//		if (bool) {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("name", name);
//			map.put("site", site);
//			map.put("SEND_TYPE", "CHECKSESSION");
//			map.put("sessionId", session.getId());
//			String data = SocketUtil.sendData(JSONObject.fromObject(map).toString(), Config.CACHED_SESSION_IP, Config.CACHED_SESSION_PORT);
//			String boolStr = JSONObject.fromObject(data).getString("success");
//			if (boolStr.equals("true")) {
//				bool = true;
//			} else {
//				bool = false;
//			}
//		}
		return bool;
	}

	/**
	 * 退出调用的方法
	 */
	public static void logout(HttpSession session) {
		session.removeAttribute("USERS_SESSION");
	}
}
