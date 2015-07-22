package com.mongodb;

import java.net.UnknownHostException;

import com.util.MD5.MD5Util;

public class MongoMain {

	public static void main(String[] args) throws UnknownHostException {
		try {
			DBCollection collection = MongoDB.getDB().getCollection("users");
			// 创建要查询的document
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("name", "zhangsan");
			// 使用collection的find方法查找document
			DBCursor cursor = collection.find(searchQuery);
			int size = cursor.size();
			if (size == 0) {
				// 使用BasicDBObject对象创建一个mongodb的document,并给予赋值。
				BasicDBObject document = new BasicDBObject();
				document.put("name", "zhangsan");
				document.put("heSaid", "张三");
				document.put("passwd", MD5Util.MD5Passwd("111111"));
				// 将新建立的document保存到collection中去
				collection.insert(document);
			} else {
				// 循环输出结果
				while (cursor.hasNext()) {
					System.out.println(cursor.next());
				}
			}
		} catch (MongoException e) {
			e.printStackTrace();
		}
		String[][] keyVal = new String[1][2];
		keyVal[0] = new String[] { "TYPE", "VERIFY_USER_PORT" };
		DBObject[] objs = MongoDB.getData("config_tab", keyVal);
		System.out.println(objs);
		MongoDB.getData("config_tab", "TYPE", "VERIFY_USER_PORT", "PORT").toInteger();
	}

	public static void addConfig() {
		DBCollection collection = MongoDB.getDB().getCollection("config_tab");
		collection.drop();
		BasicDBObject document = new BasicDBObject();
		document.put("PORT", "10000");
		document.put("IP", "127.0.0.1");
		document.put("DESC", "验证用户");
		document.put("TYPE", "VERIFY_USER_PORT");
		collection.save(document);
		document = new BasicDBObject();
		document.put("PORT", "10001");
		document.put("IP", "127.0.0.1");
		document.put("DESC", "免登url");
		document.put("TYPE", "FREE_URL_PORT");
		collection.save(document);
		document = new BasicDBObject();
		document.put("PORT", "10002");
		document.put("IP", "127.0.0.1");
		document.put("DESC", "验证服务端口号");
		document.put("TYPE", "FREE_VALIDATION_PORT");
		collection.save(document);
		document = new BasicDBObject();
		document.put("PORT", "10003");
		document.put("IP", "127.0.0.1");
		document.put("DESC", "缓存用户session");
		document.put("TYPE", "CACHED_SESSION_PORT");
		collection.save(document);

		document = new BasicDBObject();
		document.put("PORT", "11211");
		document.put("weight", "1");
		document.put("IP", "localhost");
		document.put("DESC", "内存缓存");
		document.put("TYPE", "MEMCACHED");
		collection.save(document);
		
		//SSO_SOCKET_MEMCACHED_MONGODB

	}
}
