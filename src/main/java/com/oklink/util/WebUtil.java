package com.oklink.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oklink.controller.bean.CookieBean;
import com.oklink.dao.bean.Constant;

public class WebUtil {
	
	/**
	 * 获得host后缀 
	 * @param request
	 * @return .com or .cn
	 */
	public static String getSuffix(HttpServletRequest request){
		String name = request.getHeader("host");
		String domain = OKLinkConstants.cookie_domain;
		if(name !=null && name.split("[.]").length>2){
			String[] arr = name.split("[.]");
			domain = "."+arr[2];
		}else if(domain !=null && domain.split("[.]").length>2){
			String[] arr = domain.split("[.]");
			domain = "."+arr[2];
		}
		return domain;
	}
	public static String getPreUrl(HttpServletRequest request ){
		String preUrl = OKLinkConstants.preUrl ;
		String scheme = StringUtil.isEmpty(request.getHeader("X-Forwarded-Proto"))?request.getScheme():request.getHeader("X-Forwarded-Proto");
		//根据当前host更改后缀 .cn or .com
		if("https".equals(scheme)){
			preUrl= "https"+ OKLinkConstants.preUrl.substring(OKLinkConstants.preUrl.indexOf(":"),OKLinkConstants.preUrl.length());
		}else{
			preUrl= "http"+ OKLinkConstants.preUrl.substring(OKLinkConstants.preUrl.indexOf(":"),OKLinkConstants.preUrl.length());
		}
		return preUrl;
	}
	
	/**
     *  获取请求的客户端IP
     * @param request
     * @return String IP地址
     */
    public static String getClientIPAddress(HttpServletRequest request) {
    	String address = request.getHeader("X-Forwarded-For");
    	if(StringUtil.isEmpty(address)){
    		address = request.getHeader("X-Real-IP");
    	}
        if (!StringUtil.isEmpty(address)) {
        	String[] ips = address.split(",");
        	if(ips!=null && ips.length>0){
        		address = ips[0];
        	}
            return address;
        }
        return request.getRemoteAddr();
    }
    
	/**
	 * 测试的Main方法
	 */
	public static void main(String[] args) {
		
	}

	public static String getCookieSessionId(HttpServletRequest request){
		CookieBean cBean = WebUtil.getCookieBean(request);
		if(cBean!=null){
			String sessionId = cBean.getCoin_session_id();
			if(sessionId != null){
				sessionId=sessionId.trim();
				if(sessionId.trim().equals("null")||sessionId.trim().length()==0){
					 sessionId = null;
				}
			}
			return sessionId;
		}else{
			return null;
		}
	}
	
	public static void setCookieSessionId(HttpServletRequest request,HttpServletResponse res , String sessionId) {
		CookieBean cBean=new CookieBean();
		cBean.setCoin_session_id(sessionId);
		String domain = WebUtil.getDomain(request);
		WebUtil.setCookieBean(res, cBean, domain);
	}
	
	public static void setCookieOrderId(HttpServletRequest request,HttpServletResponse res, long buttonId, long orderId) {
		Cookie cookie_order_id =new Cookie(Constant.MERCHANT_ORDER_ID + buttonId, orderId + "");
		cookie_order_id.setPath("/");
		String domain = WebUtil.getDomain(request);
		cookie_order_id.setDomain(domain);
		cookie_order_id.setHttpOnly(true);
		res.addCookie(cookie_order_id);
	}
	
	public static void clearCookieOrderId(HttpServletRequest request,HttpServletResponse response, long buttonId) {
		Cookie cookie_nike_name=new Cookie(Constant.MERCHANT_ORDER_ID + buttonId, "");
		cookie_nike_name.setPath("/");
		cookie_nike_name.setMaxAge(0);
		String domain = WebUtil.getDomain(request);
		cookie_nike_name.setDomain(domain);
		response.addCookie(cookie_nike_name);
	}
	
	public static Long getCookieOrderId(HttpServletRequest request, long buttonId) {
		String key = Constant.MERCHANT_ORDER_ID + buttonId;
		Cookie cookies[] = request.getCookies();
		if(cookies!=null){
			for(int i=0;i<cookies.length;i++){
				if(cookies[i].getName().equals(key)){
					return StringUtil.toLong(cookies[i].getValue());
				}
			}
			return null;
		}else{
			return null;
		}
	}
	
	public static CookieBean getCookieBean(HttpServletRequest request){
		
		Cookie cookies[] = request.getCookies();
		if(cookies!=null){
			CookieBean cbean=new CookieBean();
			for(int i=0;i<cookies.length;i++){
				String value=cookies[i].getValue();
				if(value!=null && value.trim().length()>0 && !value.equals("null")){
					if(cookies[i].getName().equals(OKLinkConstants.cookie_session_name)){
						cbean.setCoin_session_id(value);
					}else if(cookies[i].getName().equals(OKLinkConstants.cookie_session_nike_name)){
						cbean.setNikeName(StringUtil.UrlDecoder(value));
					}else if(cookies[i].getName().equals(OKLinkConstants.cookie_session_login_info)){
						cbean.setLoginInfo(StringUtil.UrlDecoder(value));
					}else if(cookies[i].getName().equals(OKLinkConstants.cookie_user_id)){
						cbean.setUserid(StringUtil.UrlDecoder(value));
					}
				}
			}
			return cbean;
		}else{
			return null;
		}
		
	}
	
