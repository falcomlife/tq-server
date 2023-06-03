package com.sorawingwind.storage.config.security;

import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "登录成功");
        result.put("status", 200);
        String username = authentication.getName();
        List<AuthorityDo> authorityDos = authentication.getAuthorities().stream().map(item -> {
            AuthorityDo authorityDo = new AuthorityDo();
            authorityDo.setCode(item.getAuthority());
            return authorityDo;
        }).collect(Collectors.toList());
        result.put("token", JWTUtils.createToken(username.split("/")[0],username.split("/")[1],username.split("/")[2],authorityDos));
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(s);
    }
}

