package com.oklink.service.token;

import java.util.List;

import com.oklink.dao.bean.AppToken;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:21:19
 * 类说明
 */
public interface TokenService {

	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public AppToken getAppToken(long userId,int code,String objectId);
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<AppToken> getAppTokenListByUserId(long id);
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppToken(AppToken appToken);
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppToken(AppToken appToken);
}
