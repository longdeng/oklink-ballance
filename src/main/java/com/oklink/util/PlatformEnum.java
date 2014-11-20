package com.oklink.util;


/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 上午11:00:35
 * 类说明：各平台注册应用的信息
 */
public enum PlatformEnum {
	OKLINK{
		public int getCode(){
			return PlatformConstans.OKCOIN_CODE;
		}
		public String getName(){
			return "OKLINK";
		}
		public String getClientId(){
			return OKLinkConstants.OKLINK_CLIENT_ID;
		}
		public String getClientSecret(){
			return OKLinkConstants.OKLINK_CLIENT_SECRET;
		}
		public String getRedirectUri(){
			return OKLinkConstants.OKLINK_REDIRECT_URI;
		}
		public String getHost(){
			return OKLinkConstants.OKLINK_HOST_HOME;
		}
		public String getApiHost(){
			return OKLinkConstants.OKLINK_HOST_API;
		}
		public String getAuthorizeUri(){
			return this.getHost()+"/oauth/authorize.do";
		}
		public String getTokenUri(){
			return this.getHost()+"/oauth/token.do";
		}
		public String getBalanceUri(){
			return this.getApiHost()+"/api/v1/wallets";
		}
	},
	COINBASE{
		public int getCode(){
			return PlatformConstans.COINBASE_CODE;
		}
		public String getName(){
			return "COINBASE";
		}
		public String getClientId(){
			return OKLinkConstants.COINBASE_CLIENT_ID;
		}
		public String getClientSecret(){
			return OKLinkConstants.COINBASE_CLIENT_SECRET;
		}
		public String getRedirectUri(){
			return OKLinkConstants.COINBASE_REDIRECT_URI;
		}
		public String getHost(){
			return OKLinkConstants.COINBASE_HOST_HOME;
		}
		public String getApiHost(){
			return OKLinkConstants.COINBASE_HOST_API;
		}
		public String getAuthorizeUri(){
			return this.getHost()+"/oauth/authorize";
		}
		public String getTokenUri(){
			return this.getHost()+"/oauth/token";
		}
		public String getBalanceUri(){
			return this.getApiHost()+"/v1/account/balance";
		}
	};
	
	/**
	 * 获取指定PlatformEnum
	 * @param code
	 * @return
	 */
	public static PlatformEnum getPlatformEnumByCode(int code){
		switch (code) {
			case 0:return PlatformEnum.OKLINK;
			case 1:return PlatformEnum.COINBASE;
			default:return PlatformEnum.OKLINK;
		}
	}
	
	/**
	 * 获取指定PlatformEnum
	 * @param code
	 * @return
	 */
	public static boolean isPlatformEnumCode(int code){
		for(PlatformEnum platformEnum : PlatformEnum.values()){
			if(code==platformEnum.getCode()){
				return true;
			}
		}
		return false;
	}
	
	//编号
	public abstract int getCode();
	//第三方平台名称
	public abstract String getName();
	//第三方平台申请的id
	public abstract String getClientId();
	//第三方平台申请的secret
	public abstract String getClientSecret();
	//第三方平台注册的redirectUri
	public abstract String getRedirectUri();
	//第三方平台OAUTH认证地址
	public abstract String getHost();
	//第三方平台API调用地址
	public abstract String getApiHost();
	//第三方平台申请code地址
	public abstract String getAuthorizeUri();
	//第三方平台申请access_token地址
	public abstract String getTokenUri();
	//API查看余额地址
	public abstract String getBalanceUri();
}
