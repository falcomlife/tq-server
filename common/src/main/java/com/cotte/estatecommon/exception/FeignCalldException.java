package com.cotte.estatecommon.exception;

/*
*@description: 统一异常，用于feign接口调用时，目标接口出现异常时，返回异常信息。
*@author: sorawingwind
*@date: 2023/2/28
*/
public class FeignCalldException extends Exception  {

    public FeignCalldException() {
        super();
    }
    public FeignCalldException(String s) {
        super(s);
    }
}
