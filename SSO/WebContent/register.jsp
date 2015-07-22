<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
</head>
<body>
	${message }
	<form action="/SSO/register.do" method="post">
		<input type="hidden" name="serverUrl" value="${serverUrl }" />
		<input type="hidden" name="site" value="${site }" />
		<input type="hidden" name="loginUrl" value="${loginUrl }" />
		<p>
			用户名：<input id="name" name="name" type="text" value="" />
		</p>
		<p>
			昵称：<input id="heSaid" name="heSaid" type="text" value="" />
		</p>
		<p>
			密码：<input id="passwd" name="passwd" type="password" value="" /> <br />
		</p>
		<p>
			<input type="submit" value="form提交" />
		</p>
	</form>
</body>
</html>