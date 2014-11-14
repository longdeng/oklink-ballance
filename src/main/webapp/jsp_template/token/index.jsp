<%@page import="java.util.*,com.oklink.util.WebUtil,com.oklink.util.PlatformEnum,com.oklink.dao.bean.AppBallance"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=WebUtil.getPreUrl(request) %>/link/js/jquery/jquery-1.8.2.js"></script>
</head>
<%
	List<PlatformEnum> platformEnumList = request.getAttribute("platformEnumList")!=null?(List<PlatformEnum>)(request.getAttribute("platformEnumList")):null;
	Map<Integer,List<AppBallance>> appBallanceMap = request.getAttribute("appBallanceMap")!=null?(Map<Integer,List<AppBallance>>)(request.getAttribute("appBallanceMap")):null;
%>
<body>
授权：
<hr>
<%
	for(PlatformEnum platformEnum : PlatformEnum.values()){
		%>
		<br><input type="button" id="" name="" value="<%=platformEnum.getName()%>授权" onclick="javascript:window.location.href='/oauth/authorize.do?code=<%=platformEnum.getCode()%>&random='+Math.round(Math.random()*100);"/><br>
		<%
	}
%>
余额：
<hr>
<%
	if(appBallanceMap!=null&&appBallanceMap.size()>0){
		for(PlatformEnum platformEnum : PlatformEnum.values()){
			List<AppBallance> appBallanceList = appBallanceMap.get(platformEnum.getName());
			if(appBallanceList!=null&&appBallanceList.size()>0){
				%>
				<span><%=platformEnum.getName() %>：</span><br/>
				<%
				for(AppBallance appBallance : appBallanceList){
					%>
					&nbsp;&nbsp;&nbsp;<span>平台名称：</span><span><%=appBallance.getObjectName()%></span><br/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red">BTC总余额：</span><span><%=appBallance.getLatestBtcBallance()%></span><br/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red">LTC总余额：</span><span><%=appBallance.getLatestLtcBallance()%></span><br/>
					<%
					List<AppBallance> subAppBallanceList = appBallance.getAppBallanceList();
					if(subAppBallanceList!=null&&subAppBallanceList.size()>0){
						for(AppBallance subAppBallance : subAppBallanceList){
							%>
							&nbsp;&nbsp;&nbsp;<span>钱包名称：</span><span><%=subAppBallance.getObjectName()%></span><br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>BTC余额：</span><span><%=subAppBallance.getLatestBtcBallance()%></span><br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>LTC余额：</span><span><%=subAppBallance.getLatestLtcBallance()%></span><br/>
							<%
						}
					}
				}
			}
		}
	}
%>
</body>
</html>