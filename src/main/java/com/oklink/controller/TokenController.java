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
import com.oklink.dao.bean.AppBalance;
import com.oklink.dao.bean.AppToken;
import com.oklink.dao.bean.AppUser;
import com.oklink.service.token.TokenService;
import com.oklink.service.user.UserService;
import com.oklink.util.HttpClientUtil;
import com.oklink.util.HttpMethodEnum;
import com.oklink.util.HttpSessionUtil;
import com.oklink.util.Logs;
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

	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AppUser appUser = HttpSessionUtil.getUserFromSession(request);
		if(appUser == null){
			Logs.geterrorLogger().error("TokenController index ： user is null");
			return "redirect:/?forward="+StringUtil.UrlEncoder("/user/index.do");
		}
		
		//查询已授权的余额信息
		Map<String,List<AppBalance>> appBalanceMap = new HashMap<String,List<AppBalance>>();
		List<AppToken> appTokenList = tokenService.getAppTokenListByUserId(appUser.getId());
		if(appTokenList!=null&&appTokenList.size()>0){
			AppBalance appBalance = null;
			List<AppBalance> okcoinAppBalanceList = new ArrayList<AppBalance>();
			List<AppBalance> coinbaseAppBalanceList = new ArrayList<AppBalance>();
			for(AppToken appToken : appTokenList){
				int code = appToken.getCode();
				switch (code) {
					case PlatformConstans.OKCOIN_CODE:
						appBalance = getOKLinkBalanceList(appUser.getId(), code, filterAccessToken(appToken));
						if(appBalance!=null){
							okcoinAppBalanceList.add(appBalance);
						}
						break;
					case PlatformConstans.COINBASE_CODE:
						appBalance = getCoinbaseBalanceList(appUser.getId(), code, filterAccessToken(appToken));
						if(appBalance!=null){
							coinbaseAppBalanceList.add(appBalance);
						}
						break;
				}
			}
			appBalanceMap.put(PlatformEnum.OKLINK.getName(), okcoinAppBalanceList);
			appBalanceMap.put(PlatformEnum.COINBASE.getName(), coinbaseAppBalanceList);
		}
		request.setAttribute("appBalanceMap", appBalanceMap);
		return "token/index";
	}

	/**
	 * OKLink
	 * @param userId
	 * @param code
	 * @return
	 */
	public AppBalance getOKLinkBalanceList(long userId,int code,String accessToken){
		if(StringUtil.isEmpty(accessToken)){
			Logs.geterrorLogger().error("TokenController getOKLinkBalanceList ： accessToken is empty");
			return null;
		}
		AppBalance appBalance = null;
		OKLink oklink = new OKLinkBuilder().withAccessToken(accessToken).setHost(PlatformEnum.OKLINK.getApiHost()).build();
		if(oklink == null){
			Logs.geterrorLogger().error("TokenController getOKLinkBalanceList ： oklink is empty");
			return null;
		}
		try {
			//子钱包明细
			UserBalance userBalance = oklink.getUserBalance();
			List<AppBalance> appBalanceList = null;
			List<Wallet> wallets = userBalance.getWalletBalances();
			if(wallets!=null&&wallets.size()>0){
				appBalanceList = new ArrayList<AppBalance>();
				for(Wallet wallet : wallets){
					appBalance = new AppBalance();
					appBalance.setUserId(userId);
					appBalance.setCode(PlatformEnum.OKLINK.getCode());
					appBalance.setObjectId(String.valueOf(wallet.getId()));
					appBalance.setObjectName(wallet.getName());
					appBalance.setLatestBtcBalance(wallet.getBtcBalance().getAmount().doubleValue());
					appBalance.setLatestLtcBalance(wallet.getLtcBalance().getAmount().doubleValue());
					appBalanceList.add(appBalance);
				}
			}
			//钱包总余额
			Amount totalBtcBalance = userBalance.getTotalBtcBalance();
			Amount totalLtcBalance = userBalance.getTotalLtcBalance();
			appBalance = new AppBalance();
			appBalance.setUserId(userId);
			appBalance.setCode(PlatformEnum.OKLINK.getCode());
			appBalance.setParentId(-1);
			appBalance.setObjectId(PlatformEnum.OKLINK.getName());
			appBalance.setObjectName(PlatformEnum.OKLINK.getName());
			appBalance.setLatestBtcBalance(totalBtcBalance.getAmount().doubleValue());
			appBalance.setLatestLtcBalance(totalLtcBalance.getAmount().doubleValue());
			appBalance.setAppBalanceList(appBalanceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appBalance;
	}
	
	/**
	 * Coinbase
	 * @param result
	 * @param userId
	 * @param code
	 * @return
	 */
	public AppBalance getCoinbaseBalanceList(long userId,int code,String accessToken){
		if(StringUtil.isEmpty(accessToken)){
			Logs.geterrorLogger().error("TokenController getCoinbaseBalanceList ： accessToken is empty");
			return null;
		}
		AppBalance appBalance = null;
		Coinbase boinbase = new CoinbaseBuilder().withAccessToken(accessToken).build();
		if(boinbase == null){
			Logs.geterrorLogger().error("TokenController getCoinbaseBalanceList ： boinbase is empty");
			return null;
		}
		try {
			//子钱包明细
			List<AppBalance> appBalanceList = null;
			AccountsResponse r = boinbase.getAccounts();
	        List<Account> accounts = r.getAccounts();
	        if(accounts!=null&&accounts.size()>0){
				appBalanceList = new ArrayList<AppBalance>();
		        for(Account account : accounts){
		        	if(Account.Type.WALLET.equals(account.getType())&&account.isActive()){
		        		appBalance = new AppBalance();
						appBalance.setUserId(userId);
						appBalance.setCode(PlatformEnum.COINBASE.getCode());
						appBalance.setObjectId(account.getId());
						appBalance.setObjectName(account.getName());
						appBalance.setLatestBtcBalance(account.getBalance().getAmount().doubleValue());
						appBalance.setLatestLtcBalance(0);
						appBalanceList.add(appBalance);
		        	}
		        }
	        }
	        //钱包总余额
	        User user = boinbase.getUser();
	        Money money = user.getBalance();
	        appBalance = new AppBalance();
			appBalance.setUserId(userId);
			appBalance.setCode(PlatformEnum.COINBASE.getCode());
			appBalance.setParentId(-1);
			appBalance.setObjectId(PlatformEnum.COINBASE.getName());
			appBalance.setObjectName(PlatformEnum.COINBASE.getName());
			
			appBalance.setLatestBtcBalance(money.getAmount().doubleValue());
			appBalance.setLatestLtcBalance(0);
			appBalance.setAppBalanceList(appBalanceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appBalance;
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
			Logs.geterrorLogger().error("TokenController filterAccessToken ： HttpClientUtil send error!result is empty");
			return null;
		}
		//新token
		String newAccessToken = null;
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(jsonObject!=null&&jsonObject.containsKey("access_token")&&jsonObject.containsKey("expires_in")&&jsonObject.containsKey("refresh_token")){
			//覆盖之前的token
			appToken.setAccessToken(jsonObject.getString("access_token"));
			appToken.setRefreshToken(jsonObject.getString("refresh_token"));
			appToken.setExpiresIn(jsonObject.getLongValue("expires_in"));
			long value = tokenService.updateAppToken(appToken);
			if(value>0){
				newAccessToken = appToken.getAccessToken();
				Logs.geterrorLogger().error("TokenController filterAccessToken ： newAccessToken request success!");
			}else{
				//抛异常
				Logs.geterrorLogger().error("TokenController filterAccessToken ： newAccessToken request failure!");
			}
		}
		return newAccessToken;
	}
	
}
