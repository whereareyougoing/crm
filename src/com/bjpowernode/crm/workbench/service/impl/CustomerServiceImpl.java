package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
	
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

	@Override
	public List<String> getCustomerNameList(String name) {
		
		List<String> sList = customerDao.getCustomerNameList(name);
		
		return sList;
	}
	
}
