package com.util;

import com.mongodb.MongoDB;

public class Config {
	public static final String MONGO_DB_IP = "localhost";
	public static final int MONGO_DB_PORT = 27017;
	public static final String MONGO_DB_DATABASE = "SSO_DB";
	/**
	 * 验证用户
	 */
	public static final int VERIFY_USER_PORT = MongoDB.getData("config_tab", "TYPE", "VERIFY_USER_PORT", "PORT").toInteger();

	/**
	 * 免登url
	 */
	public static final int FREE_URL_PORT = MongoDB.getData("config_tab", "TYPE", "FREE_URL_PORT", "PORT").toInteger();

	/**
	 * 验证服务端口号
	 */
	public static final int FREE_VALIDATION_PORT = MongoDB.getData("config_tab", "TYPE", "FREE_VALIDATION_PORT", "PORT").toInteger();;

}
