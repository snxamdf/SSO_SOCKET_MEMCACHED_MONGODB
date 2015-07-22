<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SSO登录</title>
</head>
<body style="text-align: center;">
	<form id="ssoform" action="/SSO/user.do" method="post">
		<input type="hidden" name="serverUrl" value="${serverUrl }" />
		<input type="hidden" name="site" value="${site }" />
		<input type="hidden" name="loginUrl" value="${loginUrl }" />
		<p>
			用户名：<input id="name" name="name" type="text" value="zhangsan" />
		</p>
		<p>
			密码：<input id="passwd" name="passwd" type="password" value="111111" /> <br />
		</p>
		<p>
			<input type="submit" value="form提交" />
		</p>
	</form>
</body>
<script type="text/javascript">
	document.getElementById("ssoform").submit();
</script>
</html>