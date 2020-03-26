<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% pageContext.setAttribute("newLine", "\n"); %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
		<div id="wrapper">
			<div id="content">
				<div class="blog-content">
					<h4>${post.title}</h4>
					<p>
						${fn:replace(post.contents, newLine,"<br>" )}
					<p>
				</div>
				<ul class="blog-list">
					<c:forEach items="${postList}" var="postVo" varStatus='status'>
						<li>
						<a href="${pageContext.request.contextPath}/${id }/${postVo.categoryNo }/${postVo.no }">
							${postVo.title }
						</a> 
						<span>${postVo.regDate }</span>	
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img src="${pageContext.request.contextPath}/assets/${blog.logo }">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			<ul>
				<c:forEach items="${categoryList}" var="categoryVo" varStatus='status'>
					<li>
					<a href="${pageContext.request.contextPath}/${id }/${categoryVo.no }">
						${categoryVo.name }
					</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>