package com.sorawingwind.storage.controller.exception;

import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.exception.DeletingObjectIsUsedException;
import com.cotte.estatecommon.exception.EmptyTokenException;
import com.cotte.estatecommon.exception.FeignCalldException;
import com.cotte.estatecommon.exception.ObjectExsitException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;

import static com.cotte.estatecommon.enums.ErrorCode.ACCESSDENIED;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public RS accessDeniedException(HttpServletResponse response, AccessDeniedException e) {
        log.error("ERROR MESSAGE: 权限不足\n{}", ExceptionUtils.getFullStackTrace(e));
        RS rs = RS.bad(e.getMessage());
        rs.setCode(ACCESSDENIED);
        return rs;
    }

    @ResponseBody
    @ExceptionHandler(DeletingObjectIsUsedException.class)
    public RS deletingObjectIsUsedException(HttpServletResponse response, DeletingObjectIsUsedException e) {
        log.error("ERROR MESSAGE: {}\n{}", e.getMessage(), ExceptionUtils.getFullStackTrace(e));
        return RS.bad(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(FeignCalldException.class)
    public RS FeignCalldException(HttpServletResponse response, FeignCalldException e) {
        log.error("ERROR MESSAGE: {}\n{}", e.getMessage(), ExceptionUtils.getFullStackTrace(e));
        return RS.bad(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ObjectExsitException.class)
    public RS ObjectExsitException(HttpServletResponse response, ObjectExsitException e) {
        log.error("ERROR MESSAGE: {}\n{}", e.getMessage(), ExceptionUtils.getFullStackTrace(e));
        return RS.bad(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RS BindException(HttpServletResponse response, MethodArgumentNotValidException e) {
        log.error("ERROR MESSAGE: {}\n{}", e.getMessage(), ExceptionUtils.getFullStackTrace(e));
        System.out.println(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return RS.bad("您所提交的”"+e.getBindingResult().getAllErrors().get(0).getDefaultMessage()+"“参数数据异常。");
    }

    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RS fileSizeException(HttpServletResponse response, MaxUploadSizeExceededException e) {
        log.error("ERROR MESSAGE: 文件大小超过限制\n{}", ExceptionUtils.getFullStackTrace(e));
        return RS.bad("文件大小超过512KB限制。");
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RS globalException(HttpServletResponse response, Exception e) {
        log.error("ERROR MESSAGE: 未定义系统异常\n{}", ExceptionUtils.getFullStackTrace(e));
        return RS.bad("服务器处理异常。");
    }

}