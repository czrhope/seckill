<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>秒杀详情页</title>
		<%@ include file="common/header.jsp" %>
	    <%@ include file="common/tag.jsp" %>
	    <!-- 使用CDN获取公共js : https://www.bootcdn.cn/ -->
	    <!-- jquery.countdown倒计时插件 -->
		<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
		<!-- jquery-cookie插件 -->
		<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
		<!-- 引入登录验证逻辑js -->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/seckill.js"></script>
	</head>
	<body>
	<!-- 页面显示部分 -->
		<div class="container">
			<div class="panel panel-default text-center">
				<div class="panel-heading text-center">
					<h1>${seckill.name }</h1>
				</div>
				<div class="panel-body">
					<h2 class="text-danger">
						<!-- 显示time图标 -->
						<span class="glyphicon glyphicon-time"></span>
						<!-- 展示倒计时 -->
						<span class="glyphicon" id="seckill-box"></span>
					</h2>
				</div>
			</div>
		</div>
		<!-- 登录弹出层，输入电话 -->
		<div id="seckillPhoneModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h3 class="modal-title text-center">
							<span class="glyphicon glyphicon-phone"></span>
						</h3>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-xs-8 col-xs-offset-2">
								<input type="text" name="seckillPhone" id="seckillPhone"
										placeholder="请输入手机号" class="form-control" />
							</div>
						</div>
					</div>
					
					<div class="modal-footer">
						<!-- 验证信息 -->
						<span id="seckillPhoneMessage" class="glyphicon"></span>
						<button type="button" id="seckillPhoneBtn" class="btn btn-success">
							<span class="glyphicon glyphicon-phone"></span>
							Submit
						</button>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		$(function(){
			//使用EL表达式传入参数
			seckill.detail.init({
				seckillId:${seckill.seckillId},
				startTime:${seckill.startTime.time},
				endTime:${seckill.endTime.time}
			});
		});
	</script>
</html>