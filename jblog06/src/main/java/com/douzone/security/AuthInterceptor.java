package com.douzone.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.douzone.jblog.vo.UserVo;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler) throws Exception {
		
		// 1. handler 종류 확인
		if(handler instanceof HandlerMethod == false) {
			// DefultServletHander가 처리하는 경우(보통, assets의 정적 자원 접근)
			return true;
		}
		
		// 2. casting
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		// 3. Method의 @Auth 받아오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. Method에 @Auth가 없다면 Type에 붙어있는지 확인한다.
		if(auth == null) {
			// 2020-02-27 과제 정답은 아래 한줄과 같다.
			auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
		}
		// 5. Type이나 Method 둘다 @Auth가 적용이 안되어 있는 경우
		if(auth == null) {
			return true;
		}
		
		// 6. 인증(Authentification) 여부 확인(@Auth가 붙어있기 때문)
		HttpSession session = request.getSession();
		// 위의 코드는 다음과 같다.
		// HttpSession session = request.getSession(false);
		if(session == null) {
			response.sendRedirect(request.getContextPath()+"/user/login");
			return false;
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			response.sendRedirect(request.getContextPath()+"/user/login");
			return false;
		}
		// 7. 권한(Authorization) 확인을 위해서  role 가져오기 ("USER","ADMIN")
		String role = auth.value();
		
		// 8. @Auth의 role이 "USER"인 경우
		//    authUser의 role이 "USER","ADMIN"인 값 모두 허용
		if("USER".equals(role)) {
			return true;
		}
		
		// 9. @Auth의 role이 "ADMIN"인 경우
		//	  authUser의 role이 "ADMIN"인 값 만 허용
//		else if("ADMIN".equals(authUser.getRole())==false) {
//			response.sendRedirect(request.getContextPath());
//			return false;
//		}
		
		// @Auth의 role => "ADMIN"
		// authUser의 role => "ADMIN"
		// 관리자 권한이 확인 되었으므로 핸들러 메소드 실행
		return true;
	}
}