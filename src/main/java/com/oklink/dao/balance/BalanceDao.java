package com.oklink.dao.balance;

import java.util.List;
import java.util.Map;

import com.oklink.dao.bean.AppBalance;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:15:31
 * 类说明
 */
public interface BalanceDao {

	/**
	 * 获取balance记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppBalance(long id);
	
	/**
	 * 获取balance记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppBalanceList(long id);
	
	/**
	 * 新增balance记录
	 * @param id
	 * @return
	 */
	public long insertAppBalance(AppBalance appBalance);
	
	/**
	 * 修改balance记录
	 * @param id
	 * @return
	 */
	public long updateAppBalance(AppBalance appBalance);
}
