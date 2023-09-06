package com.codesquad.secondhand.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;
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
public class AuthInterceptor implements HandlerInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (CorsUtils.isPreFlightRequest(request)) {
			return true;
		}

		if (request.getRequestURI().equals("/api/auth") && request.getMethod().equals(HttpMethod.POST.name())) {
			return true;
		}

		try {
			String token = AuthorizationHeaderUtil.getToken(request);
			request.setAttribute("account", jwtTokenProvider.generateAccount(token));
			return true;
		} catch (ExpiredJwtException e) {
			throw new AuthForbiddenException();
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e2) {
			throw new AuthUnauthorizedException(ErrorType.AUTH_ACCESS_TOKEN_UNAUTHORIZED);
		}
	}
}
