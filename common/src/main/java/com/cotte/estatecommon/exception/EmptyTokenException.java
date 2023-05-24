package com.cotte.estatecommon.exception;

import javax.servlet.ServletException;

public class EmptyTokenException extends ServletException {
    public EmptyTokenException() {
        super();
    }

    public EmptyTokenException(String s) {
        super(s);
    }
}
