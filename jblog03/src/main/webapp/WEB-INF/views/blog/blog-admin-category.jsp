<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<style type="text/css">
	#admin-cat.admin-cat tr td a.on{
		display:block;
		position:absolute 1 1;
		width:15px;
		height:15px;
		background:url('${pageContext.request.contextPath}/assets/images/delete.jpg') 0 0 no-repeat;
	}
</style>

<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-3.4.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/ejs/ejs.js"></script>

<script>
/* guestbook spa application */
var startNo = 0;
var isEnd = false;

/* var listItemTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-item-template.ejs"
});
var listTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-template.ejs"
}); */

var messageBox = function(title, message, callback){
	$("#dialog-message p").text(message);
	$("#dialog-message")
		.attr("title", title)
		.dialog({
			modal: true,
			buttons: {
				"확인": function() {
					$(this).dialog( "close" );
		        }
			},
			close: callback
		});
}

var render = function(vo, mode){
	var html = 
		"<tr data-no='" + vo.no + "'>" +
		"	<td>" + vo.no + "</td>" +
		"	<td>" + vo.name + "</td>" +
		"	<td>" + vo.countPost + "</td>" +
		"	<td>" + vo.description + "</td>" +
		"	<td>" +
		"		<a class='on' href='' data-no='"+vo.no+"' data-id='${id }'></a>" +
		"	</td>" +
		"</tr>";
		
	
	 if(mode){
	 	$("#admin-cat-tr-th").after(html);
	 } else {
	 	$("#admin-cat").append(html);
	 }
	// $("#admin-cat")[mode ? 'prepend' : 'append'](html);
}

var fetchList = function(){
	if(isEnd){
		return;	
	}
	
	$.ajax({
		url: '${pageContext.request.contextPath }/${id }/api/admin/category/list/',
		async: true,
		type: 'get',
		dataType: 'json',
		data: '',
		success: function(response){
			if(response.result != "success"){
				console.error(response.message);
				return;
			}
			
			// redering
			// var html = listTemplate.render(response);
			
			console.log(response);
			console.log('${pageContext.request.contextPath }/${id }/api/admin/category/list/');
			
			$.each(response.data, function(index, vo){
				render(vo);
			});
			
		},
		error: function(xhr, status, e){
			console.error(status + ":" + e);
		}
	});	
}

$(function(){
/* 	// 삭제 다이알로 객체 만들기
	var dialogDelete = $("#dialog-delete-form").dialog({
		autoOpen: false,
		width: 300,
		height: 220,
		modal: true,
		buttons: {
			"삭제": function(){
				var no = $("#hidden-no").val();
				var password = $("#password-delete").val();
				
				$.ajax({
					url: '${pageContext.request.contextPath }/${id }/api/admin/category/delete/'+no,
					async: true,
					type: 'delete',
					dataType: 'json',
					data: 'password=' + password,
					success: function(response){
						if(response.result != "success"){
							console.error(response.message);
							return;
						}
						
						if(response.data != -1){
							$("#list-guestbook li[data-no=" + response.data + "]").remove();
							dialogDelete.dialog('close');
							return;
						}
						
						// 비밀번호가 틀린경우
						$("#dialog-delete-form p.validateTips.error").show();
					},
					error: function(xhr, status, e){
						console.error(status + ":" + e);
					}
				});
			},
			"취소": function(){
				$(this).dialog('close');
			}
		},
		close: function(){
			$("#hidden-no").val("");
			$("#password-delete").val("");
			$("#dialog-delete-form p.validateTips.error").hide();
		}
	});
	 */
	
	// 입력폼 submit 이벤트
	$('#admin-cat-add-form').submit(function(event){
		event.preventDefault();
		
		var vo = {};
		vo.name = $("#input-name").val();
		if(vo.name == ''){
			messageBox("카테고리 생성", "카테고리 이름은 필수 항목 입니다.", function(){
				$("#input-name").focus();
			});
			return;	
		}
		
		vo.description = $("#input-description").val();
		if(vo.description == ''){
			messageBox("카테고리 생성", "카테고리 설명은 필수 항목 입니다.", function(){
				$("#input-description").focus();
			});
			return;	
		}
		
		$.ajax({
			url: '${pageContext.request.contextPath }/${id }/api/admin/category/add',
			async: true,
			type: 'post',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(vo),
			success: function(response){
				if(response.result != "success"){
					console.error(response.message);
					return;
				}
				
				render(response.data, true);
				// var html = listItemTemplate.render(response.data);
				// $("#admin-cat").prepend(html);
				
				// form reset
				$("#admin-cat-add-form")[0].reset();
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});
	});
	
	// Live Event: 존재하지 않는 element의 이벤트 핸들러를 미리 세팅하는 것
	// delegation(위임, document)
	// 삭제버튼
	$(document).on('click', '#admin-cat.admin-cat tr td a', function(event){
		event.preventDefault();
		
		var no = $(this).data().no;
		
		console.log(no+' is del.');
		
		$.ajax({
			url: '${pageContext.request.contextPath }/${id }/api/admin/category/delete/'+no,
			async: true,
			type: 'delete',
			dataType: 'json',
			data: 'no=' + no,
			success: function(response){
				if(response.result != "success"){
					console.error(response.message);
					return;
				}
				
				if(response.data != -1){
					$("#admin-cat tr[data-no=" + response.data + "]").remove();
					console.log(response.data+' is remove.');
					return;
				}
				
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});
	});
	
	// 처음 리스트 가져오기
	fetchList();
	
});
</script>
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
		<div id="wrapper">
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a href="${pageContext.request.contextPath}/${id }/admin/basic">기본설정</a></li>
					<li class="selected">카테고리</li>
					<li><a href="${pageContext.request.contextPath}/${id }/admin/write">글작성</a></li>
				</ul>
		      	<table id='admin-cat' class="admin-cat">
		      		<tr id='admin-cat-tr-th'>
		      			<th>번호</th>
		      			<th>카테고리명</th>
		      			<th>포스트 수</th>
		      			<th>설명</th>
		      			<th>삭제</th>      			
		      		</tr>
		      		
				</table>
      	
      			<h4 class="n-c">새로운 카테고리 추가</h4>
				<form id='admin-cat-add-form' action="" method="post">
					<table id="admin-cat-add">
						<tr>
							<td class="t">카테고리명</td>
							<td><input type="text" id="input-name" name="name"></td>
						</tr>
						<tr>
							<td class="t">설명</td>
							<td><input type="text" id="input-description" name="description"></td>
						</tr>
						<tr>
							<td class="s">&nbsp;</td>
							<td><input type="submit" value="카테고리 추가"></td>
						</tr>
					</table>
				</form>
				<div id="dialog-message" title="" style="display:none">
  					<p></p>
				</div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>