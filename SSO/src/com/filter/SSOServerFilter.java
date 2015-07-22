package com.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cached.CachedSession;
import com.socket.SocketUtil;
import com.util.Config;
import com.util.RequestParamConver;

public class SSOServerFilter implements Filter {

	protected FilterConfig config;
	protected String encoding;

	public SSOServerFilter() {
		config = null;
		encoding = null; // 编码，默认为UTF-8
	}

	@Override
	public void destroy() {
		config = null;
		encoding = null;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
		Enumeration<?> names = config.getInitParameterNames();
		String name = null, paramter = null;
		while (names.hasMoreElements()) {
			name = names.nextElement().toString();
			paramter = filterConfig.getInitParameter(name);
			if ("encoding".equals(name)) {
				encoding = paramter;
			}
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("UTF-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// 判断用户是否缓存session登录，如果登录直接中转生成免登录url
		String jsonDataStr = CachedSession.checkUserToSession(request);
		JSONObject jsonData = JSONObject.fromObject(jsonDataStr);
		String exist = jsonData.getString("exist");
		if ("true".equals(exist)) {// 已登录
			// 获取参数
			String serverUrl = request.getParameter("serverUrl");
			String loginUrl = request.getParameter("loginUrl");
			// 获取请求参数
			String paramter = "&login=login&name=" + jsonData.getString("name") + "&passwd=" + jsonData.getString("passwd");
			// 参数加密 获取md5
			String md5 = RequestParamConver.encryption(paramter);
			jsonData.accumulate("serverUrl", serverUrl);
			jsonData.accumulate("loginUrl", loginUrl);
			jsonData.accumulate("md5", md5);
			jsonDataStr = jsonData.toString();
			// 参数加密 获取md5
			String url = this.generateFreeUrl(jsonDataStr);
			// 跳转要免登录系统
			response.sendRedirect(url);
		} else if ("false".equals(exist)) {// 未登录
			// 继续执行，跳转登录界面
			chain.doFilter(req, resp);
		}
	}

	/**
	 * SocketMain 生成信任凭证 SocketMain 返回 免登url
	 */
	private String generateFreeUrl(String data) {
		String jsonStr = SocketUtil.sendData(data, Config.FREE_URL_IP, Config.FREE_URL_PORT);
		JSONObject json = JSONObject.fromObject(jsonStr);
		String url = json.getString("url");
		String success = json.getString("success");
		if ("ok".equals(success)) {
			return url;
		}
		return null;
	}
}
