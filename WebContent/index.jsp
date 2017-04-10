<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
	<h1>服务器已启动</h1>
	<%
		int total = (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
		int max = (int) (Runtime.getRuntime().maxMemory() / (1024.0 * 1024));
		int free = (int) (Runtime.getRuntime().freeMemory() / (1024.0 * 1024));
		out.println("最大可用内存 maxMemory(): " + max + " M<br/>");
		out.println("当前占用的内存 totalMemory(): " + total + " M<br/>");
		out.println("当前JVM空闲内存 freeMemory(): " + free + " M<br/>");
	%>
</body>
</html>
