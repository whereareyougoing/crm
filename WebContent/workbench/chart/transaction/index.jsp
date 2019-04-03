<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="ECharts/echarts.min.js"></script>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript">

	$(function(){
		
		/* // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        var option = {
            title: {
                text: 'ECharts 入门示例'
            },
            tooltip: {},
            legend: {
                data:['销量']
            },
            xAxis: {
                data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
            },
            yAxis: {},
            series: [{
                name: '销量',
                type: 'bar',
                data: [5, 20, 36, 10, 10, 20]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option); */
        
        doCharts();
	
	})
	
	function doCharts(){
		
		//发出ajax请求，取得统计图需要的数据
		$.ajax({
			
			url : "workbench/transaction/getChartsData.do",
			type : "get",
			dataType : "json",
			success : function(data){
				
				//data:统计图需要的数据
				
				/*
				
					data
						{
					
							"total" : 100,
							"dataList" :
							[
								
								{"value":10,"name":"01资质审查"},
								{"value":5,"name":"02需求分析"},
								{"value":6,"name":"03价值建议"},
								{"value":9,"name":"07成交"},
								...
								...
							
							]
					
						}
				
				*/
				
				var myChart = echarts.init(document.getElementById('main'));
				
				var option = {
					    title: {
					        text: '交易漏斗图',
					        subtext: '不同阶段交易数量统计'
					    },
					    
					    calculable: true,
					    series: [
					        {
					            name:'交易漏斗图',
					            type:'funnel',
					            left: '10%',
					            top: 60,
					            //x2: 80,
					            bottom: 60,
					            width: '80%',
					            // height: {totalHeight} - y - y2,
					            min: 0,
					            max: data.total,	//共多少条记录
					            minSize: '0%',
					            maxSize: '100%',
					            sort: 'descending',
					            gap: 2,
					            label: {
					                show: true,
					                position: 'inside'
					            },
					            labelLine: {
					                length: 10,
					                lineStyle: {
					                    width: 1,
					                    type: 'solid'
					                }
					            },
					            itemStyle: {
					                borderColor: '#fff',
					                borderWidth: 1
					            },
					            emphasis: {
					                label: {
					                    fontSize: 20
					                }
					            },
					            data: data.dataList
					        }
					    ]
					};
			
				myChart.setOption(option); 
			
			
			}
		
		})
		
	}
	
</script>
</head>
<body>

	<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>









































