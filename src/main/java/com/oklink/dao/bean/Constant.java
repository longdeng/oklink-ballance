package com.oklink.dao.bean;

public class Constant {
	
	//api
	public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String ACCESS_SIGNATURE = "ACCESS_SIGNATURE";
    public static final String ACCESS_NONCE = "ACCESS_NONCE";
    public static final String EXPIRE_FIELD = "expire";
    
    public static final String ERROR_FIELD = "error";
    public static final String ERROR_CODE_FIELD = "errorCode";
    public static final String SUCCESS_FIELD = "success";
    
    public static final int API_IP_LIMIT_AUTHENTICATE_ERROR_NUMS = 21;
    public static final int API_IP_LIMIT_CREATEUSER_NUMS = 22;
    
    public static final int API_IP_LIMIT_TRADE_PASS_ERROR = 23;
    
    public static final int PAGE_SIZE = 25;
    public static final int PAGE_SIZE_MAX = 1000;
    
    public static final String MAIN_URL = "http://localtest.oklink.com"; //www.coinok.com";
    
    public static final long ORDER_EXPIRE_TIME = 10*60*1000;
    
    public static final String MERCHANT_ORDER_ID = "orderid_";
    
}
