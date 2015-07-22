package com.util;

import com.mongodb.MongoDB;

/**
 * Socket
 */
public class Config {
	public static final String MONGO_DB_IP = "localhost";
	public static final int MONGO_DB_PORT = 27017;
	public static final String MONGO_DB_DATABASE = "SSO_DB";
	public static final String VERIFY_USER_IP = MongoDB.getData("config_tab", "TYPE", "VERIFY_USER_IP", "IP").toString();
	public static final int VERIFY_USER_PORT = MongoDB.getData("config_tab", "TYPE", "VERIFY_USER_PORT", "PORT").toInteger();

	public static final String FREE_URL_IP = MongoDB.getData("config_tab", "TYPE", "FREE_URL_IP", "IP").toString();
	public static final int FREE_URL_PORT = MongoDB.getData("config_tab", "TYPE", "FREE_URL_PORT", "PORT").toInteger();

	public static final int CACHED_SESSION_PORT = MongoDB.getData("config_tab", "TYPE", "CACHED_SESSION_PORT", "PORT").toInteger();

	public static final String CACHED_SESSION = "CACHEDSESSION";
	public static final String CHECK_SESSION = "CHECKSESSION";
	public static final String DESTROYED_SESSION = "DESTROYEDSESSION";
}
