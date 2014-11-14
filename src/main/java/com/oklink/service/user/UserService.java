package com.oklink.service.user;

import java.util.List;

import com.oklink.dao.bean.AppUser;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:20:28
 * 类说明
 */
public interface UserService {

	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public AppUser getAppUserById(long id);
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public AppUser getAppUserByEmail(String email);
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<AppUser> getAppUserList(long id);
	
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
