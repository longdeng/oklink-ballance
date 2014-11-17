package com.oklink.dao.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.oklink.util.PlatformConstans;
import com.oklink.util.StringUtil;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:01:54
 * 类说明
 */
public class AppToken implements Serializable {

	private static final long serialVersionUID = 7464471267474029342L;
	
	private long id;
	private long userId;
	private int code;
	private String objectId;	//第三方平台授权用户标识
	private String accessToken;
	private String refreshToken;
	private long expiresIn;
	private Date createdDate;
	private Date modifyDate;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public static AppToken convert(Map<String, Object> map) {
		if(map == null){
			return null;
		}
		long id = 0;
		if(map.get("id")!=null){
			id =StringUtil.toLong(map.get("id").toString(),0L);
			if(id == 0){
				return null;
			}
		}
		AppToken appToken = new AppToken();
		appToken.setId(id);
		if(map.get("user_id")!=null) appToken.setUserId(StringUtil.toLong(map.get("user_id").toString(), 0l));
		if(map.get("code")!=null) appToken.setCode(StringUtil.toInteger(map.get("code").toString(), 0));
		if(map.get("object_id")!=null) appToken.setObjectId(map.get("object_id").toString());
		if(map.get("access_token")!=null) appToken.setAccessToken(map.get("access_token").toString());
		if(map.get("refresh_token")!=null) appToken.setRefreshToken(map.get("refresh_token").toString());
		if(map.get("expires_in")!=null) appToken.setExpiresIn(StringUtil.toLong(map.get("expires_in").toString(), 0l));
		if(map.get("created_date")!=null) appToken.setCreatedDate((Date)map.get("created_date"));
		if(map.get("modify_date")!=null) appToken.setModifyDate((Date)map.get("modify_date"));
		return appToken;
	}
	
	/**
	 * 验证accessToken是否已经过期
	 */
	public boolean isAccessTokenExpireIn() {
		//由于无法获取服务器token创建时间，客户端验证token过期时把时间进行压缩
		if((this.modifyDate.getTime()+this.expiresIn*1000+PlatformConstans.OAUTH_ACCESSTOKEN_COMPRESS_TIME)>=new Date().getTime()){
			return true;
		}
		return false;
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
