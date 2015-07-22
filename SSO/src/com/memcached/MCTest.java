package com.memcached;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MCTest {
	public static void main(String[] args) {
		try {
			MemcachedServer server = new MemcachedServer("localhost", 11211, 1);
			List<MemcachedServer> servers = new ArrayList<MemcachedServer>();
			servers.add(server);
			MemcachedPool pool = MemcachedPool.getInstance();
			pool.initPool(servers);
			MemcachedCli client = MemcachedCli.getInstance();
			// client.set("testKey", "testValue");
			// System.out.println(client.get("testKey"));
			// client.delete("testKey");
			// System.out.println(client.get("testKey"));
			// String value = (String) client.get("test1");
			// System.out.println("value=" + value);
			// client.set("test1", "value1");
			// value = (String) client.get("test1");
			// System.out.println("value=" + value);
			// client.printStat();
			getAllKeys(client);
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getAllKeys(MemcachedCli memCachedClient) {
		List<String> list = new ArrayList<String>();
		Map<String, Map<String, String>> items = memCachedClient.stats();
		for (Iterator<String> itemIt = items.keySet().iterator(); itemIt.hasNext();) {
			String itemKey = itemIt.next();
			Map<String, String> maps = items.get(itemKey);
			for (Iterator<String> mapsIt = maps.keySet().iterator(); mapsIt.hasNext();) {
				String mapsKey = mapsIt.next();
				String mapsValue = maps.get(mapsKey);
				System.out.println("mapsKey=" + mapsKey + "  mapsValue=" + mapsValue);
				if (mapsKey.endsWith("number")) { // memcached key 类型
													// item_str:integer:number_str
					String[] arr = mapsKey.split(":");
					int slabNumber = Integer.valueOf(arr[1].trim());
					int limit = Integer.valueOf(mapsValue.trim());
					Map<String, Map<String, String>> dumpMaps = memCachedClient.statsCacheDump(slabNumber, limit);
					for (Iterator<String> dumpIt = dumpMaps.keySet().iterator(); dumpIt.hasNext();) {
						String dumpKey = dumpIt.next();
						Map<String, String> allMap = dumpMaps.get(dumpKey);
						for (Iterator<String> allIt = allMap.keySet().iterator(); allIt.hasNext();) {
							String allKey = allIt.next();
							list.add(allKey.trim());

						}
					}
				}
			}
		}
		return list;
	}
}
