package com.oklink.dao.ballance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.oklink.dao.ballance.BallanceDao;
import com.oklink.dao.bean.AppBallance;
import com.oklink.util.Db;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午12:16:33
 * 类说明
 */
@Repository
public class BallanceDaoImpl implements BallanceDao {

	/**
	 * 获取ballance记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppBallance(long id){
		String sql = "";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 获取ballance记录
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getAppBallanceList(long id){
		String sql = "select * from app_ballance where user_id = ? ";
		return Db.executeQuery(sql, new Object[]{id});
	}
	
	/**
	 * 新增ballance记录
	 * @param id
	 * @return
	 */
	public long insertAppBallance(AppBallance appBallance){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "insert into app_ballance() values()";
		paramList.add(appBallance.getUserId());
		paramList.add(appBallance.getLatestBtcBallance());
		paramList.add(appBallance.getLatestLtcBallance());
		return Db.executeUpdate(sql, paramList.toArray());
	}
	
	/**
	 * 修改ballance记录
	 * @param id
	 * @return
	 */
	public long updateAppBallance(AppBallance appBallance){
		List<Object> paramList = new ArrayList<Object>();
		String sql = "update app_ballance set latest_btc_ballance = ?,latest_ltc_ballance = ?,modify_date = NOW() where id = ? ";
		paramList.add(appBallance.getLatestBtcBallance());
		paramList.add(appBallance.getLatestLtcBallance());
		paramList.add(appBallance.getId());
		return Db.executeUpdate(sql, paramList.toArray());
	}
}
