package com.oklink.dao.user;

import java.util.List;
import java.util.Map;

import com.oklink.dao.bean.AppUser;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:15:31
 * 类说明
 */
public interface UserDao {
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppUserById(long id);
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppUserByEmail(String email);
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppUserList(long id);
	
	/**
	 * 新增user记录
	 * @param id
	 * @return
	 */
	public long insertAppUser(AppUser appUser);
	
	/**
	 * 修改user记录
	 * @param id
	 * @return
	 */
	public long updateAppUser(AppUser appUser);
	
}
