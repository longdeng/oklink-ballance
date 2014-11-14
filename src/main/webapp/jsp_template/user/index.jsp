<%@page import="com.oklink.util.WebUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=WebUtil.getPreUrl(request) %>/link/js/jquery/jquery-1.8.2.js"></script>
</head>
<body>
注册：
<form action="">
username:<input type="text" id="email" name="email"><br/> 
password:<input type="password" id="password" name="password"><br/>
<input type="button" value="sign in" onclick="sign(0)">
<input type="button" value="sign up" onclick="sign(1)">
</form>
</body>

<script type="text/javascript">
function sign(type){
	var action = type==0?"login":"register";
	var url = "/user/"+action+".do?random="+Math.round(Math.random()*100);
	var param = {
		email:$("#email").val().trim(),
		password:$("#password").val().trim()
	};
	$.post(url, param, function(data){
		var result = eval('(' + data + ')');
		if(result!=null){
			if(result.resultCode==0){//创建收款单成功
				window.location.href = "/token/index.do";
			} else {
				alert("用户名密码错误!");
			}
		}
	});
}
</script>
</html>