<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){
		
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		
		//为创建按钮绑定事件
		$("#addBtn").click(function(){
			
			//alert(123);
			
			//打开模态窗口
			/*
				
				操作模态窗口的方式：
					通过id找到指定的模态窗口的jquery对象，调用modal方法，该方法有一个参数,参数为show，表示要打开模态窗口，参数为hide，表示要关闭模态窗口
					
					$(模态窗口).modal("show"/"hide")
						
			
			*/
			
			//$("#createActivityModal").modal("show");
			
			
			//以上是练习部分--------------------------
			
			//需要走后台，目的是取得用户列表，为所有者的下拉框铺用户列表数据
			$.ajax({
			
				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function(data){
					
					/*
						data
							[{用户1},{用户2},{用户n}]
					
					*/
					
					var html = "<option></option>";
					
					//每一个n就是每一个用户
					$.each(data,function(i,n){
						
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
						
					})
					
					$("#create-owner").html(html);
					
					//取得当前登录的用户的id，将id赋值到所有者的下拉框上
					
					//js中会经常使用el表达式，但是el表达式必须要套用在引号中
					var id = "${user.id}";
					$("#create-owner").val(id);
					
					//打开模态窗口
					$("#createActivityModal").modal("show");
					
				}
			
			})
			
			
		})
		
		//为保存按钮绑定事件
		$("#saveBtn").click(function(){
			
			$.ajax({
				
				url : "workbench/activity/save.do",
				data : {
					
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())
					
				},
				type : "post",
				dataType : "json",
				success : function(data){
				
					/*
					
						data
							{"success" : true/false}
					
					*/
					
					if(data.success){
						
						//pageList(1,2);
						
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						
						//添加成功，关闭模态窗口
						$("#createActivityModal").modal("hide");
						
						//提交表单
						//$("#activityAddForm").submit();
						
						//清空表单
						/*
						
							重置表单与提交表单不同
								
								jquery对象提供了提交表单的方法submit()方法
								但是并没有提供重置表单的reset()方法
								
								jquery虽然没有，但是原生dom对象有这个方法
								
								
								
								jquery对象转换为dom对象：
									我们可以将jquery对象当做dom对象的数组来使用（也可以当做集合来使用）
									数组：jquery对象[0]
									
								dom对象转换为jquery对象：
									$(dom)
						
						*/
						//$("#activityAddForm")[0].reset();
						
						
						
					}else{
						
						alert("添加市场活动失败");
						
					}
					

					
				}
			
			})
		
		})
		
		//页面加载完毕后，展现市场活动列表
		/*
			
			以下方法为展现市场活动列表
			都什么情况下调用该方法：
			（1）当前的情况--页面加载完毕后，调用该方法
			（2）添加/修改/删除后，调用该方法
			（3）点击查询按钮，调用该方法
			（4）点击分页组件的时候，调用该方法
			
			以上4项操作后，都要调用pageList方法
			
		*/
		pageList(1,2);
		
		//为查询按钮绑定事件
		$("#searchBtn").click(function(){
			
			//点击查询按钮，需要将搜索框中的信息，保存到隐藏域中
			$("#hidden-name").val($("#search-name").val());
			$("#hidden-owner").val($("#search-owner").val());
			$("#hidden-startDate").val($("#search-startDate").val());
			$("#hidden-endDate").val($("#search-endDate").val());
			
			pageList(1,2);
		
		})
		
		//为全选的复选框绑定事件
		$("#qx").click(function(){
			
			//将所有内容部分的复选框全拿到
			/* var $xz = $("input[name=xz]");
			
			if($("#qx")[0].checked){
				
				//alert("选中了");
				for(var i=0;i<$xz.length;i++){
					
					$xz[i].checked = true;
					
				}
				
			}else{
				
				//alert("反选了");
				
				for(var i=0;i<$xz.length;i++){
					
					$xz[i].checked = false;
					
				}
				
			} */
			
			$("input[name=xz]").prop("checked",this.checked);
			
			
		})
		
		//为内容的复选框绑定事件，来操作全选的复选框
		//根据案例结果，下述alert弹框没有反应，说明绑定事件不成功
		//之所以没有绑定事件成功，是因为我们的input[name=xz]是由我们在js中动态生成的
		//动态生成的元素不能直接绑定具体事件
		/* $("input[name=xz]").click(function(){
			
			alert(123);
		
		}) */
		
		//动态生成的元素我们使用on的形式来绑定事件
		//语法：   $(需要绑定的元素的有效的外层（父级）元素).on(绑定事件的方式，需要绑定的元素，回调函数)
		
		$("#activityBody").on("click",$("input[name=xz]"),function(){
			
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		
		})
		
		//为删除按钮来绑定事件
		$("#deleteBtn").click(function(){
			
			//取得所有复选框选中的元素
			var $xz = $("input[name=xz]:checked");
			
			if($xz.length==0){
				
				alert("请选择需要删除的记录");
				
				
			//肯定是挑√了，而且有可能选中的是一条或者多条	
			}else{
				
				if(confirm("删不删？！")){
					
					//workbench/activity/delete.do?id=A0001&id=A0002&id=A0003
					
					//拼接id参数
					var param = "";
					for(var i=0;i<$xz.length;i++){
						
						param += "id="+$($xz[i]).val();
						
						//如果不是最后一个元素，需要拼接&符号
						if(i<$xz.length-1){
							param += "&";
						}
						
					}
					
					$.ajax({
						
						url : "workbench/activity/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function(data){
							
							if(data.success){
								
								$("#qx").prop("checked",false);
								
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
								
							}else{
								
								alert("删除市场活动失败");
								
							}
							
						}
					
					})
					
				}
				
				
				
				
			}
			
		})
		
		
		//为修改按钮绑定事件
		$("#editBtn").click(function(){
			
			//绑定该事件的目的：我们现在需要走后台，取得修改模态窗口所需要的数据，然后展现修改模态窗口 
			
			//取得复选框挑√的
			//判断必须要挑√，必须只能是一个√
			//将挑√的复选框的value值拿到，就相当于拿到了需要修改记录的id
			var $xz = $("input[name=xz]:checked");
			if($xz.length==0){
				alert("请选择需要修改的记录");
			}else if($xz.length>1){
				alert("只能选择一条记录执行修改");
			
			//肯定挑√了，而且肯定是一条
			}else{
				
				//$xz虽然选中的元素是复选框，但是在else里是能够确定只选中一条的，所以我们可以直接使用val方法来取得其value值
				var id = $xz.val();
				
				//发出ajax请求，传递参数id
				//后台接收id，根据id查单条
				//后台还需要提供所有者下拉框所需的用户列表
				//后台将单条Activity a，和用户列表uList，一起响应会ajax
				//ajax回调函数处理a和uList
				//数据铺完了之后，打开修改的模态窗口
				$.ajax({
			
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						
						"id" : id
						
					},
					type : "get",
					dataType : "json",
					success : function(data){
						
						/*
							
							data
								{"uList":[{用户1},{2},{3}],"a":{市场活动}}
								{"uList":[{用户1},{2},{3}],"a":{"id":?,"name":?,"owner":?....}}
						
						
						*/
						
						var html = "<option></option>";
						
						$.each(data.uList,function(i,n){
							
							html += "<option value='"+n.id+"'>"+n.name+"</option>";
						
						})
						
						$("#edit-owner").html(html);
						
						$("#edit-owner").val(data.a.owner);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);
						$("#edit-id").val(data.a.id);
						
						//打开修改模态窗口
						$("#editActivityModal").modal("show");
						
						
					}
				
				})
				
			}
			
		})
		
		//为更新按钮绑定事件，执行修改操作
		$("#updateBtn").click(function(){
			
			$.ajax({
				
				url : "workbench/activity/update.do",
				data : {
					
					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())
					
				},
				type : "post",
				dataType : "json",
				success : function(data){
				
					/*
					
						data
							{"success" : true/false}
					
					*/
					
					if(data.success){
						
						//pageList(1,2);
						/*
							$("#activityPage").bs_pagination('getOption', 'currentPage'):维持原有的页码
							$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))：维持展现的记录数
						
						*/
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
							,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						
						//修改成功，关闭模态窗口
						$("#editActivityModal").modal("hide");
						
						
						
					}else{
						
						alert("修改市场活动失败");
						
					}
					

					
				}
			
			})
		
		})
		
		
		
	});
	
	/*
		
		前端操作用的：
		操作前端分页需要的这两个组件
		pageNo:页码(第几页)
		pageSize：每页展现的记录数
		为什么列出来这两个，因为所有的分页操作，我们都是要操作这两个数值，其他所有的相关的值，都是从这两个值计算出来的
		
		后端操作用的：
		skipCount:略过的记录数
		pageSize:每页展现的记录数
		
		前端拥有的两个分页组件pageNo和pageSize，在发出ajax请求的时候，应该将这两个参数传递到后台
		后台经过处理，处理成为后台需要的分页组件skipCount，pageSize
		
		为后台传递的参数：
			确定了两个：
			pageNo
			pageSize
		
		除了分页相关的参数，我们还应该为后台传递哪些参数呢？
		还有4个查询条件
			name
			owner
			startDate
			endDate
			
		我们现在已经确定了，一共要为后台传递6个参数
			
			分页相关
			pageNo
			pageSize
			
			条件查询相关
			name
			owner
			startDate
			endDate
			
	
	*/
	function pageList(pageNo,pageSize){
		
		//将隐藏域中的查询条件取值，赋值给搜索框	
		$("#search-name").val($("#hidden-name").val());
		$("#search-owner").val($("#hidden-owner").val());
		$("#search-startDate").val($("#hidden-startDate").val());
		$("#search-endDate").val($("#hidden-endDate").val());
		
		$.ajax({
			
			url : "workbench/activity/pageList.do",
			data : {
				
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
				
			},
			type : "get",
			dataType : "json",
			success : function(data){
				
				/*
				
					data
						
						市场活动列表 我们展现数据需要的
						总记录数 一会我们在处理前端分页组件的时候，前端的分页插件需要的
						
						{"dataList":[{市场活动1},{2},{3}],"total":100}
						
						思考：后端如何为前端传递以上格式的数据？
							
							(1)使用vo
								public class PageVo<T>
									private int total;
									private List<T> dataList;
								
								//做市场活动模块 activity
								PageVo<Activtiy> vo = new PageVo();
								vo.setDataList(市场活动列表)
								
								//做线索模块 clue
								PageVo<Clue> vo = new PageVo();
								vo.setDataList(线索列表)
							
							（2）使用map
								
								map.put("dataList"，市场活动列表);
								map.put("total",100);
									
				
				*/
				
				//将dataList取出，展现市场活动列表
				var html = "";
				
				$.each(data.dataList,function(i,n){
					
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				
				})
				
				$("#activityBody").html(html);
				
				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				
				//以上展现完市场活动列表后，我们需要展现分页信息
				//对于分页信息的展现，我们要使用的是bootstrap的分页插件
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true, 
					
					//表示在点击分页组件的时候，触发该回调函数
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
			   });

				
			}
		
		})
		
	}
	
</script>
</head>
<body>
	
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						
						<input type="hidden" id="edit-id"/>
						
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">
								</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityAddForm">
						
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!-- 
					
						data-dismiss="modal"
							关闭模态窗口
					
					 -->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <!-- 
				  
				  	注意：
				  		type="submit" 非常危险，一定要及时处理成为button
				  
				   -->
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <!-- 
				  	data-toggle="modal" data-target="#createActivityModal"
				  	
				  	data-toggle="modal"
				  			表示触发该按钮，要打开一个模态窗口（模态框）
				  	data-target="#createActivityModal"
				  			表示要打开指定的模态窗口，根据的是id来指定的
				  	
				  	按钮的触发，何时打开模态窗口，应该由我们自己来决定
				  	
				   -->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称123</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<!-- <tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr> -->
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				
				<div id="activityPage"></div>
				
			</div>
			
		</div>
		
	</div>
</body>
</html>