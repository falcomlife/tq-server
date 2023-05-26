package com.sorawingwind.storage.config.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class JWTUtils {
    public static String TOKEN_HEADER = "token";

    //过期时间
    private static int EXPIRITION_DAY =7;

    private static String ROLE = "role";

    private static String SIGN = "#N!&SI#^";

    public static String createToken(String username, List<AuthorityDo> authorityDos){
        ArrayList<String> authorityList = new ArrayList<>();
        for(AuthorityDo authorityDo:authorityDos){
            authorityList.add(authorityDo.getCode());
        }
        Calendar instance = Calendar.getInstance();
        //过期时间设为7天
        instance.add(Calendar.DATE,EXPIRITION_DAY);
        String token = JWT.create()
                .withArrayClaim("authorities",authorityList.toArray(new String[0]))
                .withClaim("username",username)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    public static HashMap<String,Object> decode(String token){
        HashMap<String, Object> map = new HashMap<>();
        DecodedJWT verify;
        try{
            verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        }catch (Exception e){
            throw new TokenExpiredException("凭证过期，请重新登录。");
        }
        String username = verify.getClaim("username").asString();
        String[] authorities = verify.getClaim("authorities").asArray(String.class);
        map.put("username",username);
        map.put("authorities",authorities);
        return map;
    }

    public static void setExpiritionDay(int expiritionDay) {
        EXPIRITION_DAY = expiritionDay;
    }

    public static void setRole(String role) {
        JWTUtils.ROLE = role;
    }

    public static void setSign(String sign) {
        JWTUtils.SIGN = sign;
    }
}

