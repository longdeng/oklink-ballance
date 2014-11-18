package com.oklink.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oklink.dao.bean.AppUser;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014年11月18日 上午11:35:49
 * 类说明
 */
public class HttpSessionUtil {
	
	public static AppUser getUserFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		AppUser user = session.getAttribute("user")==null?null:(AppUser)session.getAttribute("user");
		return user;
	}

	public static void setUserSession(HttpServletRequest request, AppUser user) {
		HttpSession session = request.getSession();
		if (session != null){
			session.setAttribute("user", user);
		}
	}

	public static void removeUserSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null){
			session.invalidate();
		}
	}
}
