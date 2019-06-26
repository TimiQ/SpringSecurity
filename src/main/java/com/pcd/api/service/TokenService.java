package com.pcd.api.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pcd.api.token.JavaWebToken;
import com.pcd.api.util.AuthorizedCache;
import com.pcd.api.util.DataResponse;

@Service
public class TokenService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean checkToken(String token) {
		// TODO 内存数据库读取数据
		// String toeknRe=(String)redis.get(token);
		String toeknRe = AuthorizedCache.REDIS_MAP.get(token);
		if (StringUtils.isNotBlank(toeknRe) && token.equals(toeknRe)) {
			return true;
		} else {
			return false;
		}
	}

	public DataResponse<String> login(String username, String password) {
		DataResponse<String> response = new DataResponse<String>();
		// TODO 从内存数据库中取数据
		// String pass=(String)redis.get(username);
		String pass = AuthorizedCache.USER_MAP.get(username);
		if (StringUtils.isNotBlank(pass) && pass.equals(password)) {
			logger.info("login success" + username);
			Map map = new HashMap<>();
			map.put("id", 120);
			String token = JavaWebToken.createJavaWebToken(map);
			// TODO 存入内存数据库
			// redis.set(token,token);
			AuthorizedCache.REDIS_MAP.put(token, token);
			response.setValue(token);
		} else {
			response.setStatusCode("401");
			response.setStatusMsg("用戶名或者密碼錯誤");
		}
		return response;
	}
}
