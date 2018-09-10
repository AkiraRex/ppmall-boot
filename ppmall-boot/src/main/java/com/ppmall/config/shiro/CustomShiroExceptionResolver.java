package com.ppmall.config.shiro;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ppmall.common.ServerResponse;

/**
 * 自定义异常处理
 * 
 * @author rex
 *
 */
public class CustomShiroExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		try {
			// TODO Auto-generated method stub
			ServerResponse serverResponse = ServerResponse.createErrorMessage("权限不足,请联系管理员");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(serverResponse.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
