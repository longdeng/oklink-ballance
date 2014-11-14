package com.oklink.service.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oklink.dao.bean.AppUser;
import com.oklink.dao.user.UserDao;
import com.oklink.service.user.UserService;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:20:48
 * 类说明
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public AppUser getAppUserById(long id){
		List<Map<String, Object>> list = userDao.getAppUserById(id);
		AppUser appUser = null;
		if(list!=null && list.size()>0){
			appUser = AppUser.convert(list.get(0));
		}
		return appUser;
	}
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public AppUser getAppUserByEmail(String email){
		List<Map<String, Object>> list = userDao.getAppUserByEmail(email);
		AppUser appUser = null;
		if(list!=null && list.size()>0){
			appUser = AppUser.convert(list.get(0));
		}
		return appUser;
	}
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<AppUser> getAppUserList(long id){
		List<Map<String, Object>> list = userDao.getAppUserList(id);
		List<AppUser> result = null;
		if(list!=null && list.size()>0){
			result = new ArrayList<AppUser>();
			for(Map<String, Object> map : list){
				result.add(AppUser.convert(map));
			}
		}
		return result;
	}
	
	/**
	 * 新增user记录
	 * @param id
	 * @return
	 */
	public long insertAppUser(AppUser appUser){
		return userDao.insertAppUser(appUser);
	}
	
	/**
	 * 修改user记录
	 * @param id
	 * @return
	 */
	public long updateAppUser(AppUser appUser){
		return userDao.updateAppUser(appUser);
	}
}
