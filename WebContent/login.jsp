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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">

	$(function(){
		
		//如果当前页面不是顶层窗口，将当前页面设置为顶层窗口
		if(window.top!=window){
			window.top.location=window.location;
		}

		
		//页面加载完毕后，账号的文本框自动获得焦点
		$("#loginAct").focus();
		$("#loginAct").val("");
		
		
		//为登录按钮绑定事件
		$("#submitBtn").click(function(){
			
			//登录操作
			login();
		
		})
		
		//敲回车也能登录
		//event：能够取得敲的是哪个键
		$(window).keydown(function(event){
			
			//如果我们触发的键位的编号为13，说明我们敲的是回车键
			if(event.keyCode==13){
				
				//登录操作
				login();
				
			}
			
		})
		
		
		
		
	})
	
	/*
	
		注意：
			普通的function方法，必须要写在$(function)的外面
	
	*/
	function login(){
			
		//取得账号密码
		//去左右空格的方式 ： $.trim(值)
		var loginAct = $.trim($("#loginAct").val());
		var loginPwd = $.trim($("#loginPwd").val());
		
		if(loginAct=="" || loginPwd==""){
			
			$("#msg").html("账号密码都不能为空");
			
			//强制终止方法体
			return false;
			
		}
		
		//验证账号密码是否正确
		$.ajax({
			
			url : "settings/user/login.do",
			data : {
				
				"loginAct" : loginAct,
				"loginPwd" : loginPwd
				
			},
			type : "post",
			dataType : "json",
			success : function(data){
				
				/*
					
					如果登录验证成功
						data
							{"success" : true}
					
					如果登录验证失败
						data
							{"success" : false,"msg" : ?}
				
				
				*/
				
				if(data.success){
					
					//登录成功
					//跳转到工作台初始页
					window.location.href = "workbench/index.jsp";
					
				}else{
					
					//登录失败
					$("#msg").html(data.msg);
					
				}
				
			}
		
		})
		
	}
	
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>