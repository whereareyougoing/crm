package com.bjpowernode.crm.settings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

	@Override
	public User login(String loginAct, String loginPwd, String ip) throws LoginException {
		
		/*
		 * 验证登录业务流程
		 * 
		 * 验证账号密码
			select * from tbl_user where loginAct=? and loginPwd=?
			
			通过以上sql语句，得到一个user对象
		 *  判断user对象
		 *  	如果user对象为null，说明 账号密码不正确  为上一层抛出 异常，异常信息为账号密码不正确
		 *      
		 *      如果user对象不为null，说明账号密码正确
		 *      
		 *      从user对象中取出 expireTime,lockState,allowIps
		 *      验证以上3项，为上一层抛出异常，异常信息分别为
		 *      					   账号已时效
		 *      					 账号已锁定
		 *      					ip地址受限
		 *      
		 *      如果全程没有异常信息，那么最后将user对象返回
		 * 
		 */
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loginAct",loginAct);
		map.put("loginPwd",loginPwd);
		User user = userDao.login(map);
		
		if(user==null){
			
			throw new LoginException("账号密码不正确");
			
		}
		
		//如果能够执行到这一行，说明账号密码正确，需要验证其他3项信息
		
		//验证失效时间
		String expireTime = user.getExpireTime();
		String currentTime = DateTimeUtil.getSysTime();
		if(expireTime.compareTo(currentTime) < 0){
			
			throw new LoginException("账号已失效");
			
		}
		
		
		//验证锁定状态
		String lockState = user.getLockState();
		if("0".equals(lockState)){
			
			throw new LoginException("账号已锁定");
			
		}
		
		
		//验证ip地址
		String allowIps = user.getAllowIps();
		if(!allowIps.contains(ip)){
			
			throw new LoginException("ip地址受限");
			
		}
		
		return user;
	}

	@Override
	public List<User> getUserList() {
		
		List<User> uList = userDao.getUserList();
		
		return uList;
	}
	
}






















