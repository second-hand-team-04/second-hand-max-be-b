package com.codesquad.secondhand.common.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.codesquad.secondhand.auth.domain.JwtTokenProvider;
import com.codesquad.secondhand.common.exception.ErrorType;
import com.codesquad.secondhand.common.exception.auth.AuthForbiddenException;
import com.codesquad.secondhand.common.exception.auth.AuthUnauthorizedException;
import com.codesquad.secondhand.common.util.AuthorizationHeaderUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
		try {
			String token = AuthorizationHeaderUtil.getToken(webRequest.getNativeRequest(HttpServletRequest.class));
			return jwtTokenProvider.getAccount(token);
		} catch (ExpiredJwtException e) {
			throw new AuthForbiddenException();
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e2) {
			throw new AuthUnauthorizedException(ErrorType.AUTH_ACCESS_TOKEN_UNAUTHORIZED);
		}
	}
}
