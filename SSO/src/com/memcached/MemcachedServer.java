package com.memcached;

public class MemcachedServer {
	private String address;
	private int port;
	private int weight;

	public MemcachedServer(String address, int port, int weight) {
		this.address = address;
		this.port = port;
		this.weight = weight;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public int getWeight() {
		return weight;
	}

	public String toString() {
		return address + ":" + port + "," + weight;
	}
}
