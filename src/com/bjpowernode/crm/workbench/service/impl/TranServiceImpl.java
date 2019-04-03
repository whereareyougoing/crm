package com.bjpowernode.crm.workbench.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {
	
	private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
	private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
	@Override
	public boolean save(Tran t, String customerName) {
		
		boolean flag = true;
		
		//根据客户名称，判断客户有没有
		//如果没有客户，需要添加一个客户
		
		Customer cus = customerDao.getCustomerByName(customerName);
		
		//如果cus为null，说明没有这个客户，需要新添加客户
		if(cus==null){
			
			cus = new Customer();
			cus.setContactSummary(t.getContactSummary());
			cus.setCreateBy(t.getCreateBy());
			cus.setCreateTime(DateTimeUtil.getSysTime());
			cus.setDescription(t.getDescription());
			cus.setId(UUIDUtil.getUUID());
			cus.setName(customerName);
			cus.setNextContactTime(t.getNextContactTime());
			cus.setOwner(t.getOwner());
			
			//添加客户
			int count1 = customerDao.save(cus);
			if(count1!=1){
				flag = false;
			}
				
		}
		
		t.setCustomerId(cus.getId());
		//添加交易
		int count2 = tranDao.save(t);
		if(count2!=1){
			flag = false;
		}
		
		//添加交易历史
		TranHistory th = new TranHistory();
		th.setCreateBy(t.getCreateBy());
		th.setCreateTime(DateTimeUtil.getSysTime());
		th.setExpectedDate(t.getExpectedDate());
		th.setId(UUIDUtil.getUUID());
		th.setMoney(t.getMoney());
		th.setStage(t.getStage());
		th.setTranId(t.getId());
		int count3 = tranHistoryDao.save(th);
		if(count3!=1){
			flag = false;
		}
		
		return flag;
	}
	@Override
	public Tran detail(String id) {
		
		Tran t = tranDao.detail(id);
		
		return t;
	}
	@Override
	public List<TranHistory> getHistoryListByTranId(String tranId) {
		
		List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);
		
		return thList;
	}
	@Override
	public boolean changeStage(Tran t) {
		
		boolean flag = true;
		
		//改变交易阶段
		int count1 = tranDao.changeStage(t);
		if(count1!=1){
			flag = false;
		}
		
		//创建交易历史
		TranHistory th = new TranHistory();
		th.setCreateBy(t.getEditBy());
		th.setCreateTime(DateTimeUtil.getSysTime());
		th.setExpectedDate(t.getExpectedDate());
		th.setId(UUIDUtil.getUUID());
		th.setMoney(t.getMoney());
		th.setStage(t.getStage());
		th.setTranId(t.getId());
		int count2 = tranHistoryDao.save(th);
		if(count2!=1){
			flag = false;
		}
		
		
		return flag;
	}
	@Override
	public Map<String, Object> getChartsData() {
		
		//取得total
		int total = tranDao.getTotal();
		
		
		//取得dataList
		List<Map<String,Object>> dataList = tranDao.getDataList();
		
		
		//total和dataList保存到map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("dataList", dataList);
		
		
		//将map返回
		return map;
	}
	
}




























