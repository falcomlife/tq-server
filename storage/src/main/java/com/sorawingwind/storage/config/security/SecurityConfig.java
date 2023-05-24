package com.sorawingwind.storage.config.security;

import com.sorawingwind.storage.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailService;

    //构造注入自定义UserDetailService
    @Autowired
    public SecurityConfig(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    //注入AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //注入过滤器
    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setPasswordParameter("password");
        loginFilter.setUsernameParameter("username");
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        loginFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return loginFilter;
    }

    //配置添加自定义认证数据源，替换过滤器
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 鉴权过滤
        http.authorizeHttpRequests().antMatchers("/static/**").permitAll()
                .and().formLogin()
                // 登出逻辑处理
                .and().logout().logoutSuccessHandler(new MyLogoutSuccessHandler())
                // 会话过期处理
                .and().sessionManagement().invalidSessionStrategy(new MyInvalidSessionStrategy())
                // 登录用户账号密码匹配处理
                .and().userDetailsService(userDetailService)
                .csrf().disable()
                // 跨域处理
                .cors().configurationSource(configurationSource());
        // 登录认证处理
        http.addFilterAt(loginFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class);
        // token解析和处理
        http.addFilter(jwtAuthorizationFilter(http.getSharedObject(AuthenticationManager.class)));
        // 401,403情况统一处理
        http.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());
        return http.build();
    }

    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        JWTAuthorizationFilter filter = new JWTAuthorizationFilter(authenticationManager);
        return filter;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


}
