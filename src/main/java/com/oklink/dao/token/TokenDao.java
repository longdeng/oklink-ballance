package com.oklink.dao.token;

import java.util.List;
import java.util.Map;

import com.oklink.dao.bean.AppToken;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:15:31
 * 类说明
 */
public interface TokenDao {
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppToken(long userId,int code,String objectId);
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppTokenListByUserId(long id);
	
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
