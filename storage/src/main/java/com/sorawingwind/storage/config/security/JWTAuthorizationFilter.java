package com.sorawingwind.storage.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.cotte.estatecommon.exception.EmptyTokenException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //从请求头中获取token
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if(uri.startsWith("/images/") || uri.startsWith("/web/") || uri.equals("/outStorage/image") || uri.equals("/favicon.ico") || (uri.equals("/user") && method.equals("POST"))){
            chain.doFilter(request,response);
            return;
        }
        String token = request.getHeader(JWTUtils.TOKEN_HEADER);
        if(StringUtils.isBlank(token)){
            throw new EmptyTokenException("请求凭证为空，请重新登录。");
        }
        //将token中的用户名和权限用户组放入Authentication对象,在之后实现鉴权
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        super.doFilterInternal(request, response, chain);
    }

    //解析token获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        HashMap<String, Object> tokenInfo = JWTUtils.decode(token);
        String username = (String) tokenInfo.get("username");
        String[] authoritiesArr = (String[]) tokenInfo.get("authorities");
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(String authority:authoritiesArr){
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return new UsernamePasswordAuthenticationToken(username,null,authorities);

    }
}
