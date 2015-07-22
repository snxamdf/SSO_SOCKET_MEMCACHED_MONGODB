package com.util;

import com.mongodb.MongoDB;

/**
 * Socket
 */
public class Config {

	public static final String MONGO_DB_IP = "localhost";
	public static final int MONGO_DB_PORT = 27017;
	public static final String MONGO_DB_DATABASE = "SSO_DB";
	/**
	 * 验证凭证
	 */
	public static final int FREE_VALIDATION_PORT = MongoDB.getData("config_tab", "TYPE", "FREE_VALIDATION_PORT", "PORT").toInteger();
	public static final String FREE_VALIDATION_IP = MongoDB.getData("config_tab", "TYPE", "FREE_VALIDATION_IP", "IP").toString();

	/**
	 * 缓存session
	 */
	public static final int CACHED_SESSION_PORT = MongoDB.getData("config_tab", "TYPE", "CACHED_SESSION_PORT", "PORT").toInteger();
	public static final String CACHED_SESSION_IP = MongoDB.getData("config_tab", "TYPE", "CACHED_SESSION_IP", "IP").toString();

	public static final String CACHED_SESSION = "CACHEDSESSION";
	public static final String CHECK_SESSION = "CHECKSESSION";
	public static final String DESTROYED_SESSION = "DESTROYEDSESSION";
}
