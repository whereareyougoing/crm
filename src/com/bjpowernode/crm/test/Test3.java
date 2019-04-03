package com.bjpowernode.crm.test;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

public class Test3 {

	public static void main(String[] args) {
		
		String loginPwd = "yanmingjie123@bj";
		
		loginPwd = MD5Util.getMD5(loginPwd);
		
		System.out.println(loginPwd);
		
	}

}
