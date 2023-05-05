package com.cotte.estatecommon.utils;

import com.cotte.estatecommon.Subject;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;


public class JwtDecoder {


    public static String decode(String token) {

        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Key key = Keys.hmacShaKeyFor("AAAAB3NzaC1yc2EAAAADAQABAAABAQDN/TzzZi2SDi8HmXLWjpzD7HMpmGgfy6nDZ8eUJzhViL62SMKRmy/YMSQ+yUtbamIccOPQ3b5A1rOMJObFAOqOc7AgUBHzoJIHF0I3lwoXyalBOs7Kl5WPB9fvk6PpiYtXA3tRSr76RT2JhbJb0skLwViTZVFB4KuVFYXXZ+iHsBRY+6JuRqv7im0RRfqm6QLb8fBftPXucR8PCOGq36Qyj1H1xS2Fer1T9hHv2sEV692YuR3kQp6BMZfx7VWg47Vhfwg7qS9GXB04itMVqDVgypOCkQ6S+CudCFlPegIuk/tdy35tOnKNL5HStqylgBDp7alh4dEI3JM0exgPlLCJ".getBytes());

        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            //OK, we can trust this JWT
            return claims.get("userId").toString();
        } catch (SignatureException e) {
            //logger.warn("Json web token check failed. the signature check error. {}", e);
            return null;
            //don't trust the JWT!
        } catch (MalformedJwtException e) {
            //logger.warn("token format error. {}", e);
            return null;
        } catch (ExpiredJwtException e) {
            //logger.warn("token expired, {}", e);
            return null;
        } catch (Exception e) {
            //logger.warn("jwt check exception.  {}", e);
            return null;
        }
    }

    public static Map<String, String> decodeToMap(String token) {

        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Key key = Keys.hmacShaKeyFor("AAAAB3NzaC1yc2EAAAADAQABAAABAQDN/TzzZi2SDi8HmXLWjpzD7HMpmGgfy6nDZ8eUJzhViL62SMKRmy/YMSQ+yUtbamIccOPQ3b5A1rOMJObFAOqOc7AgUBHzoJIHF0I3lwoXyalBOs7Kl5WPB9fvk6PpiYtXA3tRSr76RT2JhbJb0skLwViTZVFB4KuVFYXXZ+iHsBRY+6JuRqv7im0RRfqm6QLb8fBftPXucR8PCOGq36Qyj1H1xS2Fer1T9hHv2sEV692YuR3kQp6BMZfx7VWg47Vhfwg7qS9GXB04itMVqDVgypOCkQ6S+CudCFlPegIuk/tdy35tOnKNL5HStqylgBDp7alh4dEI3JM0exgPlLCJ".getBytes());
        Map<String, String> result = new HashMap<String, String>();
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            //OK, we can trust this JWT
            result.put("tenantId", claims.get("tenantId", java.lang.String.class));
            result.put("userId", claims.getSubject());
            result.put("account", claims.get("account", java.lang.String.class));
            return result;
        } catch (SignatureException e) {
            //logger.warn("Json web token check failed. the signature check error. {}", e);
            return null;
            //don't trust the JWT!
        } catch (MalformedJwtException e) {
            //logger.warn("token format error. {}", e);
            return null;
        } catch (ExpiredJwtException e) {
            //logger.warn("token expired, {}", e);
            return null;
        } catch (Exception e) {
            //logger.warn("jwt check exception.  {}", e);
            return null;
        }
    }

    /**
     * 将token 转化为用户
     *
     * @param token
     * @return
     */
    public static Subject decodeToUser(String token) {
        Subject subject = new Subject();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Key key = Keys.hmacShaKeyFor("AAAAB3NzaC1yc2EAAAADAQABAAABAQDN/TzzZi2SDi8HmXLWjpzD7HMpmGgfy6nDZ8eUJzhViL62SMKRmy/YMSQ+yUtbamIccOPQ3b5A1rOMJObFAOqOc7AgUBHzoJIHF0I3lwoXyalBOs7Kl5WPB9fvk6PpiYtXA3tRSr76RT2JhbJb0skLwViTZVFB4KuVFYXXZ+iHsBRY+6JuRqv7im0RRfqm6QLb8fBftPXucR8PCOGq36Qyj1H1xS2Fer1T9hHv2sEV692YuR3kQp6BMZfx7VWg47Vhfwg7qS9GXB04itMVqDVgypOCkQ6S+CudCFlPegIuk/tdy35tOnKNL5HStqylgBDp7alh4dEI3JM0exgPlLCJ".getBytes());
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            //OK, we can trust this JWT
            subject.setTenantId(claims.get("tenantId", java.lang.String.class));
            subject.setUserId(claims.get("userId", java.lang.String.class));
            subject.setAccount(claims.get("account", java.lang.String.class));
            subject.setOrgId(claims.get("org", java.lang.String.class));
            return subject;
        } catch (SignatureException e) {
            //logger.warn("Json web token check failed. the signature check error. {}", e);
            return null;
            //don't trust the JWT!
        } catch (MalformedJwtException e) {
            //logger.warn("token format error. {}", e);
            return null;
        } catch (ExpiredJwtException e) {
            //logger.warn("token expired, {}", e);
            return null;
        } catch (Exception e) {
            //logger.warn("jwt check exception.  {}", e);
            return null;
        }
    }

}
