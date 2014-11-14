package com.oklink.dao.util;

import java.util.List;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午3:37:19
 * 类说明
 */
public interface BaseDao<T> {
	
	public T get(long id);
	
	public List<T> getList();
	
	public long insert(T t);
	
	public long update(T t);
	
	public long remove(long id);

}
