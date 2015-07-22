package com.cached;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.memcached.MemcachedCli;

public class CachedSession {
	private static final Logger LOGGER = Logger.getLogger(CachedSession.class.getName());
	public static String CACHED_KEY_SESSION_ID = "SESSION_ID_";
	public static String CACHED_KEY_SESSION_SITE = "SESSION_SITE_";

	/**
	 * 缓存session 和登录用户信息绑定一起
	 */
	public static void addUserToSession(JSONObject json) {
		synchronized ("") {
			MemcachedCli cli = MemcachedCli.getInstance();
			Object memCachedJsonData = cli.get(CACHED_KEY_SESSION_ID + json.getString("sessionId"));
			JSONObject jsonData = JSONObject.fromObject(memCachedJsonData == null ? "{}" : memCachedJsonData);
			int size = jsonData.size();
			if (size == 0) {
				jsonData.accumulate("site", json.getString("site"));
			} else {
				// 判断当前登录站点
				if (jsonData.getString("site").indexOf(json.getString("site")) == -1) {
					String site = jsonData.getString("site") + "," + json.getString("site");
					jsonData.remove("site");
					jsonData.accumulate("site", site);
				}
			}
			jsonData.put("name", json.getString("name"));
			jsonData.put("passwd", json.getString("passwd"));
			jsonData.put("sessionId", json.getString("sessionId"));
			jsonData.put("lastTime", json.getString("lastTime"));
			cli.set(CACHED_KEY_SESSION_ID + json.getString("sessionId"), jsonData.toString(), new java.util.Date(System.currentTimeMillis() + 60 * 1000 * 120));
			// ------------------------------------------------------------
			Map<String, String> map = new HashMap<String, String>();
			map.put("sessionId", json.getString("sessionId"));
			map.put("siteSessionId", json.getString("siteSessionId"));
			map.put("site", json.getString("site"));
			cli.set(CACHED_KEY_SESSION_SITE + json.getString("siteSessionId"), JSONObject.fromObject(map).toString(), new java.util.Date(System.currentTimeMillis() + 60 * 1000 * 120));
			// ------------------------------------------------------------
		}
	}

	public static String checkUserToSession(HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		String site = request.getParameter("site");
		return checkUser(sessionId, site);
	}

	public static String checkUserToSession(JSONObject request) {
		String sessionId = request.getString("sessionId");
		String site = request.getString("site");
		return checkUser(sessionId, site);
	}

	private static String checkUser(String sessionId, String site) {
		synchronized ("") {
			MemcachedCli cli = MemcachedCli.getInstance();
			Object memCachedJsonData = cli.get(CACHED_KEY_SESSION_ID + sessionId);
			JSONObject jsonData = JSONObject.fromObject(memCachedJsonData == null ? "{}" : memCachedJsonData);
			int size = jsonData.size();
			if (size == 0) {
				jsonData.accumulate("exist", "false");
			} else {
				jsonData.accumulate("exist", "true");
			}
			return jsonData.toString();
		}
	}

	public static void removeUser(String sessionId) {

	}

	public static void destroyedSession(String siteSessionId) {
		synchronized ("") {
			// 获取siteSession
			MemcachedCli cli = MemcachedCli.getInstance();
			Object memCachedJsonData = cli.get(CACHED_KEY_SESSION_SITE + siteSessionId);
			JSONObject jsonData = JSONObject.fromObject(memCachedJsonData == null ? "{}" : memCachedJsonData);
			int size = jsonData.size();
			if (size == 0) {
				return;
			}
			String chchedSite = jsonData.getString("site");
			// 获取SESSION_ID
			memCachedJsonData = cli.get(CACHED_KEY_SESSION_ID + jsonData.getString("sessionId"));
			jsonData = JSONObject.fromObject(memCachedJsonData == null ? "{}" : memCachedJsonData);
			size = jsonData.size();
			if (size == 0) {
				return;
			}
			String[] sites = jsonData.getString("site").split(",");
			StringBuffer siteSb = new StringBuffer();
			// 清除SESSION_ID里面的site
			for (String site : sites) {
				if (!chchedSite.equals(site)) {
					siteSb.append(site).append(",");
				}
			}
			if (siteSb.length() > 0) {
				siteSb.delete(siteSb.length() - 1, siteSb.length());
			}
			if (siteSb.length() != 0) {
				LOGGER.info("删除 SESSION_ID site=" + chchedSite);
				jsonData.put("site", siteSb.toString());
				updateUserToSession(jsonData);
				cli.delete(CACHED_KEY_SESSION_SITE + siteSessionId);
			} else {
				LOGGER.info("清除 CACHED_KEY_SESSION_ID and CACHED_KEY_SESSION_SITE " + jsonData.getString("sessionId") + "  " + siteSessionId);
				cli.delete(CACHED_KEY_SESSION_ID + jsonData.getString("sessionId"));
				cli.delete(CACHED_KEY_SESSION_SITE + siteSessionId);
			}

		}
	}

	public static void updateUserToSession(JSONObject json) {
		synchronized ("") {
			MemcachedCli cli = MemcachedCli.getInstance();
			cli.set(CACHED_KEY_SESSION_ID + json.getString("sessionId"), json.toString(), new java.util.Date(System.currentTimeMillis() + 60 * 1000 * 120));
		}
	}
}
