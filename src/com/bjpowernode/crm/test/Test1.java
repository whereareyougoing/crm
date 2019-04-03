package com.bjpowernode.crm.test;

import com.bjpowernode.crm.utils.DateTimeUtil;

public class Test1 {

	public static void main(String[] args) {
		
		String expireTime = "2019-08-31 10:10:10";

		String currentTime = DateTimeUtil.getSysTime();
		
		int count = expireTime.compareTo(currentTime);
		
		if(count>0){
			
			System.out.println("账号没有失效，可以登录");
			
		}else{
			
			System.out.println("账号已失效");
			
			
		}
		
	}

}
