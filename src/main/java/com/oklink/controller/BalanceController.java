package com.oklink.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.oklink.service.balance.BalanceService;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-5 上午11:50:02
 * 类说明
 */
@Controller
@RequestMapping(value="/balance/*.do")
public class BalanceController extends MultiActionController {
	
	@Autowired
	private BalanceService balanceService;

	public String index(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		balanceService.getAppBalanceList(0);
		return "user/index";
	}
	
}
