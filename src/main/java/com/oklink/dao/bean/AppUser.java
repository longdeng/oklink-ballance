package com.oklink.dao.bean;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.oklink.util.StringUtil;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 上午11:57:48
 * 类说明
 */
public class AppUser implements Serializable {

	private static final long serialVersionUID = 6635616196010697638L;
	
	private long id;
	private String email;
	private String password;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public static AppUser convert(Map<String, Object> map) {
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
		AppUser appUser = new AppUser();
		appUser.setId(id);
		if(map.get("email")!=null) appUser.setEmail(map.get("email").toString());
		if(map.get("password")!=null) appUser.setPassword(map.get("password").toString());
		return appUser;
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
