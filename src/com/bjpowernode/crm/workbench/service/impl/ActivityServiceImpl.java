package com.bjpowernode.crm.workbench.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
	
	private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
	private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
	private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

	@Override
	public boolean save(Activity a) {
		
		int count = activityDao.save(a);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public PaginationVO<Activity> pageList(Map<String, Object> paramMap) {
		
		//取dataList
		List<Activity> dataList = activityDao.pageList(paramMap);
		
		
		//取total
		int total = activityDao.getTotal(paramMap);
		
		
		//将dataList和total封装到vo对象中
		PaginationVO<Activity> vo = new PaginationVO<Activity>();
		vo.setDataList(dataList);
		vo.setTotal(total);
		
		
		//返回vo
		return vo;
	}

	@Override
	public boolean delete(String[] ids) {
		
		boolean flag = true;
		
		//应该删除的条数
		int total = activityRemarkDao.getTotalByAids(ids);
		
		//删除需要删除的所有市场活动关联的市场活动备注
		//真正删除的条数
		int count1 = activityRemarkDao.deleteByAids(ids);
		
		if(total!=count1){
			
			flag = false;
			
		}
		
		//删除所有需要删除的市场活动
		int count2 = activityDao.delete(ids);
		if(count2!=ids.length){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> getUserListAndActivity(String id) {
		
		//取得uList
		List<User> uList = userDao.getUserList();
		
		//根据id取得单条记录a
		Activity a = activityDao.getById(id);
		
		
		//将uList和a保存到map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uList",uList);
		map.put("a",a);
		
		
		//返回map
		return map;
	}

	@Override
	public boolean update(Activity a) {
		int count = activityDao.update(a);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public Activity detail(String id) {
		
		Activity a = activityDao.detail(id);
		
		return a;
	}

	@Override
	public List<ActivityRemark> getRemarkListByAid(String aid) {
		
		List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(aid);
		
		return arList;
	}

	@Override
	public boolean deleteRemark(String id) {
		
		int count = activityRemarkDao.deleteRemark(id);
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public boolean saveRemark(ActivityRemark ar) {
		
		int count = activityRemarkDao.saveRemark(ar);
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public boolean updateRemark(ActivityRemark ar) {
		int count = activityRemarkDao.updateRemark(ar);
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public List<Activity> getActivityListByClueId(String clueId) {
		
		List<Activity> aList = activityDao.getActivityListByClueId(clueId);
		
		return aList;
	}

	@Override
	public List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map) {
		
		List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
		
		return aList;
	}

	@Override
	public List<Activity> getActivityListByName(String aname) {
		
		List<Activity> aList = activityDao.getActivityListByName(aname);
		
		return aList;
	}
	
}



































