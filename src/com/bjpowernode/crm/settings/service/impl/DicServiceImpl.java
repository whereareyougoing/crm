package com.bjpowernode.crm.settings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {
	
	private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
	private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
	
	@Override
	public Map<String, List<DicValue>> getAll() {
		
		Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
		
		//取得所有的类型
		List<DicType> dtList = dicTypeDao.getTypeList();
		
		//根据字典类型为key，字典值列表为value，保存到map中
		for(DicType dt:dtList){
			
			//遍历出来每一个类型
			String typeCode = dt.getCode();
			//根据每一个类型取得字典值的列表
			List<DicValue> dvList = dicValueDao.getValueListByTypeCode(typeCode);
			
			map.put(typeCode+"List",dvList);
			
		}
		
		//返回map
		return map;
	}
	
}




















