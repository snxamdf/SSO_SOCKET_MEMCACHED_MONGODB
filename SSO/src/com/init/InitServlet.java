package com.init;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.memcached.MemcachedException;
import com.memcached.MemcachedPool;
import com.memcached.MemcachedServer;
import com.socket.session.cached.CachedSessionServer;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(InitServlet.class.getName());

	public InitServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		LOGGER.info("InitServlet 已启动...");
		new CachedSessionServer().listenerSocket();
		this.initMemCached();
	}

	/**
	 * 出始化 MemCached 参数
	 */
	private void initMemCached() {
		LOGGER.info("出始化 MemCached 参数...");
		MemcachedServer server = new MemcachedServer("localhost", 11211, 1);
		List<MemcachedServer> servers = new ArrayList<MemcachedServer>();
		servers.add(server);
		MemcachedPool pool = MemcachedPool.getInstance();
		try {
			pool.initPool(servers);
		} catch (MemcachedException e) {
			LOGGER.info("出始化 MemCached 异常" + e.getMessage());
		}
		LOGGER.info("出始化 MemCached 完成");
	}
}
