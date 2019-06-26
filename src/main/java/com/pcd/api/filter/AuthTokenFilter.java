package com.pcd.api.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTokenFilter extends AbstractAuthenticationProcessingFilter {
	Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	@Value("${token.header}")
	private String tokenHeader;

	@Value("${token.name}")
	private String tokenName;

	public AuthTokenFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse arg1)
			throws AuthenticationException, IOException, ServletException {
		String authToken = request.getHeader(this.tokenHeader);
		if (StringUtils.isEmpty(authToken)) {
			authToken = request.getParameter(this.tokenName);
		}
		logger.debug("start to check token:{} *************");
		if (authToken == null) {
			throw new AuthenticationCredentialsNotFoundException("Access Token is not provided");
		}
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authToken, null);
		return getAuthenticationManager().authenticate(authentication);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		String message;
		if (authException.getCause() != null) {
			message = authException.getCause().getMessage();
		} else {
			message = authException.getMessage();
		}

		// TODO 自定义错误消息
		Map map = new HashMap();
		map.put("status_code", "400");
		map.put("status_msg", message);

		byte[] body = new ObjectMapper().writeValueAsBytes(map);
		response.getOutputStream().write(body);
	}
}
