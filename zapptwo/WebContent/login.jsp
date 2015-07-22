<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试</title>
<script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#subok").click(function() {
			var name = $("#name").val();
			var pass = $("#pass").val();
			$.post("/user.do", {
				name : name,
				pass : pass
			}, function(result) {
				alert(result);
			});
		});
	})
</script>
</head>
<body>
	<form action="/user.do" method="post">
		<input id="name" name="name" value="zs"/> <br /> 
		<input id="pass" name="pass" value="11"/> <br /> 
		<input id="subok" type="button" value="ajax提交" /> 
		<input type="submit" value="form提交" />
	</form>
</body>
</html>