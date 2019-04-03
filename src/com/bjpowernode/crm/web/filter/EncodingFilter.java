package com.bjpowernode.crm.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		//处理post请求，中文参数问题
		req.setCharacterEncoding("UTF-8");
		//处理响应流响应中文信息问题
		resp.setContentType("text/html;charset=utf-8");
		
		//将请求放行
		chain.doFilter(req, resp);
		
	}

}
