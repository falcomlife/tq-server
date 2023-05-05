package com.cotte.estatecommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


public class JWTUtil implements Serializable {

    /**
     * 生成令牌
     * @param secret JWT签名秘钥
     * @param expiration JWT过期时间
     * @param claims 私有数据声明
     * @return 令牌
     */
    public static String generateToken(String secret, String expiration, String subject, Map<String, Object> claims) throws Exception {
        String id = UUIDUtil.simpleUUid();
        long currentMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentMillis);
        Date expirationDate = new Date(currentMillis + Long.parseLong(expiration) * 1000);
        return Jwts.builder()
                .setClaims(claims) // 私有声明，一定要先设置私有声明，否则将会覆盖标准声明
                .setId(id) // JWT ID，JWT的唯一标识，主要用来作为一次性token，从而回避重放攻击
                .setSubject(subject) // JWT主题，即它的所有人，可以存放登录账号
                .setIssuedAt(currentDate) // JWT签发时间
                .setExpiration(expirationDate) // JWT过期时间
                .signWith(SignatureAlgorithm.HS512, MD5Util.MD5(secret)) // 签名算法和签名使用的秘钥
                .compact();
    }

    /**
     * 刷新令牌
     * @param token JWT令牌
     * @param secret JWT签名秘钥
     * @param expiration JWT过期时间
     * @return 令牌
     */
    public static String refreshToken(String secret, String token, String expiration) throws Exception {
        Claims claims = getClaimsFromToken(secret, token);

        long currentMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentMillis);
        Date expirationDate = new Date(currentMillis + Long.parseLong(expiration) * 1000);

        return Jwts.builder()
                .setClaims(claims) // 私有声明，一定要先设置私有声明，否则将会覆盖标准声明
                .setId(claims.getId()) // JWT ID，JWT的唯一标识，主要用来作为一次性token，从而回避重放攻击
                .setSubject(claims.getSubject()) // JWT主题，即它的所有人，可以存放登录账号
                .setIssuedAt(currentDate) // JWT签发时间
                .setExpiration(expirationDate) // JWT过期时间
                .signWith(SignatureAlgorithm.HS512, MD5Util.MD5(secret)) // 签名算法和签名使用的秘钥
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     * @param secret JWT签名秘钥
     * @param token JWT令牌
     * @return 私有数据声明
     */
    public static Claims getClaimsFromToken(String secret, String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(MD5Util.MD5(secret)) // 签名秘钥，和生成的签名秘钥一样
                .parseClaimsJws(token) // 需要解析的JWTToken
                .getBody(); // 获取私有数据声明
    }

    /**
     * 判断令牌是否过期
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String secret, String token) throws Exception {
        Claims claims = null;
        try {
            claims = getClaimsFromToken(secret, token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException jwte) {
            jwte.printStackTrace();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}