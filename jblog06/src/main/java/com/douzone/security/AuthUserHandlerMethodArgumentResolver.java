package com.douzone.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.douzone.jblog.vo.UserVo;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {
		
		// supportsParameter(parameter)가 false라면
		// (parameter 가 지원되지 않는다면)
		if(!supportsParameter(parameter)) {
			// WebArgumentResolver.UNRESOLVED를 반환 
			// (method에 parameter를 주지 않음)
			return WebArgumentResolver.UNRESOLVED;
		}
		// 이하는 parameter 가 지원됨을 전제로 진행된다.
		
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession();
		if(session == null) {
			return null;
		}
		
		return session.getAttribute("authUser");
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
		
		// @AuthUser가 안붙여 있으면,
		if(authUser == null) {
			return false;
		}
		
		// 파라미터 타입이 UserVo가 아니면,
		if(!parameter.getParameterType().equals(UserVo.class)) {
			return false;
		}
		
		return true;
	}

}
