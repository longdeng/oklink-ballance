package com.oklink.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONObject;
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.oklink.api.OKLink;
import com.oklink.api.OKLinkBuilder;
import com.oklink.dao.bean.AppToken;
import com.oklink.dao.bean.AppUser;
import com.oklink.service.token.TokenService;
import com.oklink.util.HttpClientUtil;
import com.oklink.util.HttpMethodEnum;
import com.oklink.util.HttpSessionUtil;
import com.oklink.util.Logs;
import com.oklink.util.PlatformConstans;
import com.oklink.util.PlatformEnum;
import com.oklink.util.StringUtil;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-7 下午4:46:54
 * 类说明：outh授权
 */
@Controller
@RequestMapping(value="/oauth/*.do")
public class OAuthController extends MultiActionController {
	
	@Autowired
	private TokenService tokenService;

	/**
	 * 访问第三方平台
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String authorize(HttpServletRequest request, HttpServletResponse response)throws Exception {
		AppUser appUser = HttpSessionUtil.getUserFromSession(request);
		if(appUser==null){
			Logs.geterrorLogger().error("OAuthController authorize ： user is null");
			return "redirect:/?forward="+StringUtil.UrlEncoder("/user/index.do");
		}
		int code = StringUtil.toInteger(request.getParameter("code"),-1);
		if(code==-1||!PlatformEnum.isPlatformEnumCode(code)){
			Logs.geterrorLogger().error("OAuthController authorize ： code is not invalid");
			return "redirect:/?forward="+StringUtil.UrlEncoder("/user/index.do");
		}
		PlatformEnum platformEnum = PlatformEnum.getPlatformEnumByCode(code);
		String url = platformEnum.getAuthorizeUri()+"?response_type=code&client_id="+platformEnum.getClientId()+"&redirect_uri="+StringUtil.UrlEncoder(platformEnum.getRedirectUri());
		return "redirect:"+url;
	}
	
	/**
	 * 获取code,通过code得到token
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String token(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AppUser appUser = HttpSessionUtil.getUserFromSession(request);
		if(appUser==null){
			Logs.geterrorLogger().error("OAuthController token ： user is null");
			return "redirect:/?forward="+StringUtil.UrlEncoder("/user/index.do");
		}
		String code = request.getParameter("code");
		int type = StringUtil.toInteger(request.getParameter("type"),-1);
		if(StringUtil.isEmpty(code)||type==-1||!PlatformEnum.isPlatformEnumCode(type)){
			Logs.geterrorLogger().error("OAuthController token ： code is not invalid");
			return "redirect:/?forward="+StringUtil.UrlEncoder("/user/index.do");
		}
		PlatformEnum platformEnum = PlatformEnum.getPlatformEnumByCode(type);
		String url = platformEnum.getTokenUri()+"?grant_type=authorization_code&code="+code+"&redirect_uri="+StringUtil.UrlEncoder(platformEnum.getRedirectUri())+"&client_id="+platformEnum.getClientId()+"&client_secret="+platformEnum.getClientSecret();
		String result = HttpClientUtil.send(url, HttpMethodEnum.POST);
		if(StringUtil.isEmpty(result)){
			Logs.geterrorLogger().error("OAuthController token ： HttpClientUtil send error ");
			return "redirect:/?forward="+StringUtil.UrlEncoder("/user/index.do");
		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(jsonObject!=null&&jsonObject.containsKey("access_token")&&jsonObject.containsKey("expires_in")&&jsonObject.containsKey("refresh_token")){
			//获取用户标识
			String objectId = "";
			String accessToken = jsonObject.getString("access_token");
			try{
				switch (type) {
					case PlatformConstans.OKCOIN_CODE:
						OKLink oklink = new OKLinkBuilder().withAccessToken(accessToken).setHost(PlatformEnum.OKLINK.getApiHost()).build();
						objectId = String.valueOf(oklink.getUser().getId());
						break;
					case PlatformConstans.COINBASE_CODE:
						Coinbase boinbase = new CoinbaseBuilder().withAccessToken(accessToken).build();
						objectId = boinbase.getUser().getId();
						break;
				}
			}catch(Exception e){
				Logs.getinfoLogger().error("OAuthController token:	objectId obtain error",e);
				return "redirect:/token/index.do";
			}
			//更新数据token信息
			long value = 0;
			AppToken appToken = tokenService.getAppToken(appUser.getId(), type, objectId);
			if(appToken==null){
				//新增
				appToken = new AppToken();
				appToken.setCode(type);
				appToken.setObjectId(objectId);
				appToken.setUserId(appUser.getId());
				appToken.setAccessToken(accessToken);
				appToken.setRefreshToken(jsonObject.getString("refresh_token"));
				appToken.setExpiresIn(jsonObject.getLongValue("expires_in"));
				value = tokenService.insertAppToken(appToken);
			}else{
				//修改
				appToken.setAccessToken(jsonObject.getString("access_token"));
				appToken.setRefreshToken(jsonObject.getString("refresh_token"));
				appToken.setExpiresIn(jsonObject.getLongValue("expires_in"));
				value = tokenService.updateAppToken(appToken);
			}
			if(value<0){
				Logs.geterrorLogger().error("OAuthController token:Reauthorization failure");
			}else{
				Logs.getinfoLogger().error("OAuthController token:Reauthorization success");
			}
		}
		return "redirect:/token/index.do";
	}
}
