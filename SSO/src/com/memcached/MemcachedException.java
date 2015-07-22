package com.memcached;

@SuppressWarnings("serial")
public class MemcachedException extends Exception {

	public MemcachedException() {
		super();
	}

	public MemcachedException(Throwable t) {
		super(t);
	}

	public MemcachedException(String error) {
		super(error);
	}

	public MemcachedException(String error, Throwable t) {
		super(error, t);
	}
}
