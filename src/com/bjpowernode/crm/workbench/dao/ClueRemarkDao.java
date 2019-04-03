package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

public interface ClueRemarkDao {

	List<ClueRemark> getRemarkListByClueId(String clueId);

	int delete(ClueRemark clueRemark);

}
