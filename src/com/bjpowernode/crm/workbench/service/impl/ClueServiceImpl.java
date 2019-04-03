package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.dao.ClueRemarkDao;
import com.bjpowernode.crm.workbench.dao.ContactsActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.dao.ContactsRemarkDao;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.CustomerRemarkDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;
import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {
	
	private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
	private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
	private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
	
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
	private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
	
	private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
	private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
	private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
	
	private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
	private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
	
	
	
	@Override
	public boolean save(Clue c) {
		
		int count = clueDao.save(c);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public Clue detail(String id) {
		
		Clue c = clueDao.detail(id);
		
		return c;
	}

	@Override
	public boolean unbund(String id) {
		
		int count = clueActivityRelationDao.unbund(id);
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	@Override
	public boolean bund(String cid, String[] aids) {
		
		boolean flag = true;
		
		for(String aid:aids){
			
			ClueActivityRelation car = new ClueActivityRelation();
			car.setId(UUIDUtil.getUUID());
			car.setClueId(cid);
			car.setActivityId(aid);
			
			int count = clueActivityRelationDao.bund(car);
			if(count!=1){
				flag = false;
			}
			
		}
		
		return flag;
	}

	@Override
	public boolean convert(String clueId, Tran t, String createBy) {
		
		boolean flag = true;
		
		String createTime = DateTimeUtil.getSysTime();
		
		//(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
		Clue c = clueDao.getById(clueId);
		
		//(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
		//通过线索对象c，取得公司名称
		String company = c.getCompany();
		//通过以上取得的公司名称，在客户表中 根据名称 查询公司有没有
		//虽然查询的业务是看看公司的名字有没有，但是如果有这家公司，我们后续的转换业务是需要这个公司的一些信息的
		//所以我们就不能返回count来进行判断，我们需要返回一个Customer cus 客户的对象
		//如果该cus对象为null，说明没有这家公司，如果cus不为null，说明有这家公司，一会我们要使用cus中的一些信息
		Customer cus = customerDao.getCustomerByName(company);
		
		//如果cus为null，需要创建一个客户
		if(cus==null){
			
			cus = new Customer();
			
			//cus中的很多信息，都是由c对象中转换而来的
			cus.setAddress(c.getAddress());
			cus.setContactSummary(c.getContactSummary());
			cus.setCreateBy(createBy);
			cus.setCreateTime(createTime);
			cus.setDescription(c.getDescription());
			cus.setId(UUIDUtil.getUUID());
			cus.setName(company);
			cus.setNextContactTime(c.getNextContactTime());
			cus.setOwner(c.getOwner());
			cus.setPhone(c.getPhone());
			cus.setWebsite(c.getWebsite());
			
			//添加客户
			int count1 = customerDao.save(cus);
			if(count1!=1){
				flag = false;
			}
			
		}
		
		//以上我们将客户信息处理完毕了，不论以上有没有新建一个客户，总之我们都拿到了一个客户对象cus
		//下面的业务，使用到客户相关的信息(尤其是id)，找cus对象就可以了
		
		//(3) 通过线索对象提取联系人信息，保存联系人
		Contacts con = new Contacts();
		con.setAddress(c.getAddress());
		con.setAppellation(c.getAppellation());
		con.setContactSummary(c.getContactSummary());
		con.setCreateBy(createBy);
		con.setCreateTime(createTime);
		con.setCustomerId(cus.getId());
		con.setDescription(c.getDescription());
		con.setEmail(c.getEmail());
		con.setFullname(c.getFullname());
		con.setId(UUIDUtil.getUUID());
		con.setJob(c.getJob());
		con.setMphone(c.getMphone());
		con.setNextContactTime(c.getNextContactTime());
		con.setOwner(c.getOwner());
		con.setSource(c.getSource());
		
		//添加联系人
		int count2 = contactsDao.save(con);
		if(count2!=1){
			flag = false;
		}
		
		
		//(4)线索备注转换到客户备注以及联系人备注
		//搜索到该线索对应的备注列表
		List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkListByClueId(clueId);
		for(ClueRemark clueRemark : clueRemarkList){
			
			//取得需要转换的线索的备注信息
			String noteContent = clueRemark.getNoteContent();
			
			//将线索备注转换为客户备注
			CustomerRemark customerRemark = new CustomerRemark();
			customerRemark.setCreateBy(createBy);
			customerRemark.setCreateTime(createTime);
			customerRemark.setCustomerId(cus.getId());
			customerRemark.setEditFlag("0");
			customerRemark.setId(UUIDUtil.getUUID());
			customerRemark.setNoteContent(noteContent);
			//创建客户备注
			int count3 = customerRemarkDao.save(customerRemark);
			if(count3!=1){
				flag = false;
			}
			
			//将线索备注转换为联系人备注
			ContactsRemark contactsRemark = new ContactsRemark();
			contactsRemark.setCreateBy(createBy);
			contactsRemark.setCreateTime(createTime);
			contactsRemark.setContactsId(con.getId());
			contactsRemark.setEditFlag("0");
			contactsRemark.setId(UUIDUtil.getUUID());
			contactsRemark.setNoteContent(noteContent);
			int count4 = contactsRemarkDao.save(contactsRemark);
			if(count4!=1){
				flag = false;
			}
			
		}
		
		//(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
		//取得该线索关联的  关联关系列表
		List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
		for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
			
			//取得线索关联的每一个市场活动id
			String activityId = clueActivityRelation.getActivityId();
			String contactsId = con.getId();
			String id = UUIDUtil.getUUID();
			
			ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
			contactsActivityRelation.setActivityId(activityId);
			contactsActivityRelation.setContactsId(contactsId);
			contactsActivityRelation.setId(id);
			//添加联系人和市场活动的关联关系
			int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
			if(count5!=1){
				flag = false;
			}
		}
		
		//(6) 如果有创建交易需求，创建一条交易
		//根据参数t是否为null，判断是否需要创建交易
		
		//如果需要创建交易
		if(t!=null){
			
			//此时t已经有一些基础数据了
			//转换的页面，交易的表单中提交的数据  金额，交易名称，预计成交日期，阶段，市场活动Id
			//id
			//createBy createTime
			
			//除了以上的基础数据之外，我们还可以根据线索，客户以及联系人的信息，继续完善t对象
			t.setContactsId(con.getId());
			t.setContactSummary(c.getContactSummary());
			t.setCustomerId(cus.getId());
			t.setDescription(c.getDescription());
			t.setNextContactTime(c.getNextContactTime());
			t.setOwner(c.getOwner());
			t.setSource(c.getSource());
			
			//添加交易
			int count6 = tranDao.save(t);
			if(count6!=1){
				flag = false;
			}
			
			//(7) 如果创建了交易，则创建一条该交易下的交易历史
			TranHistory th = new TranHistory();
			th.setCreateBy(createBy);
			th.setCreateTime(createTime);
			th.setExpectedDate(t.getExpectedDate());
			th.setId(UUIDUtil.getUUID());
			th.setMoney(t.getMoney());
			th.setStage(t.getStage());
			th.setTranId(t.getId());
			
			//添加交易历史
			int count7 = tranHistoryDao.save(th);
			if(count7!=1){
				flag = false;
			}
		}
		
		//(8) 删除线索备注
		for(ClueRemark clueRemark : clueRemarkList){
			
			int count8 = clueRemarkDao.delete(clueRemark);
			if(count8!=1){
				flag = false;
			}
			
		}
		
		//(9) 删除线索和市场活动的关系
		for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
			
			int count9 = clueActivityRelationDao.delete(clueActivityRelation);
			if(count9!=1){
				flag = false;
			}
			
		}
		
		//(10) 删除线索
		int count10 = clueDao.delete(clueId);
		if(count10!=1){
			flag = false;
		}
		
		return flag;
	}

	
	
}






























































