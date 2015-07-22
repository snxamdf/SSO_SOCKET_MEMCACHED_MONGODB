package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.util.MD5.MD5Util;

public class RequestParamConver {
	/**
	 * 请求参数加密
	 */
	public static String encryption(String paramter) {
		return MD5Util.getEncryptedPwd(paramter);
	}

	/**
	 * 请求参数加密
	 */
	public static String encryption(HttpServletRequest request) {
		Enumeration<?> enun = request.getParameterNames();
		String param, value;
		StringBuffer sb = new StringBuffer();
		while (enun.hasMoreElements()) {
			param = (String) enun.nextElement();
			value = request.getParameter(param);
			sb.append(param).append("=").append(value).append("&");
		}
		if (sb.length() != 0) {
			sb.delete(sb.length() - 1, sb.length());
			return MD5Util.getEncryptedPwd(sb.toString());
		}
		return null;
	}

	/**
	 * 获取请求参数
	 */
	public static String getParamter(HttpServletRequest request) {
		Enumeration<?> enun = request.getParameterNames();
		String param, value;
		StringBuffer sb = new StringBuffer();
		while (enun.hasMoreElements()) {
			param = (String) enun.nextElement();
			value = request.getParameter(param);
			if (!"serverUrl".equals(param))
				sb.append(param).append("=").append(value).append("&");
		}
		if (sb.length() != 0) {
			sb.delete(sb.length() - 1, sb.length());
			return sb.toString();
		}
		return null;
	}

	/**
	 * 将参数加密转换json
	 */
	public static String getParamToJson(HttpServletRequest request) {
		String name = request.getParameter("name");
		String passwd = request.getParameter("passwd");
		String serverUrl = request.getParameter("serverUrl");
		String loginUrl = request.getParameter("loginUrl");
		// 获取请求参数
		String paramter = "&login=login&name=" + name + "&passwd=" + MD5Util.MD5Passwd(passwd);
		// 参数加密 获取md5
		String md5 = RequestParamConver.encryption(paramter);
		try {
			serverUrl = URLEncoder.encode(serverUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 请求sso验证服务器 生成免登 url
		Map<String, String> mp = new HashMap<String, String>();
		mp.put("name", name);
		mp.put("passwd", MD5Util.MD5Passwd(passwd));
		mp.put("serverUrl", serverUrl);
		mp.put("loginUrl", loginUrl);
		mp.put("md5", md5);

		return JSONObject.fromObject(mp).toString();
	}
}
