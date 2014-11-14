package com.oklink.controller;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.oklink.controller.bean.Result;
import com.oklink.dao.bean.AppUser;
import com.oklink.service.user.UserService;
/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 上午11:11:06
 * 类说明:用户登录与注册
 */
@Controller
@RequestMapping(value="/user/*.do")
public class UserController extends MultiActionController {
	
	@Autowired
	private UserService userService;
	
	public String index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "user/index";
	}

	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String login(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Result result = new Result();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		AppUser appUser = userService.getAppUserByEmail(email);
		if(appUser!=null&&appUser.getPassword().equals(DigestUtils.md5Hex(password))){
			result.setResultCode(0);
			out.print(result);
			return null;
		}else{
			result.setResultCode(-1);
			out.print(result);
			return null;
		}
	}
	
	/**
	 * 用户注册 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String register(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Result result = new Result();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		AppUser appUser = new AppUser();
		appUser.setEmail(email);
		appUser.setPassword(DigestUtils.md5Hex(password));
		long value = userService.insertAppUser(appUser);
		if(value > 0){
			result.setResultCode(0);
			out.print(result);
			return null;
		}else{
			result.setResultCode(-1);
			out.print(result);
			return null;
		}
	}
	
}
