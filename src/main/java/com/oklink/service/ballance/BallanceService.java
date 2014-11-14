package com.oklink.service.ballance;

import java.util.List;

import com.oklink.dao.bean.AppBallance;


/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:15:10
 * 类说明
 */
public interface BallanceService {

	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public AppBallance getAppBallance(long id);
	
	/**
	 * 获取token记录
	 * @param id
	 * @return
	 */
	public List<AppBallance> getAppBallanceList(long id);
	
	/**
	 * 新增token记录
	 * @param id
	 * @return
	 */
	public long insertAppBallance(AppBallance appBallance);
	
	/**
	 * 修改token记录
	 * @param id
	 * @return
	 */
	public long updateAppBallance(AppBallance appBallance);
	
}
