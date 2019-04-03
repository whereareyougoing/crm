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
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

public class ActivityController extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getServletPath();
		
		if("/workbench/activity/getUserList.do".equals(path)){
			
			getUserList(request,response);
			
		}else if("/workbench/activity/save.do".equals(path)){
			
			save(request,response);
			
		}else if("/workbench/activity/pageList.do".equals(path)){
			
			pageList(request,response);
			
		}else if("/workbench/activity/delete.do".equals(path)){
			
			delete(request,response);
			
		}else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
			
			getUserListAndActivity(request,response);
			
		}else if("/workbench/activity/update.do".equals(path)){
			
			update(request,response);
			
		}else if("/workbench/activity/detail.do".equals(path)){
			
			detail(request,response);
			
		}else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
			
			getRemarkListByAid(request,response);
			
		}else if("/workbench/activity/deleteRemark.do".equals(path)){
			
			deleteRemark(request,response);
			
		}else if("/workbench/activity/saveRemark.do".equals(path)){
			
			saveRemark(request,response);
			
		}else if("/workbench/activity/updateRemark.do".equals(path)){
			
			updateRemark(request,response);
			
		}
		

	}

	private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 修改备注  的操作");
		
		//取得响应参数
		String id = request.getParameter("id");
		String noteContent = request.getParameter("noteContent");
		String editTime = DateTimeUtil.getSysTime();
		String editBy = ((User)request.getSession().getAttribute("user")).getName();
		String editFlag = "1";
		
		ActivityRemark ar = new ActivityRemark();
		ar.setId(id);
		ar.setNoteContent(noteContent);
		ar.setEditBy(editBy);
		ar.setEditTime(editTime);
		ar.setEditFlag(editFlag);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.updateRemark(ar);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",flag);
		map.put("noteContent",noteContent);
		map.put("editBy",editBy);
		map.put("editTime",editTime);
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 添加备注  的操作");
		
		//接收相应参数
		String noteContent = request.getParameter("noteContent");
		String activityId = request.getParameter("activityId");
		String id = UUIDUtil.getUUID();
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		String editFlag = "0";
		
		ActivityRemark ar = new ActivityRemark();
		ar.setId(id);
		ar.setActivityId(activityId);
		ar.setNoteContent(noteContent);
		ar.setCreateBy(createBy);
		ar.setCreateTime(createTime);
		ar.setEditFlag(editFlag);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.saveRemark(ar);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",flag);
		map.put("ar",ar);
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 删除备注  的操作");
		
		//取得需要删除备注的id
		String id = request.getParameter("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.deleteRemark(id);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 根据市场活动id取得备注列表  的操作");
		
		//取得市场活动id
		String aid = request.getParameter("aid");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<ActivityRemark> arList = as.getRemarkListByAid(aid);
		
		PrintJson.printJsonObj(response,arList);
		
	}

	private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到  跳转到详细信息页  的操作");
		
		//走后台的目的是为了根据id取得该条记录详细信息（a对象），然后将a对象保存到request域，转发到目标页detail.jsp
		
		//取得需要展现详细信息的记录的id
		String id = request.getParameter("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		Activity a = as.detail(id);
		
		request.setAttribute("a",a);
		request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
		
	}

	private void update(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 修改市场活动 的操作");
		
		//接收表单参数
		String id = request.getParameter("id");
		String owner = request.getParameter("owner");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cost = request.getParameter("cost");
		String description = request.getParameter("description");
		//创建时间：当前系统时间
		String editTime = DateTimeUtil.getSysTime();
		//创建人：当前登录的用户
		String editBy = ((User)request.getSession().getAttribute("user")).getName();
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		Activity a = new Activity();
		a.setId(id);
		a.setOwner(owner);
		a.setName(name);
		a.setStartDate(startDate);
		a.setEndDate(endDate);
		a.setCost(cost);
		a.setDescription(description);
		a.setEditBy(editBy);
		a.setEditTime(editTime);
		
		boolean flag = as.update(a);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  打开市场活动修改模态窗口期间的取值（用户列表+单条市场活动）   的操作");
		
		//取得需要修改的市场活动的id
		String id = request.getParameter("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		/*
		 * 业务层需要给我现在的cotroller返回
		 * 	uList
		 *  a
		 */
		Map<String,Object> map = as.getUserListAndActivity(id);
		
		//{"uList":[{用户1},{2},{3}],"a":{"id":?,"name":?,"owner":?....}}
		PrintJson.printJsonObj(response,map);
		
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  删除市场活动（可批量删除）   的操作");
		
		//接收需要删除的市场活动的id数组
		String ids[] = request.getParameterValues("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.delete(ids);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void pageList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 查询市场活动列表（条件查询+分页查询）  的操作");
		
		//接收参数
		String name = request.getParameter("name");
		String owner = request.getParameter("owner");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");
		int pageNo = Integer.valueOf(pageNoStr);
		int pageSize = Integer.valueOf(pageSizeStr);
		//将前端为我们后端传递的这两个参数，处理成为后端要使用的
		//skipCount和pageSize
		//其中pageSize已经有了
		//处理skipCount
		int skipCount = (pageNo-1)*pageSize;
		
		//以上6个参数准备就绪，统一为sql语句传递就可以了
		//创建一个map对象，保存以上6个参数，传递到sql语句中
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("name",name);
		paramMap.put("owner",owner);
		paramMap.put("startDate",startDate);
		paramMap.put("endDate",endDate);
		paramMap.put("skipCount",skipCount);
		paramMap.put("pageSize",pageSize);
		
		//原材料准备完毕，调用业务层对象做 查询列表的业务
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		//vo包含了dataList和total两项信息
		PaginationVO<Activity> vo = as.pageList(paramMap);
		
		//将vo处理成为{"dataList":[{市场活动1},{2},{3}],"total":100}
		
		PrintJson.printJsonObj(response, vo);
		
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 添加市场活动 的操作");
		
		//接收表单参数
		String id = UUIDUtil.getUUID();
		String owner = request.getParameter("owner");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cost = request.getParameter("cost");
		String description = request.getParameter("description");
		//创建时间：当前系统时间
		String createTime = DateTimeUtil.getSysTime();
		//创建人：当前登录的用户
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		Activity a = new Activity();
		a.setId(id);
		a.setOwner(owner);
		a.setName(name);
		a.setStartDate(startDate);
		a.setEndDate(endDate);
		a.setCost(cost);
		a.setDescription(description);
		a.setCreateBy(createBy);
		a.setCreateTime(createTime);
		
		boolean flag = as.save(a);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 取得用户列表 的操作");
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		List<User> uList = us.getUserList();
		
		//将uList转换为[{用户1},{用户2},{用户n}]
		PrintJson.printJsonObj(response,uList);
		
	}

}
























