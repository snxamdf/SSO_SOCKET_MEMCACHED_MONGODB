package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.base.BaseServlet;

public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 9113605833027606760L;

	@Override
	public void doServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("pass", pass);
		this.dispatcherForward("/loginOk.jsp");
	}

}
