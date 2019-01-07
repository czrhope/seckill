<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <title>秒杀列表页</title>
    <%@ include file="common/header.jsp" %>
    <%@ include file="common/tag.jsp" %>
  </head>
  <body>
    <!-- 页面显示部分 -->
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h2>秒杀列表</h2>
			</div>
			<div class="panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>详情页</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<c:forEach var="sk" items="${seckillList }">
								<tr>
									<td>${sk.name }</td>
									<td>${sk.number }</td>
									<td>
										<fmt:formatDate value="${sk.startTime }" pattern="yyyy-MM-dd HH-mm-ss"/>
									</td>
									<td>
										<fmt:formatDate value="${sk.endTime }" pattern="yyyy-MM-dd HH-mm-ss"/>
									</td>
									<td>
										<fmt:formatDate value="${sk.createTime }" pattern="yyyy-MM-dd HH-mm-ss"/>
									</td>
									<td>
										<a class="btn btn-info" href="${pageContext.request.contextPath}/seckill/${sk.seckillId }/detail" target="_blank">详情页</a>
									</td>
								</tr>
							</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div> 
  </body>
</html>