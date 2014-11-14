package com.oklink.dao.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.oklink.util.StringUtil;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:03:13
 * 类说明
 */
public class AppBallance implements Serializable {
	private static final long serialVersionUID = -8761333341983145683L;

	private long id;
	private long parentId;
	private long userId;
	private int code;
	private String objectId;	//第三方平台的钱包标识（用于余额监控）
	private String objectName;	//第三方平台的钱包名称（用于页面显示）
	private double latestBtcBallance;
	private double latestLtcBallance;
	private List<AppBallance> appBallanceList;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
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
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public double getLatestBtcBallance() {
		return latestBtcBallance;
	}
	public void setLatestBtcBallance(double latestBtcBallance) {
		this.latestBtcBallance = latestBtcBallance;
	}
	public double getLatestLtcBallance() {
		return latestLtcBallance;
	}
	public void setLatestLtcBallance(double latestLtcBallance) {
		this.latestLtcBallance = latestLtcBallance;
	}
	public List<AppBallance> getAppBallanceList() {
		return appBallanceList;
	}
	public void setAppBallanceList(List<AppBallance> appBallanceList) {
		this.appBallanceList = appBallanceList;
	}
	
	public static AppBallance convert(Map<String, Object> map) {
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
		AppBallance appBallance = new AppBallance();
		appBallance.setId(id);
		if(map.get("parent_id")!=null) appBallance.setParentId(StringUtil.toLong(map.get("parent_id").toString(), 0l));
		if(map.get("user_id")!=null) appBallance.setUserId(StringUtil.toLong(map.get("user_id").toString(), 0l));
		if(map.get("code")!=null) appBallance.setCode(StringUtil.toInteger(map.get("code").toString(), 0));
		if(map.get("object_id")!=null) appBallance.setObjectId(map.get("object_id").toString());
		if(map.get("object_name")!=null) appBallance.setObjectName(map.get("object_name").toString());
		if(map.get("latest_btc_ballance")!=null) appBallance.setLatestBtcBallance(StringUtil.toDouble(map.get("latest_btc_ballance").toString(),0d));
		if(map.get("latest_ltc_ballance")!=null) appBallance.setLatestLtcBallance(StringUtil.toDouble(map.get("latest_ltc_ballance").toString(),0d));
		return appBallance;
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
