package com.bjpowernode.crm.settings.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

public class UserController extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getServletPath();
		
		if("/settings/user/login.do".equals(path)){
			
			login(request,response);
			
		}else if("/settings/user/xxx.do".equals(path)){
			
			//xxx(request,response);
			
		}
		
	}

	private void login(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 验证登录 的操作");
		
		//接收需要验证的账号和密码
		String loginAct = request.getParameter("loginAct");
		String loginPwd = request.getParameter("loginPwd");
		loginPwd = MD5Util.getMD5(loginPwd);
		String ip = request.getRemoteAddr();
		
		/*
		 * 我们这次登录操作，业务层要使用一种自定义异常的方式
		 * 
		 * 思路：
		 * 	  控制器调用业务层，完成验证登录的操作
		 *   业务层验证登录，如果出现了问题，则向上一层抛出相应的异常
		 *   		（
		 *   			如果验证账号密码不正确，则为控制器抛出账号密码不正确的异常信息
		 *   			如果账号超过时效时间了，则为控制器抛出账号已时效的异常信息
		 *   
		 *   			。。。。
		 *   
		 *   			一共有4种异常信息
		 *   				账号密码不正确
		 *   				账号已时效
		 *   				账号已锁定
		 *   				ip地址受限
		 *   		）
		 * 	
		 *  如果业务层全程没有出现异常，则向控制器返回user对象
		 *  将user对象保存到session域中（主要目的是为了在每一张页面都可以随时取得用户的名字）
		 * 
		 */
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		try{
			
			User user = us.login(loginAct,loginPwd,ip);
			
			//如果能走到此行，一定是验证登录成功
			request.getSession().setAttribute("user",user);
			
			//{"success" : true}
			/*String str = "{\"success\" : true}";
			response.getWriter().print(str);*/
			
			PrintJson.printJsonFlag(response, true);
			
		}catch(Exception e){
			e.printStackTrace();
			
			String msg = e.getMessage();
			
			//{"success" : false,"msg" : ?}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success",false);
			map.put("msg",msg);
			
			//将map转换为{"success" : false,"msg" : ?}
			PrintJson.printJsonObj(response, map);
			
		}
		
		
	}

}
























