<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
			<ul class="admin-menu">
				<li><a href="${pageContext.request.contextPath}/${id }/admin/basic">기본설정</a></li>
				<li><a href="${pageContext.request.contextPath}/${id }/admin/category">카테고리</a></li>
				<li><a href="${pageContext.request.contextPath}/${id }/admin/write">글작성</a></li>
			</ul>