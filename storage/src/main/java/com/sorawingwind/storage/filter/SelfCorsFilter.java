package com.sorawingwind.storage.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
@Order(2)
public class SelfCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //解决跨域的问题
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "false");
        resp.setHeader("Access-Control-Allow-Headers", "username,Content-Type,Content-Length, Authorization, Accept, X-Requested-With, X-App-Id, token, Cache-Control, Authorization, sessionId, lan");
        resp.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
        resp.setHeader("Access-Control-Max-Age", "3600");
        chain.doFilter(req, resp);
    }

}