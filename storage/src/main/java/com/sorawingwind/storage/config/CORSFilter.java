package com.sorawingwind.storage.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @ClassName: CORSFilter
 * @Description: 跨域AJAX请求
 * @author: zhouyj
 * @date: 2017年12月22日 下午1:08:55
 *
 */
@Order(1)
//重点
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
public class CORSFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type,token");
		response.setHeader("Content-Type", "application/x-www-form-urlencoded");
		if(((HttpServletRequest)req).getMethod().equals(RequestMethod.OPTIONS.name())) {
			 response.getWriter().write("");
		}else {
			chain.doFilter(req, res);
		}
	}
	@Override
	public void init(FilterConfig filterConfig) {
	}
	@Override
	public void destroy() {
	}
}
