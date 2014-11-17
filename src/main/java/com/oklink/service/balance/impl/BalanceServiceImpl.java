package com.oklink.service.balance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oklink.dao.balance.BalanceDao;
import com.oklink.dao.bean.AppBalance;
import com.oklink.service.balance.BalanceService;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:19:36
 * 类说明
 */
@Service
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private BalanceDao balanceDao;
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public AppBalance getAppBalance(long id){
		return null;
	}
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<AppBalance> getAppBalanceList(long id){
		List<Map<String, Object>> list = balanceDao.getAppBalanceList(id);
		List<AppBalance> result = null;
		if(list!=null && list.size()>0){
			result = new ArrayList<AppBalance>();
			for(Map<String, Object> map : list){
				result.add(AppBalance.convert(map));
			}
		}
		return result;
	}
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppBalance(AppBalance appBalance){
		return balanceDao.insertAppBalance(appBalance);
	}
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppBalance(AppBalance appBalance){
		return balanceDao.updateAppBalance(appBalance);
	}
}
