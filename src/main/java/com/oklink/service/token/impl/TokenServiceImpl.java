package com.oklink.service.token.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oklink.dao.bean.AppToken;
import com.oklink.dao.token.TokenDao;
import com.oklink.service.token.TokenService;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:21:43
 * 类说明
 */
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenDao tokenDao;
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public AppToken getAppToken(long userId,int code,String objectId){
		List<Map<String, Object>> list = tokenDao.getAppToken(userId,code,objectId);
		AppToken appToken = null;
		if(list!=null && list.size()>0){
			appToken = AppToken.convert(list.get(0));
		}
		return appToken;
	}
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<AppToken> getAppTokenListByUserId(long id){
		List<Map<String, Object>> list = tokenDao.getAppTokenListByUserId(id);
		List<AppToken> result = null;
		if(list!=null && list.size()>0){
			result = new ArrayList<AppToken>();
			for(Map<String, Object> map : list){
				result.add(AppToken.convert(map));
			}
		}
		return result;
	}
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppToken(AppToken appToken){
		return tokenDao.insertAppToken(appToken);
	}
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppToken(AppToken appToken){
		return tokenDao.updateAppToken(appToken);
	}
	
}
