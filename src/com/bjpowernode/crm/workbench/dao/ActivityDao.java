package com.bjpowernode.crm.workbench.dao;

import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.workbench.domain.Activity;

public interface ActivityDao {

	int save(Activity a);

	List<Activity> pageList(Map<String, Object> paramMap);

	int getTotal(Map<String, Object> paramMap);

	int delete(String[] ids);

	Activity getById(String id);

	int update(Activity a);

	Activity detail(String id);

	List<Activity> getActivityListByClueId(String clueId);

	List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map);

	List<Activity> getActivityListByName(String aname);

}
