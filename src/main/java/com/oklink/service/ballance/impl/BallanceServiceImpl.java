package com.oklink.service.ballance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oklink.dao.ballance.BallanceDao;
import com.oklink.dao.bean.AppBallance;
import com.oklink.service.ballance.BallanceService;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:19:36
 * 类说明
 */
@Service
public class BallanceServiceImpl implements BallanceService {

	@Autowired
	private BallanceDao ballanceDao;
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public AppBallance getAppBallance(long id){
		return null;
	}
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<AppBallance> getAppBallanceList(long id){
		List<Map<String, Object>> list = ballanceDao.getAppBallanceList(id);
		List<AppBallance> result = null;
		if(list!=null && list.size()>0){
			result = new ArrayList<AppBallance>();
			for(Map<String, Object> map : list){
				result.add(AppBallance.convert(map));
			}
		}
		return result;
	}
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppBallance(AppBallance appBallance){
		return ballanceDao.insertAppBallance(appBallance);
	}
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppBallance(AppBallance appBallance){
		return ballanceDao.updateAppBallance(appBallance);
	}
}
