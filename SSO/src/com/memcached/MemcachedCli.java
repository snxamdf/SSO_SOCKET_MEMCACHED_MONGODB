package com.memcached;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.danga.MemCached.MemCachedClient;

public class MemcachedCli {
	private static MemcachedCli unique = new MemcachedCli();

	public MemcachedCli() {
		init();
	}

	public static MemcachedCli getInstance() {
		return unique;
	}

	private MemCachedClient client = new MemCachedClient();

	private void init() {
		client.setPrimitiveAsString(true);
		// client.setCompressEnable(true);
		// client.setCompressThreshold(4 * 1024);
	}

	public boolean set(String key, Object value) {
		return client.set(key, value);
	}

	public boolean set(String key, Object value, Date expired) {
		return client.set(key, value, expired);
	}

	public Object get(String key) {
		return client.get(key);
	}

	public void delete(String key) {
		client.delete(key);
	}

	public Map<String, Map<String, String>> stats() {
		return client.stats();
	}

	public Map<String, Map<String, String>> statsCacheDump(int slabNumber, int limit) {

		return client.statsCacheDump(slabNumber, limit);
	}

	public void printStat() {
		Map<?, ?> stats = client.stats();
		Set<?> keys = stats.keySet();
		Iterator<?> keyIter = keys.iterator();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			Object value = stats.get(key);
			System.out.println(key + "=" + value);
		}
	}
}
