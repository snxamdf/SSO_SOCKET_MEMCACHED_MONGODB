<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/zappone/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	$(function() {
		setInterval(function() {
			window.location.reload();
		}, 1000*60*3);
	})
</script>
<title>登录成功</title>
</head>
<body>成功one ${USERS_SESSION }
</body>
</html>