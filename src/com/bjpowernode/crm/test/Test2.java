package com.bjpowernode.crm.test;

import com.bjpowernode.crm.utils.DateTimeUtil;

public class Test2 {

	public static void main(String[] args) {
		
		String lockState = "1";
		
		if("0".equals(lockState)){
			
			System.out.println("账号已锁定");
			
		}else{
			
			System.out.println("账号处于启用状态");
		}
		
	}

}
