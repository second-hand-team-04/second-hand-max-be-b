package com.codesquad.secondhand.common.interceptor;

import java.util.Arrays;

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

		if (WhiteList.contains(request)) {
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

	enum WhiteList {
		SIGN_UP("/api/users", HttpMethod.POST),
		SIGN_IN("/api/auth", HttpMethod.POST);

		private final String url;
		private final HttpMethod httpMethod;

		WhiteList(String url, HttpMethod httpMethod) {
			this.url = url;
			this.httpMethod = httpMethod;
		}
		public static boolean contains(HttpServletRequest request) {
			return Arrays.stream(values())
				.anyMatch(w -> w.url.equals(request.getRequestURI()) &&
					w.httpMethod.name().equals(request.getMethod()));
		}
	}
}
