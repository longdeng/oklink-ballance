package com.oklink.dao.token.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.oklink.dao.bean.AppToken;
import com.oklink.dao.token.TokenDao;
import com.oklink.util.Db;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:16:33
 * 类说明
 */
@Repository
public class TokenDaoImpl implements TokenDao {

	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppToken(long userId,int code,String objectId){
		String sql = "select * from app_token where user_id = ? and code = ? and object_id = ? ";
		return Db.executeQuery(sql, new Object[]{userId,code,objectId});
	}
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppTokenListByUserId(long id){
		String sql = "select * from app_token where user_id = ? ";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppToken(AppToken appToken){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "insert into app_token(user_id,code,object_id,access_token,refresh_token,expires_in,create_date,modify_date) values(?,?,?,?,?,?,NOW(),NOW())";
		paramList.add(appToken.getUserId());
		paramList.add(appToken.getCode());
		paramList.add(appToken.getObjectId());
		paramList.add(appToken.getAccessToken());
		paramList.add(appToken.getRefreshToken());
		paramList.add(appToken.getExpiresIn());
		return Db.executeUpdate(sql, paramList.toArray());
	}
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppToken(AppToken appToken){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "update app_token set access_token = ?,refresh_token = ?,expires_in = ?,modify_date = NOW() where id = ? ";
		paramList.add(appToken.getAccessToken());
		paramList.add(appToken.getRefreshToken());
		paramList.add(appToken.getExpiresIn());
		paramList.add(appToken.getId());
		return Db.executeUpdate(sql, paramList.toArray());
	}
}
