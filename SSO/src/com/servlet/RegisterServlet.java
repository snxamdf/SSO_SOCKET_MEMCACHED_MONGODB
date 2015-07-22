package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoDB;
import com.util.MD5.MD5Util;
import com.util.servlet.BaseServlet;

public class RegisterServlet extends BaseServlet {
	private static final long serialVersionUID = 9113605833027606760L;

	@Override
	public void doServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String passwd = request.getParameter("passwd");
		String heSaid = request.getParameter("heSaid");
		DBCollection collection = MongoDB.getDB().getCollection("users");
		BasicDBObject document = new BasicDBObject();
		document.put("name", name);
		document.put("heSaid", heSaid);
		document.put("passwd", MD5Util.MD5Passwd(passwd));
		collection.insert(document);
		request.setAttribute("serverUrl", request.getParameter("serverUrl"));
		request.setAttribute("loginUrl", request.getParameter("loginUrl"));
		request.setAttribute("site", request.getParameter("site"));
		this.dispatcherForward("/index.jsp");
	}
}
