package com.sorawingwind.storage.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyInvalidSessionStrategy implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        // 401
        result.put("status", HttpStatus.UNAUTHORIZED.value());
        result.put("msg", "当前会话已经过期,请重新登录");
        String s = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(s);
    }
}
