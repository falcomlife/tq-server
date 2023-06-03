package com.sorawingwind.storage.config.security;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyInvalidSessionStrategy implements InvalidSessionStrategy {

    private boolean createNewSession = true;

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (createNewSession) {
            request.getSession();
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(request.getServletPath());
        requestDispatcher.forward(request, response);
    }

    public void setCreateNewSession(boolean createNewSession) {
        this.createNewSession = createNewSession;
    }

}
