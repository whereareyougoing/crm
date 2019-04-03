package com.bjpowernode.crm.workbench.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

public class TranController extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String path = request.getServletPath();
		
		if("/workbench/transaction/add.do".equals(path)){
			
			add(request,response);
			
		}else if("/workbench/transaction/getCustomerNameList.do".equals(path)){
			
			getCustomerNameList(request,response);
			
		}else if("/workbench/transaction/save.do".equals(path)){
			
			save(request,response);
			
		}else if("/workbench/transaction/detail.do".equals(path)){
			
			detail(request,response);
			
		}else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
			
			getHistoryListByTranId(request,response);
			
		}else if("/workbench/transaction/changeStage.do".equals(path)){
			
			changeStage(request,response);
			
		}else if("/workbench/transaction/getChartsData.do".equals(path)){
			
			getChartsData(request,response);
			
		}

	}

	private void getChartsData(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  取得交易阶段统计图的数据    的操作");
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		/*
		 *
		 * map.put("total",100)
		 * map.put("dataList",dataList)
		 * 
		 * 
		 */
		Map<String,Object> map = ts.getChartsData();
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void changeStage(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  改变阶段    的操作");
		
		String id = request.getParameter("id");
		String stage = request.getParameter("stage");
		String money = request.getParameter("money");
		String expectedDate = request.getParameter("expectedDate");
		String editTime = DateTimeUtil.getSysTime();
		String editBy = ((User)request.getSession().getAttribute("user")).getName();
		
		Tran t = new Tran();
		t.setId(id);
		t.setStage(stage);
		t.setMoney(money);
		t.setExpectedDate(expectedDate);
		t.setEditTime(editTime);
		t.setEditBy(editBy);
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		boolean flag = ts.changeStage(t);
		
		Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		String possibility = pMap.get(stage);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",flag);
		map.put("stage",stage);
		map.put("possibility",possibility);
		map.put("editTime",editTime);
		map.put("editBy",editBy);
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到   根据交易id取得相应的交易历史列表    的操作");
		
		String tranId = request.getParameter("tranId");
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		List<TranHistory> thList = ts.getHistoryListByTranId(tranId);
		
		
		//取得阶段和可能性的对应关系
		Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		
		for(TranHistory th : thList){
			
			String stage = th.getStage();
			
			String possibility = pMap.get(stage);
			
			th.setPossibility(possibility);
			
		}
		
		PrintJson.printJsonObj(response, thList);
		
		
	}

	private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到   跳转到详细信息页    的操作");
		
		String id = request.getParameter("id");
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		Tran t = ts.detail(id);
		
		//处理可能性
		
		//取得阶段和可能性的对应关系
		Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		
		//取得阶段
		String stage = t.getStage();
		
		//通过阶段取得可能性
		String possibility = pMap.get(stage);
		
		//将可能性保存到t对象中
		t.setPossibility(possibility);
		
		request.setAttribute("t",t);
		request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
		
	}

	private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到   添加交易    的操作");
		
		//取得表单参数
		String id = UUIDUtil.getUUID();
		String owner = request.getParameter("owner");
		String money = request.getParameter("money");
		String name = request.getParameter("name");
		String expectedDate = request.getParameter("expectedDate");
		String customerName = request.getParameter("customerName");	//我们得到的是name，但是需要保存的是id，需要在业务层进行特殊处理
		String stage = request.getParameter("stage");
		String type = request.getParameter("type");
		String source = request.getParameter("source");
		String activityId = request.getParameter("activityId");
		String contactsId = request.getParameter("contactsId");
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		String description = request.getParameter("description");
		String contactSummary = request.getParameter("contactSummary");
		String nextContactTime = request.getParameter("nextContactTime");
		
		Tran t = new Tran();
		t.setActivityId(activityId);
		t.setContactsId(contactsId);
		t.setContactSummary(contactSummary);
		t.setCreateBy(createBy);
		t.setCreateTime(createTime);
		t.setDescription(description);
		t.setExpectedDate(expectedDate);
		t.setId(UUIDUtil.getUUID());
		t.setMoney(money);
		t.setName(name);
		t.setNextContactTime(nextContactTime);
		t.setOwner(owner);
		t.setSource(source);
		t.setStage(stage);
		t.setType(type);
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

		boolean flag = ts.save(t,customerName);
		
		if(flag){
			
			response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
			
		}
		
	}

	private void getCustomerNameList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到   根据客户名称模糊查询名称列表    操作");
		
		//接收文本框中输入的客户名称
		String name = request.getParameter("name");
		
		CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
		
		List<String> sList = cs.getCustomerNameList(name);
		
		PrintJson.printJsonObj(response, sList);
		
	}

	private void add(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到跳转到交易添加页的操作");
		
		//取得用户列表（做添加页的所有者）
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		List<User> uList = us.getUserList();
		
		request.setAttribute("uList",uList);
		request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
		
	}

}
























