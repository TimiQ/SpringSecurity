package com.pcd.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcd.api.service.TokenService;
import com.pcd.api.util.DataResponse;

@RestController
public class TestController {
	@Autowired
	TokenService tokenService;

	@PostMapping("/login")
	public DataResponse<String> login(@RequestParam(value = "userName",required=false) String userName,
			@RequestParam(value = "password",required=false) String password) {
		return tokenService.login(userName, password);
	}

	@PostMapping("/service/findUserList")
	public DataResponse<String> findUserList() {
		DataResponse dr = new DataResponse();

		dr.setStatusCode("200");
		dr.setStatusMsg("测试请求成功");

		return dr;
	}

}
