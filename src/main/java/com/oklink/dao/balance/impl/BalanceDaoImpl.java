package com.oklink.dao.balance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.oklink.dao.balance.BalanceDao;
import com.oklink.dao.bean.AppBalance;
import com.oklink.util.Db;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:16:33
 * 类说明
 */
@Repository
public class BalanceDaoImpl implements BalanceDao {

	/**
	 * 获取balance记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppBalance(long id){
		String sql = "";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 获取balance记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppBalanceList(long id){
		String sql = "select * from app_balance where user_id = ? ";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 新增balance记录
	 * @param id
	 * @return
	 */
	public long insertAppBalance(AppBalance appBalance){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "insert into app_balance() values()";
		paramList.add(appBalance.getUserId());
		paramList.add(appBalance.getLatestBtcBalance());
		paramList.add(appBalance.getLatestLtcBalance());
		return Db.executeUpdate(sql, paramList.toArray());
	}
	
	/**
	 * 修改balance记录
	 * @param id
	 * @return
	 */
	public long updateAppBalance(AppBalance appBalance){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "update app_balance set latest_btc_balance = ?,latest_ltc_balance = ?,modify_date = NOW() where id = ? ";
		paramList.add(appBalance.getLatestBtcBalance());
		paramList.add(appBalance.getLatestLtcBalance());
		paramList.add(appBalance.getId());
		return Db.executeUpdate(sql, paramList.toArray());
	}
}
