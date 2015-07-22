package com.cached;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

public class CachedCredentials {
	private static Map<String, Map<String, String>> cachedCredentials = new ConcurrentHashMap<String, Map<String, String>>();

	/**
	 * 添加toKen md5 信任凭证 和登录用户信息绑定一起
	 */
	public static void addUserCredentials(String name, String passwd, String toKen, String md5) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("passwd", passwd);
		map.put("token", toKen);
		map.put("md5", md5);
		map.put("state", "-1");
		cachedCredentials.put(name, map);
	}

	/**
	 * 检查 信任凭证
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkUserCredentials(String jsonData) {
		JSONObject json = JSONObject.fromObject(jsonData);
		Map<String, String> map = cachedCredentials.get(json.getString("name"));
		if (map == null) {
			return false;
		}
		String token = map.get("token");
		if (token.equals(json.getString("token"))) {
			cachedCredentials.remove(json.getString("name"));
			return true;
		}
		return false;
	}
}
