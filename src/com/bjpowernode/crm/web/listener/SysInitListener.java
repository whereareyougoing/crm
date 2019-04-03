package com.bjpowernode.crm.web.listener;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

public class SysInitListener implements ServletContextListener{
	
	/*
	 * 
	 * 该方法是用来监听上下文域对象创建的方法
	 * 
	 * 一旦上下文域对象创建了，则马上执行该方法
	 * 
	 * ServletContextEvent event：该参数用来取得我们监听的域对象
	 * 	比如我们现在监听的是上下文域对象，那么我们就可以使用event对象来get到上下文域对象
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		System.out.println("上下文域对象创建了");
		
		System.out.println("初始化数据字典开始----------------");
		ServletContext application = event.getServletContext();
		
		DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
		
		Map<String,List<DicValue>> map = ds.getAll();
		
		Set<String> set = map.keySet();
		for(String key:set){
			
			//将map中的键值对转换为application保存的键值对
			application.setAttribute(key,map.get(key));
			
		}
		System.out.println("初始化数据字典结束----------------");
		
		//服务器启动，解析properties，将文件中所有的键值对转换为map中的键值对
		//最后将map保存到application中
		ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
		
		Enumeration<String> e = rb.getKeys();
		
		Map<String,String> pMap = new HashMap<String,String>();
		
		while(e.hasMoreElements()){
			
			//阶段
			String key = e.nextElement();
			//可能性
			String value = rb.getString(key);
			
			pMap.put(key, value);
			
		}
		
		application.setAttribute("pMap",pMap);
		
	}
	
}


















