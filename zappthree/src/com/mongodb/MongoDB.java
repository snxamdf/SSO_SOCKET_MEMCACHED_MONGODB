package com.mongodb;

import com.util.Config;

@SuppressWarnings("deprecation")
public class MongoDB {
	private static Mongo mongo = null;
	private static DB db = null;
	static {
		mongo = new Mongo(Config.MONGO_DB_IP, Config.MONGO_DB_PORT);
		db = mongo.getDB(Config.MONGO_DB_DATABASE);
	}

	public static DB getDB() {
		return db;
	}

	public static DBObject[] getData(String coll, String[][] keyVal) {
		DBCollection collection = MongoDB.getDB().getCollection(coll);
		BasicDBObject document = new BasicDBObject();
		for (String[] array : keyVal) {
			document.put(array[0], array[1]);
		}
		DBCursor cursor = collection.find(document);
		int size = cursor.size();
		if (size == 0) {
			return null;
		}
		DBObject[] result = new DBObject[size];
		int i = 0;
		// 循环输出结果
		while (cursor.hasNext()) {
			result[i] = cursor.next();
		}
		return result;
	}

	public static MongoDataType getData(String coll, String key, String value, String getKey) {
		System.out.println("读取配置:coll=" + coll + " key=" + key + " value=" + value + " getKey=" + getKey);
		DBCollection collection = MongoDB.getDB().getCollection(coll);
		BasicDBObject document = new BasicDBObject();
		document.put(key, value);
		DBCursor cursor = collection.find(document);
		int size = cursor.size();
		if (size == 0) {
			return new MongoDataType();
		}
		while (cursor.hasNext()) {
			return new MongoDataType(cursor.next().get(getKey));
		}
		return new MongoDataType();
	}

	public static Integer toInteger() {

		return 0;
	}
}
