package com.servlet.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = -6203625286536278208L;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private PrintWriter printWriter;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.genery(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.genery(request, response);
	}

	private void genery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.session = request.getSession();
		this.request = request;
		this.response = response;
		this.printWriter = response.getWriter();
		this.doServlet(request, response);
	}

	protected void sendRedirect(String url) throws IOException {
		this.response.sendRedirect(url);
	}

	protected void dispatcherForward(String url) throws IOException, ServletException {
		this.request.getRequestDispatcher(url).forward(this.request, this.response);
	}

	protected abstract void doServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	public HttpSession getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

}
