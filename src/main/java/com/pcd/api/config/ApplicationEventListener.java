package com.pcd.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.pcd.api.util.AuthorizedCache;

@Component
public class ApplicationEventListener implements ApplicationListener<ApplicationReadyEvent> {
	@Value("${user.username}")
	private String username;
	@Value("${user.password}")
	private String password;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

		// TODO 内存数据库存储 Redis 、mangoDB
		// redis.set(username,password);
		// 模拟内存数据库
		AuthorizedCache.USER_MAP.put(username, password);
	}
}
