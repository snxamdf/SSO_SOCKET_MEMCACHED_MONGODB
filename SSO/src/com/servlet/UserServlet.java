package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.socket.SocketUtil;
import com.util.Config;
import com.util.RequestParamConver;
import com.util.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 9113605833027606760L;

	@Override
	public void doServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = null;
		// 未登录
		// 获取请求参数
		// 转换请求参数为json
		String jsonData = RequestParamConver.getParamToJson(request);
		// 发送 socket server 验证用户信息
		String resultData = SocketUtil.sendData(jsonData, Config.VERIFY_USER_IP, Config.VERIFY_USER_PORT);
		// 解析 socket resultData
		JSONObject json = JSONObject.fromObject(resultData);
		String success = json.getString("success"), code = json.getString("code");
		if ("ok".equals(success)) {
			if ("000000".equals(code)) {
				url = json.getString("url") + "&sessionId=" + request.getSession().getId();
				// 跳转要免登录系统
				response.sendRedirect(url);
			} else if ("000001".equals(code)) {// 用户名或密码不正确
				this.getPrintWriter().write("用户名或密码不正确:" + resultData);
			} else if ("000002".equals(code)) {// 用户不存在
				request.setAttribute("serverUrl", request.getParameter("serverUrl"));
				request.setAttribute("loginUrl", request.getParameter("loginUrl"));
				request.setAttribute("site", request.getParameter("site"));
				this.getRequest().setAttribute("message", "用户不存在请注册" + resultData);
				this.dispatcherForward("/register.jsp");
			}
		}
	}
}
