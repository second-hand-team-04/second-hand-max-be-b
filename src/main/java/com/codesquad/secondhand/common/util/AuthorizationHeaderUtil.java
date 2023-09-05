package com.codesquad.secondhand.common.util;

import javax.servlet.http.HttpServletRequest;

import com.codesquad.secondhand.common.exception.ErrorType;
import com.codesquad.secondhand.common.exception.auth.AuthUnauthorizedException;

public class AuthorizationHeaderUtil {

	public static String getToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");

		if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
			throw new AuthUnauthorizedException(ErrorType.AUTH_ACCESS_TOKEN_UNAUTHORIZED);
		}

		return authorization.split(" ")[1];
	}

	public static String getRefreshToken(String authorizationHeader) {
		if (!"bearer".equalsIgnoreCase(authorizationHeader.split(" ")[0])) {
			throw new AuthUnauthorizedException(ErrorType.AUTH_REFRESH_TOKEN_UNAUTHORIZED);
		}

		return authorizationHeader.split(" ")[1];
	}
}
