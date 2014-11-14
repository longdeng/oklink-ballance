package com.oklink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSONObject;
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.AccountsResponse;
import com.coinbase.api.entity.User;
import com.oklink.api.OKLink;
import com.oklink.api.OKLinkBuilder;
import com.oklink.api.bean.Amount;
import com.oklink.api.bean.UserBalance;
import com.oklink.api.bean.Wallet;
import com.oklink.dao.bean.AppBallance;
import com.oklink.dao.bean.AppToken;
import com.oklink.dao.bean.AppUser;
import com.oklink.service.token.TokenService;
import com.oklink.service.user.UserService;
import com.oklink.util.HttpClientUtil;
import com.oklink.util.HttpMethodEnum;
import com.oklink.util.PlatformConstans;
import com.oklink.util.PlatformEnum;
import com.oklink.util.StringUtil;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 上午11:52:31
 * 类说明
 */
@Controller
@RequestMapping(value="/token/*.do")
public class TokenController extends MultiActionController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	public String index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		long userId = StringUtil.toLong(request.getParameter("userId"), 1l);
		AppUser appUser = userService.getAppUserById(userId);
		if(appUser == null){
			return null;
		}
		
		//查询已授权的余额信息
		Map<String,List<AppBallance>> appBallanceMap = new HashMap<String,List<AppBallance>>();
		List<AppToken> appTokenList = tokenService.getAppTokenListByUserId(userId);
		if(appTokenList!=null&&appTokenList.size()>0){
			AppBallance appBallance = null;
			List<AppBallance> okcoinAppBallanceList = new ArrayList<AppBallance>();
			List<AppBallance> coinbaseAppBallanceList = new ArrayList<AppBallance>();
			for(AppToken appToken : appTokenList){
				int code = appToken.getCode();
				switch (code) {
					case PlatformConstans.OKCOIN_CODE:
						appBallance = getOKLinkBallanceList(userId, code, filterAccessToken(appToken));
						if(appBallance!=null){
							okcoinAppBallanceList.add(appBallance);
						}
						break;
					case PlatformConstans.COINBASE_CODE:
						appBallance = getCoinbaseBallanceList(userId, code, filterAccessToken(appToken));
						if(appBallance!=null){
							coinbaseAppBallanceList.add(appBallance);
						}
						break;
				}
			}
			appBallanceMap.put(PlatformEnum.OKLINK.getName(), okcoinAppBallanceList);
			appBallanceMap.put(PlatformEnum.COINBASE.getName(), coinbaseAppBallanceList);
		}
		request.setAttribute("appBallanceMap", appBallanceMap);
		return "token/index";
	}

	/**
	 * OKLink
	 * @param userId
	 * @param code
	 * @return
	 */
	public AppBallance getOKLinkBallanceList(long userId,int code,String accessToken){
		if(StringUtil.isEmpty(accessToken)){
			return null;
		}
		AppBallance appBallance = null;
		OKLink oklink = new OKLinkBuilder().withAccessToken(accessToken).build();
		if(oklink == null){
			return null;
		}
		try {
			//子钱包明细
			UserBalance userBalance = oklink.getUserBalance();
			List<AppBallance> appBallanceList = null;
			List<Wallet> wallets = userBalance.getWalletBalances();
			if(wallets!=null&&wallets.size()>0){
				appBallanceList = new ArrayList<AppBallance>();
				for(Wallet wallet : wallets){
					appBallance = new AppBallance();
					appBallance.setUserId(userId);
					appBallance.setCode(PlatformEnum.OKLINK.getCode());
					appBallance.setObjectId(String.valueOf(wallet.getId()));
					appBallance.setObjectName(wallet.getName());
					appBallance.setLatestBtcBallance(wallet.getBtcBalance().getAmount());
					appBallance.setLatestLtcBallance(wallet.getLtcBalance().getAmount());
					appBallanceList.add(appBallance);
				}
			}
			//钱包总余额
			Amount totalBtcBalance = userBalance.getTotalBtcBalance();
			Amount totalLtcBalance = userBalance.getTotalLtcBalance();
			appBallance = new AppBallance();
			appBallance.setUserId(userId);
			appBallance.setCode(PlatformEnum.OKLINK.getCode());
			appBallance.setParentId(-1);
			appBallance.setObjectId(PlatformEnum.OKLINK.getName());
			appBallance.setObjectName(PlatformEnum.OKLINK.getName());
			appBallance.setLatestBtcBallance(totalBtcBalance.getAmount());
			appBallance.setLatestLtcBallance(totalLtcBalance.getAmount());
			appBallance.setAppBallanceList(appBallanceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appBallance;
	}
	
	/**
	 * Coinbase
	 * @param result
	 * @param userId
	 * @param code
	 * @return
	 */
	public AppBallance getCoinbaseBallanceList(long userId,int code,String accessToken){
		if(StringUtil.isEmpty(accessToken)){
			return null;
		}
		AppBallance appBallance = null;
		Coinbase boinbase = new CoinbaseBuilder().withAccessToken(accessToken).build();
		if(boinbase == null){
			return null;
		}
		try {
			//子钱包明细
			List<AppBallance> appBallanceList = null;
			AccountsResponse r = boinbase.getAccounts();
	        List<Account> accounts = r.getAccounts();
	        if(accounts!=null&&accounts.size()>0){
				appBallanceList = new ArrayList<AppBallance>();
		        for(Account account : accounts){
		        	if(Account.Type.WALLET.equals(account.getType())&&account.isActive()){
		        		appBallance = new AppBallance();
						appBallance.setUserId(userId);
						appBallance.setCode(PlatformEnum.COINBASE.getCode());
						appBallance.setObjectId(account.getId());
						appBallance.setObjectName(account.getName());
						appBallance.setLatestBtcBallance(account.getBalance().getAmount().doubleValue());
						appBallance.setLatestLtcBallance(0);
						appBallanceList.add(appBallance);
		        	}
		        }
	        }
	        //钱包总余额
	        User user = boinbase.getUser();
	        Money money = user.getBalance();
	        appBallance = new AppBallance();
			appBallance.setUserId(userId);
			appBallance.setCode(PlatformEnum.COINBASE.getCode());
			appBallance.setParentId(-1);
			appBallance.setObjectId(PlatformEnum.COINBASE.getName());
			appBallance.setObjectName(PlatformEnum.COINBASE.getName());
			
			appBallance.setLatestBtcBallance(money.getAmount().doubleValue());
			appBallance.setLatestLtcBallance(0);
			appBallance.setAppBallanceList(appBallanceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appBallance;
	}
	
	/**
	 * token过期处理
	 * @param appToken
	 * @return
	 */
	public String filterAccessToken(AppToken appToken){
		if(!appToken.isAccessTokenExpireIn()){
			return appToken.getAccessToken();
		}
		PlatformEnum platformEnum = PlatformEnum.getPlatformEnumByCode(appToken.getCode());
		String url = platformEnum.getTokenUri()+"?grant_type=refresh_token&refresh_token="+appToken.getRefreshToken()+"&redirect_uri="+StringUtil.UrlEncoder(platformEnum.getRedirectUri())+"&client_id="+platformEnum.getClientId()+"&client_secret="+platformEnum.getClientSecret();
		String result = HttpClientUtil.send(url, HttpMethodEnum.POST);
		if(StringUtil.isEmpty(result)){
			return null;
		}
		//新token
		String newAccessToken = null;
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(jsonObject!=null&&jsonObject.containsKey("access_token")&&jsonObject.containsKey("expires_in")&&jsonObject.containsKey("refresh_token")){
			AppToken updateAppToken = new AppToken();
			updateAppToken.setAccessToken(jsonObject.getString("access_token"));
			updateAppToken.setRefreshToken(jsonObject.getString("refresh_token"));
			updateAppToken.setExpiresIn(jsonObject.getLongValue("expires_in"));
			long value = tokenService.updateAppToken(updateAppToken);
			if(value>0){
				newAccessToken = updateAppToken.getAccessToken();
			}else{
				//抛异常
			}
		}
		return newAccessToken;
	}
	
}