	/**获取客户端的主机根域名*/
	public static String getDomain(HttpServletRequest request){
		String name = request.getHeader("host");
		String domain = OKLinkConstants.cookie_domain;
		if(name !=null && name.split("[.]").length>2){
			String[] arr = name.split("[.]");
			domain = arr[1]+"."+arr[2];
		}
		return domain;
	}
	
	public static void setCookieBean(HttpServletResponse res , CookieBean cBean,String domain){
		if(cBean!=null){
			if(cBean.getCoin_session_id()!=null && cBean.getCoin_session_id().trim().length()>0 && !cBean.getCoin_session_id().trim().equals("null")){
				Cookie cookie_session_id=new Cookie(OKLinkConstants.cookie_session_name,cBean.getCoin_session_id());
				cookie_session_id.setPath("/");
				//cookie_session_id.setMaxAge(CoinOkConstants.cookie_session_id_time);
				cookie_session_id.setDomain(domain);
				cookie_session_id.setHttpOnly(true);
				String ifonline=IniReader.getInstance(2).getValue("onlinesys", "ifonline");
				if(ifonline.equals("1")){
					cookie_session_id.setSecure(true);
				}
				res.addCookie(cookie_session_id);
			}
			if(cBean.getLoginInfo()!=null&&cBean.getLoginInfo().trim().length()>0&&!cBean.getLoginInfo().trim().equals("null")){
				Cookie cookie_loginName=new Cookie(OKLinkConstants.cookie_session_login_info,StringUtil.UrlEncoder(cBean.getLoginInfo()));
				cookie_loginName.setPath("/");
				cookie_loginName.setMaxAge(OKLinkConstants.cookie_session_id_time);
				cookie_loginName.setDomain(domain);
				res.addCookie(cookie_loginName);
			}
			if(cBean.getNikeName()!=null&&cBean.getNikeName().trim().length()>0&&!cBean.getNikeName().trim().equals("null")){
				Cookie cookie_nikeName=new Cookie(OKLinkConstants.cookie_session_nike_name,StringUtil.UrlEncoder(cBean.getNikeName()));
				cookie_nikeName.setPath("/");
				cookie_nikeName.setMaxAge(OKLinkConstants.cookie_session_id_time);
				cookie_nikeName.setDomain(domain);
				res.addCookie(cookie_nikeName);
			}
			if(cBean.getUserid()!=null&&cBean.getUserid().trim().length()>0&&!cBean.getUserid().trim().equals("null")){
				Cookie userid=new Cookie(OKLinkConstants.cookie_user_id,StringUtil.UrlEncoder(cBean.getUserid()));
				userid.setPath("/");
				userid.setDomain(domain);
				String ifonline=IniReader.getInstance(2).getValue("onlinesys", "ifonline");
				if(ifonline.equals("1")){
					userid.setSecure(true);
				}
				res.addCookie(userid);
			}
		}// End Of |if(cBean!=null)|
	}
	
	public static String getMainUrl(HttpServletRequest request ){
		String mainUrl = OKLinkConstants.mainUrl ;
		String scheme = StringUtil.isEmpty(request.getHeader("X-Forwarded-Proto"))?request.getScheme():request.getHeader("X-Forwarded-Proto");
		//根据当前host更改后缀 .cn or .com
		String hostName = request.getHeader("host");
		if("https".equals(scheme)){
			if(!StringUtil.isEmpty(hostName)){
				mainUrl= "https://"+ hostName;
			}else{
				mainUrl= "https"+ OKLinkConstants.mainUrl.substring(OKLinkConstants.mainUrl.indexOf(":"),OKLinkConstants.mainUrl.length());
			}
		}else{
			if(!StringUtil.isEmpty(hostName)){
				mainUrl= "http://"+ hostName;
			}else{
				mainUrl = "http"+ OKLinkConstants.mainUrl.substring(OKLinkConstants.mainUrl.indexOf(":"),OKLinkConstants.mainUrl.length());
			}
		}
		return mainUrl;
	}
	
	/**清除客户端登录Cookie*/
	public static void clearLongLoginCookie(HttpServletRequest request,HttpServletResponse res){
		Cookie cookie_session_id=new Cookie(OKLinkConstants.cookie_session_login_info,"");
		cookie_session_id.setPath("/");
		cookie_session_id.setMaxAge(-1);
		String domain = WebUtil.getDomain(request);
		cookie_session_id.setDomain(domain);
		res.addCookie(cookie_session_id);
	 }
	
	/**清除用户昵称信息cookie*/
	public static void clearNikeNameCookie(HttpServletRequest request,HttpServletResponse response){

		Cookie cookie_nike_name=new Cookie(OKLinkConstants.cookie_session_nike_name,"");
		cookie_nike_name.setPath("/");
		cookie_nike_name.setMaxAge(0);
		String domain = WebUtil.getDomain(request);
		cookie_nike_name.setDomain(domain);
		response.addCookie(cookie_nike_name);
	}
	
	public static void saveLoginCookie(HttpServletRequest request,HttpServletResponse response,String nikeName ) {
		CookieBean cBean = getCookieBean(request);
		if(cBean !=null){
			cBean.setNikeName(nikeName);
			String domain = WebUtil.getDomain(request);
			WebUtil.setCookieBean(response, cBean, domain);
		}
	}
	public static void saveLongLoginCookie(HttpServletRequest request,HttpServletResponse response,String loginInfo) {
		CookieBean cBean = getCookieBean(request);
		if(cBean !=null){
			cBean.setLoginInfo(loginInfo);
			String domain = WebUtil.getDomain(request);
			WebUtil.setCookieBean(response, cBean, domain);
		}
	}
}
