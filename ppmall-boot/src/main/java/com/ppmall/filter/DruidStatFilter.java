package com.ppmall.filter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties.WebStatFilter;

@WebFilter(filterName = "druidWebStatFilter", urlPatterns = { "/*" }, initParams = {
		@WebInitParam(name = "exclusions", value = "*.js,*.jpg,*.png,*.gif,*.ico,*.css,/druid/*")// 配置本过滤器放行的请求后缀
})
public class DruidStatFilter extends WebStatFilter {
}