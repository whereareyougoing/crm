package com.bjpowernode.crm.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bjpowernode.crm.settings.domain.User;

public class LoginFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("进入到验证是否已经登录过的过滤器");
		
		/*
		 * 取得session对象
		 * 	从session对象中取user
		 * 
		 *  判断user对象
		 *  	如果user为null，说明没有登录过，重定向到登录页
		 *  	如果user不为null，说明登录过，将请求放行到访问的目标资源
		 *  		
		 * 
		 */
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		String path = request.getServletPath();
		//登录相关的请求，自动放行
		if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
			
			chain.doFilter(req, resp);
			
			
		//其他的请求，需要验证有没有登录过	
		}else{
			
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			
			//已经登录过
			if(user!=null){
				
				//将请求放行
				chain.doFilter(req, resp);
			
			//还没有登录过	
			}else{
				
				//重定向到登录页
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				
			}
			
		}
		
		
		
	}

}





















































