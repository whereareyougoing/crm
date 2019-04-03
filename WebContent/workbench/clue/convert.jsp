<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

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
		
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
		
		//为放大镜图标绑定事件，打开搜索市场活动的模态窗口
		$("#toSearchActivityModel").click(function(){
			
			$("#searchActivityModal").modal("show");
		
		})
		
		$("#aname").keydown(function(event){
			
			if(event.keyCode==13){
				
				//查询并展现市场活动列表
				$.ajax({
			
					url : "workbench/clue/getActivityListByName.do",
					data : {
						
						"aname" : $.trim($("#aname").val())
						
					},
					type : "get",
					dataType : "json",
					success : function(data){
						
						/*
						
							data
								[{市场活动1},{2},{3}]
						
						*/
						
						var html = "";
						$.each(data,function(i,n){
							
							html += '<tr>';
							html += '<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';
						
						})
						
						$("#activitySearchBody").html(html);
						
					}
				
				})
				
				
				
				return false;
				
			}
		
		})
		
		//提交模态窗口中的单选框选中的市场活动，将选中的内容提交到指定位置
		$("#submitActivityBtn").click(function(){
			
			var $xz = $("input[name=xz]:checked");
			
			if($xz.length==0){
				alert("请选择需要提交的市场活动");
			
			//选了1条
			}else{
				
				var id = $xz.val();
				var name = $("#"+id).html();
				
				$("#activityId").val(id);
				$("#activityName").val(name);
				
				$("#searchActivityModal").modal("hide");
				
			}
		
		})
		
		//为转换按钮来绑定事件，执行线索的转换
		$("#convertBtn").click(function(){
			
			/*
				
				线索的转换，分成两种形式
					一种形式为仅仅只是转换线索
					一种为在转换线索的同时，创建一笔交易
				
					
			*/
		
			//我们根据判断 "为客户创建交易"的复选框有没有挑√，来决定要不要创建交易
			//如果挑√了，则需要创建交易，如果没挑√，则不需要创建交易
			
			/*
			
				不论是否创建交易，我们都需要执行线索的转换，所以我们发出的请求是一致的
				发出传统请求
					（1）点击超链接/点击按钮触发事件，直接跳转
					（2）提交表单
					（3）在地址栏输入地址，敲回车
				
				
			*/
			
			//挑√了，需要创建交易
			if($("#isCreateTransaction").prop("checked")){
			
				//alert("线索转换，同时创建交易");
				//window.location.href = "workbench/clue/convert.do?clueId=${param.id}&money=xxx&name=xxx&expectedDate=xxx&stage=xxx&activityId=xxx";
				//以上以页面直接跳转的形式，提交参数太麻烦，所以我们可以以提交表单的形式，来发出请求
				
				$("#tranForm").submit();
				
			//没有挑√，不需要创建交易
			}else{
				
				//alert("线索转换，不需要创建交易");
				window.location.href = "workbench/clue/convert.do?clueId=${param.id}";
				
			}
			
		})
		
	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<!-- <tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr> -->
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form action="workbench/clue/convert.do" method="post" id="tranForm">
		  
		  <input type="hidden" name="flag" value="a"/>
		  <input type="hidden" name="clueId" value="${param.id}"/>
		  
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stageList}" var="s">
		    		<option value="${s.value}">${s.text}</option>
		    	</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="toSearchActivityModel" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <!-- 该文本框中的市场活动的名字是提供给用户看的 -->
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
		    <!-- 该隐藏域中的市场活动id是提供给form表单提交参数的 -->
		    <input type="hidden" id="activityId" name="activityId"/>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>