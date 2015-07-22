package com.cached;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoDB;
import com.util.MD5.MD5Util;

public class CheckUser {
	private static Map<String, Map<String, String>> cached = new ConcurrentHashMap<String, Map<String, String>>();
	static {
		Map<String, String> user1 = new HashMap<String, String>();
		user1.put("name", "zhangsan");
		user1.put("passwd", MD5Util.MD5Passwd("111111"));
		cached.put(user1.get("name"), user1);
		Map<String, String> user2 = new HashMap<String, String>();
		user2.put("name", "lisi");
		user2.put("passwd", MD5Util.MD5Passwd("111111"));
		cached.put(user2.get("name"), user2);
	}

	public static String verifyUser(String name, String passwd) {
		DBCollection collection = MongoDB.getDB().getCollection("users");
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", name);
		DBCursor cursor = collection.find(searchQuery);
		int size = cursor.size();
		if (size == 0) {
			return "000002";// 用户不存在
		} else {
			String uname = null, upasswd = null;
			while (cursor.hasNext()) {
				DBObject dbo = cursor.next();
				uname = dbo.get("name").toString();
				upasswd = dbo.get("passwd").toString();
			}
			if (name.equals(uname) && passwd.equals(upasswd)) {
				return "000000";// 用户存在验证正确
			} else {
				return "000001";// 用户名或密码不正确
			}
		}
	}
}
