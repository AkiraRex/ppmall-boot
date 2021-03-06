package com.ppmall.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;

@Controller
@RequestMapping("/error")
public class ErrorController {

	@RequestMapping("/notLogin")
	@ResponseBody
	public ServerResponse notLogin() {

		return ServerResponse.createErrorStatus(ResponseCode.NOT_LOGIN.getCode(), ResponseCode.NOT_LOGIN.getDesc());

	}
	
	@RequestMapping("/notAuth")
	@ResponseBody
	public ServerResponse notAuth() {

		return ServerResponse.createErrorStatus(20, "权限不足");

	}
}
