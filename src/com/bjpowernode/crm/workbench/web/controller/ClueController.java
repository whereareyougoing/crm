package com.bjpowernode.crm.workbench.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

public class ClueController extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String path = request.getServletPath();
		
		if("/workbench/clue/getUserList.do".equals(path)){
			
			getUserList(request,response);
			
		}else if("/workbench/clue/save.do".equals(path)){
			
			save(request,response);
			
		}else if("/workbench/clue/detail.do".equals(path)){
			
			detail(request,response);
			
		}else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
			
			getActivityListByClueId(request,response);
			
		}else if("/workbench/clue/unbund.do".equals(path)){
			
			unbund(request,response);
			
		}else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
			
			getActivityListByNameAndNotByClueId(request,response);
			
		}else if("/workbench/clue/bund.do".equals(path)){
			
			bund(request,response);
			
		}else if("/workbench/clue/getActivityListByName.do".equals(path)){
			
			getActivityListByName(request,response);
			
		}else if("/workbench/clue/convert.do".equals(path)){
			
			convert(request,response);
			
		}
		

	}

	private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到   线索转换   的操作");
		
		//接收是否需要创建交易的标记
		String flag = request.getParameter("flag");
		
		//接收需要转换的线索的id
		String clueId = request.getParameter("clueId");
		
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		
		Tran t = null;
		//转换的过程中需要创建交易
		if("a".equals(flag)){
			
			t = new Tran();
			//取得交易相关参数(来自交易表单中的数据)
			String id = UUIDUtil.getUUID();
			String money = request.getParameter("money");
			String name = request.getParameter("name");
			String expectedDate = request.getParameter("expectedDate");
			String stage = request.getParameter("stage");
			String activityId = request.getParameter("activityId");
			String createTime = DateTimeUtil.getSysTime();
			
			
			t.setId(id);
			t.setMoney(money);
			t.setName(name);
			t.setExpectedDate(expectedDate);
			t.setStage(stage);
			t.setActivityId(activityId);
			t.setCreateBy(createBy);
			t.setCreateTime(createTime);
			
		}
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		/*
		 * 做转换业务，需要哪些原材料（参数）
		 * 
		 * clueId：做线索转换用的，必须的  
		 * t：必须的（有可能不创建交易，t就没有值）
		 * 		业务层通过判断t是否为null来决定是否创建一笔交易
		 */
		boolean flag1 = cs.convert(clueId,t,createBy);
		
		if(flag1){
			
			response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
			
		}
		
		
	}

	private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  查询市场活动列表（按照名字模糊查询）  的操作");
		
		String aname = request.getParameter("aname");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<Activity> aList = as.getActivityListByName(aname);
		
		PrintJson.printJsonObj(response, aList);
	}

	private void bund(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  关联市场活动  的操作");
		
		//取得相关参数
		String cid = request.getParameter("cid");
		String aids[] = request.getParameterValues("aid");
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag = cs.bund(cid,aids);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 查询市场活动列表（按照名字模糊查询+非关联clueId）  的操作");
		
		String aname = request.getParameter("aname");
		String clueId = request.getParameter("clueId");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("aname",aname);
		map.put("clueId",clueId);
		
		List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
		
		PrintJson.printJsonObj(response, aList);
		
	}

	private void unbund(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  解除关联市场活动  的操作");
		
		//接收关联表的id
		String id = request.getParameter("id");
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag = cs.unbund(id);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  根据线索id查询关联的市场活动列表  的操作");
		
		//取得线索id
		String clueId = request.getParameter("clueId");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<Activity> aList = as.getActivityListByClueId(clueId);
		
		PrintJson.printJsonObj(response, aList);
		
	}

	private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到  跳转到线索详细信息页  的操作");
		
		//取得需要展现详细信息记录的id
		String id = request.getParameter("id");
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		Clue c = cs.detail(id);
		
		request.setAttribute("c", c);
		request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
		
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  添加线索  的操作");
		
		//取得表单数据
		String id = UUIDUtil.getUUID();
		String fullname = request.getParameter("fullname");
		String appellation = request.getParameter("appellation");
		String owner = request.getParameter("owner");
		String company = request.getParameter("company");
		String job = request.getParameter("job");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String website = request.getParameter("website");
		String mphone = request.getParameter("mphone");
		String state = request.getParameter("state");
		String source = request.getParameter("source");
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		String description = request.getParameter("description");
		String contactSummary = request.getParameter("contactSummary");
		String nextContactTime = request.getParameter("nextContactTime");
		String address = request.getParameter("address");
		
		Clue c = new Clue();
		c.setAddress(address);
		c.setAppellation(appellation);
		c.setCompany(company);
		c.setContactSummary(contactSummary);
		c.setCreateBy(createBy);
		c.setCreateTime(createTime);
		c.setDescription(description);
		c.setEmail(email);
		c.setFullname(fullname);
		c.setId(id);
		c.setJob(job);
		c.setMphone(mphone);
		c.setNextContactTime(nextContactTime);
		c.setOwner(owner);
		c.setPhone(mphone);
		c.setSource(source);
		c.setState(state);
		c.setWebsite(website);
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag = cs.save(c);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 打开添加线索模态窗口过程的取得所有者列表   的操作");
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		List<User> uList = us.getUserList();
		
		PrintJson.printJsonObj(response,uList);
		
	}


}
























