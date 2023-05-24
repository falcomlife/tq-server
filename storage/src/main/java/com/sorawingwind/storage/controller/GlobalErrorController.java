package com.sorawingwind.storage.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.cotte.estatecommon.enums.ErrorCode;
import com.cotte.estatecommon.exception.EmptyTokenException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalErrorController extends BasicErrorController {

    public GlobalErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.BINDING_ERRORS));
        try {
            Class exceptionClass = Class.forName((String) body.get("exception"));
            if (exceptionClass.equals(EmptyTokenException.class)) {
                Map<String, Object> map = new HashMap<>();
                map.put("s", 2);
                map.put("rs", body.get("message"));
                map.put("code", ErrorCode.EMPTYTOKEN.getIndex());
                return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
            } else if (exceptionClass.equals(TokenExpiredException.class)) {
                Map<String, Object> map = new HashMap<>();
                map.put("s", 2);
                map.put("rs", body.get("message"));
                map.put("code", ErrorCode.TOKENEXPIRED.getIndex());
                return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return super.error(request);
    }
}