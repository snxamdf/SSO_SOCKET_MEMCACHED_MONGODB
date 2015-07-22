package com.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.session.LoginSession;
import com.util.Config;
import com.util.MD5.MD5Util;
import com.util.socket.SocketUtil;

/**
 * 请求编码过滤器，过滤所有请求，将请求参数字符串转换为指定编码，避免乱码出现 使用方法：
 * 在web.xml中配置此Filter过滤所有路径，使用时无需任何额外代码，直接取参数即可 支持Post方法和Get方法得请求。
 */
public class SSOFilter implements Filter {

	protected FilterConfig config;
	protected String encoding;
	protected String sso_login_https;
	protected String loginUrl;
	protected String site;

	public SSOFilter() {
		config = null;
		encoding = null; // 编码，默认为UTF-8
	}

	@Override
	public void destroy() {
		config = null;
		encoding = null;
		sso_login_https = null;
		loginUrl = null;
		site = null;
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
			} else if ("sso-login-https".equals(name)) {
				sso_login_https = paramter;
			} else if ("domainLogin".equals(name)) {
				try {
					loginUrl = URLEncoder.encode(paramter, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if ("site".equals(name)) {
				site = paramter;
			}
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		resp.setContentType("text/html;charset=utf-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;//判断是否登录
		String name = request.getParameter("name");
		String passwd = request.getParameter("passwd");

		boolean res = LoginSession.isLogin(name, this.site, request.getSession());
		if (res) {
			//已经登录
			chain.doFilter(req, resp);
		} else {//未登录
			String toKen = request.getParameter("token");
			String md5 = request.getParameter("md5");
			String login = request.getParameter("login");
			if (toKen != null && md5 != null && "login".equals(login)) {
				String sessionId = request.getParameter("sessionId");
				//获取请求参数
				String originalString = "&login=login&name=" + name + "&passwd=" + passwd;
				boolean paramBool = MD5Util.validation(originalString, md5);//验证参数
				if (paramBool) {//免登录 参数验证成功
					String data = this.getParamToJson(request);
					//验证信任凭证
					data = SocketUtil.sendData(data, Config.FREE_VALIDATION_IP, Config.FREE_VALIDATION_PORT);
					if ("true".equals(data)) {//验证成功
						//添加SSO登录服务器缓存session数据
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", name);//登录帐号
						map.put("passwd", passwd);//登录帐号
						map.put("sessionId", sessionId);//session id
						map.put("lastTime", String.valueOf(request.getSession().getLastAccessedTime()));//最后更新时间
						map.put("site", this.site);//网站 站点
						map.put("siteSessionId", request.getSession().getId());//站点Session
						map.put("SEND_TYPE", Config.CACHED_SESSION);
						data = SocketUtil.sendData(JSONObject.fromObject(map).toString(), Config.CACHED_SESSION_IP, Config.CACHED_SESSION_PORT);
						JSONObject jsonData = JSONObject.fromObject(data);
						//SSO登录服务器缓存session成功
						if ("ok".equals(jsonData.getString("success"))) {
							LoginSession.login(name, request.getSession());
							String serverUrl = request.getParameter("serverUrl");
							response.sendRedirect(serverUrl);
						} else {
							response.getWriter().write("SSO登录服务器缓存session失败!");
						}
					} else if ("false".equals(data)) {//验证失败
						response.getWriter().write("信任凭证  验证失败!");
					}
				} else {
					response.getWriter().write("免登录 参数验证失败!");
				}
			} else {
				response.sendRedirect(sso_login_https + "?loginUrl=" + this.loginUrl + "&serverUrl=" + this.getServerUrl(request) + "&site=" + this.site);
			}
		}
	}

	/**
	 * 将参数加密转换json
	 */
	public String getParamToJson(HttpServletRequest request) {
		String name = request.getParameter("name");
		String toKen = request.getParameter("token");
		//获取请求参数
		//参数加密 获取md5

		//请求sso验证服务器 生成免登 url
		Map<String, String> mp = new HashMap<String, String>();
		mp.put("name", name);
		mp.put("token", toKen);

		return JSONObject.fromObject(mp).toString();
	}

	/**
	 * 获取请求服务地址和参数
	 */
	private String getServerUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		Enumeration<?> params = request.getParameterNames();
		String param = "", values = "";
		StringBuffer sb = new StringBuffer();
		while (params.hasMoreElements()) { // 获取请求参数名列表
			param = params.nextElement().toString();
			values = request.getParameter(param);
			sb.append(param).append("=").append(values).append("&");
		}
		if (sb.length() != 0) {
			sb.delete(sb.length() - 1, sb.length());
			sb.insert(0, url);
			sb.insert(url.length(), "?");
		} else {
			sb.insert(0, url);
		}
		try {
			url = URLEncoder.encode(sb.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * 过滤哪些url不验证登陆信息
	 */
	@SuppressWarnings("unused")
	private boolean filterUrl(HttpServletRequest request) {
		// 存放 要过滤的路径 如有其它的 自己添加
		String[] urlStr = new String[] { "login.do" };
		// 存放 要过滤带该参数请求的url 如有其它的 自己添加
		String[] paramStr = new String[] {};
		Enumeration<?> params = request.getParameterNames();
		String param = "", values = "";
		while (params.hasMoreElements()) { // 获取请求参数名列表
			param = (String) params.nextElement();
			values = request.getParameter(param);
			if (values == null) { // 当参数值为空时忽略
				values = "";
			}
			if (request.getMethod().equals("GET")) {
				try {
					values = java.net.URLDecoder.decode(values, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			for (String ps : paramStr) {
				if (ps.equals(param + "=" + values)) {
					return true;
				}
			}
		}
		String url = request.getRequestURI();
		if (request.getMethod().equals("GET")) {
			try {
				url = java.net.URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		for (String us : urlStr) {
			if (url.indexOf(us) != -1) {
				return true;
			}
		}
		return false;
	}
}
