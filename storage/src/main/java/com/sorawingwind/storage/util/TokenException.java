package com.sorawingwind.storage.util;

/**
 * @ClassName
 * @description:
 * @author: tgt
 * @date: 2022/6/6
 */
public class TokenException extends Exception {
    public TokenException(){
        super("token 过期异常");
    }
}
