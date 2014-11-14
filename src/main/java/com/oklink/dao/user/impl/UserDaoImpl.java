package com.oklink.dao.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.oklink.dao.bean.AppUser;
import com.oklink.dao.user.UserDao;
import com.oklink.util.Db;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:16:33
 * 类说明
 */
@Repository
public class UserDaoImpl implements UserDao {

	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppUserById(long id){
		String sql = "select * from app_user where id = ? ";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppUserByEmail(String email){
		String sql = "select * from app_user where email = ? ";
		return Db.executeQuery(sql, new Object[]{email});
	}
	
	/**
	 * 获取user记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppUserList(long id){
		String sql = "select * from app_user where id = ? ";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 新增user记录
	 * @param id
	 * @return
	 */
	public long insertAppUser(AppUser appUser){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "insert into app_user(email,password,create_date,modify_date) values(?,?,NOW(),NOW()) ";
		paramList.add(appUser.getEmail());
		paramList.add(appUser.getPassword());
		return Db.executeUpdate(sql, paramList.toArray());
	}
	
	/**
	 * 修改user记录
	 * @param id
	 * @return
	 */
	public long updateAppUser(AppUser appUser){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "update app_user set email = ?,password = ?,modify_date = NOW() where id = ? ";
		paramList.add(appUser.getEmail());
		paramList.add(appUser.getPassword());
		paramList.add(appUser.getId());
		return Db.executeUpdate(sql, paramList.toArray());
	}
	
}
