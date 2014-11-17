package com.oklink.service.balance;

import java.util.List;

import com.oklink.dao.bean.AppBalance;


/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:15:10
 * 类说明
 */
public interface BalanceService {

	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public AppBalance getAppBalance(long id);
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<AppBalance> getAppBalanceList(long id);
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppBalance(AppBalance appBalance);
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppBalance(AppBalance appBalance);
	
}
