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
			return "ba0bb9e1ca317288d08a99038c989d34634d2501741f3651d3c5251695bd6fc9";
		}
		public String getClientSecret(){
			return "33e90c387643b37ebf50bae803c2b67538842b96dd43504d1768b2f15f0baf72";
		}
		public String getRedirectUri(){
			return "http://local.oklink.com:90/oauth/token.do?type=0";
		}
		public String getHost(){
			return "http://local.oklink.com";
		}
		public String getApiHost(){
			return "http://local.oklink.com";
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
			return "16119303307e6ba0c78a280e6734311cd658e2bfebde252ed3fed59fc5ee9fc8";
		}
		public String getClientSecret(){
			return "c5266a9cd74da0a449a92ca303769c66de2a7dd2ae7b024259b99d5fb1306cc0";
		}
		public String getRedirectUri(){
			return "http://local.oklink.com/oauth/token.do?type=1";
		}
		public String getHost(){
			return "https://www.coinbase.com";
		}
		public String getApiHost(){
			return "https://api.coinbase.com";
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
	//第三方平台oauth认证地址
	public abstract String getHost();
	//第三方平台api调用地址
	public abstract String getApiHost();
	//第三方平台申请code地址
	public abstract String getAuthorizeUri();
	//第三方平台申请access_token地址
	public abstract String getTokenUri();
	//api 查看余额地址
	public abstract String getBalanceUri();
}
