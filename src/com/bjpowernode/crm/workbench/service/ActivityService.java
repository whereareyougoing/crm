package com.bjpowernode.crm.workbench.service;

import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

public interface ActivityService {

	boolean save(Activity a);

	PaginationVO<Activity> pageList(Map<String, Object> paramMap);

	boolean delete(String[] ids);

	Map<String, Object> getUserListAndActivity(String id);

	boolean update(Activity a);

	Activity detail(String id);

	List<ActivityRemark> getRemarkListByAid(String aid);

	boolean deleteRemark(String id);

	boolean saveRemark(ActivityRemark ar);

	boolean updateRemark(ActivityRemark ar);

	List<Activity> getActivityListByClueId(String clueId);

	List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map);

	List<Activity> getActivityListByName(String aname);

}
