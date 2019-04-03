package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

public interface ActivityRemarkDao {

	int deleteByAids(String[] ids);

	int getTotalByAids(String[] ids);

	List<ActivityRemark> getRemarkListByAid(String aid);

	int deleteRemark(String id);

	int saveRemark(ActivityRemark ar);

	int updateRemark(ActivityRemark ar);

}
