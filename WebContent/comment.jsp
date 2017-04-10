<%@page import="wx.entity.App"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>写点评,送积分</title>
<link type="text/css" rel="Stylesheet" href="style.css" />
<style type="text/css">
.bt_menu {
	height: 96px;
	width: 280px;
}
</style>

<%
String WeChatID= (String)request.getAttribute("WeChatID");
String OpenID= (String)request.getAttribute("OpenID");
String Path=App.IP;
%>

<script type="text/javascript" src="script.js"></script>

</head>

<body>
	<form id="form1" name="form1" method="post" action="">
		<table width="100%" cellpadding="10">
			<tr>
				<td><h1>点评:</h1></td>
			</tr>
			<tr>
				<td><label for="textarea"></label> <textarea name="textarea"
						id="textarea" cols="80" rows="15"></textarea></td>
			</tr>
			<tr>
				<td align="center"><input type="button" value="提  交"
					style="font-size: 1em" name="button" id="button" class="bt_menu"
					onclick="comment('<%=WeChatID%>','<%=Path%>','<%=OpenID%>')" />

				</td>
			</tr>
		</table>
	</form>
</body>
</html>
