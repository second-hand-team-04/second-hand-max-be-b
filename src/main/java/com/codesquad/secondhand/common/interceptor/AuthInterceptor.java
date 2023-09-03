package com.codesquad.secondhand.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codesquad.secondhand.auth.domain.JwtTokenProvider;
import com.codesquad.secondhand.common.exception.auth.AuthenticationException;

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

		try {
			String authorization = request.getHeader("Authorization");
			if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
				throw new AuthenticationException();
			}

			String token = authorization.split(" ")[1];
			jwtTokenProvider.getAccount(token);
			return true;
		} catch (RuntimeException e) {
			throw new AuthenticationException();
		}
	}
}
