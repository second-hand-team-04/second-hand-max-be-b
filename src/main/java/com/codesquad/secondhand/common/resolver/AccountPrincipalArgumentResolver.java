package com.codesquad.secondhand.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codesquad.secondhand.auth.domain.JwtTokenProvider;
import com.codesquad.secondhand.common.exception.auth.AuthenticationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AccountPrincipal.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String authorization = webRequest.getHeader("Authorization");

		if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
			throw new AuthenticationException();
		}

		String token = authorization.split(" ")[1];
		return jwtTokenProvider.getAccount(token);
	}
}
