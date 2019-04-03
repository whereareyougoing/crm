package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.Customer;

public interface CustomerDao {

	Customer getCustomerByName(String company);

	int save(Customer cus);

	List<String> getCustomerNameList(String name);

}
