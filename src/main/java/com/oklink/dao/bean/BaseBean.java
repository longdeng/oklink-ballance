package com.oklink.dao.bean;

import java.util.Map;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 下午4:41:06
 * 类说明
 */
public interface BaseBean<T> {

	public T convert(Map<String, Object> map);
	
	public String toString();
}
