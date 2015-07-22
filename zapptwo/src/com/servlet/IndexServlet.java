package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.base.BaseServlet;

/**
 * Created by hadoop on 2015/7/3.
 */
public class IndexServlet extends BaseServlet {
	private static final long serialVersionUID = 6956021083502756507L;

	@Override
	public void doServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.sendRedirect("/zapptwo/index.jsp");
	}
}
