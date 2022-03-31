package com.example.webpos.utils;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtil {

    @Cacheable(key = "#sessionId", cacheNames = "sessions")
    public HttpSession getSession(String sessionId, HttpServletRequest request) {
        System.out.println("get session");
        return request.getSession();
    }

    @CachePut(key = "#sessionId", cacheNames = "sessions")
    public HttpSession updateSession(String sessionId, HttpSession session) {
        return session;
    }

    public HttpSession updateSession(HttpSession session) {
        return updateSession(session.getId(), session);
    }

    public HttpSession getSession(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        return getSession(sessionId, request);
    }

    public String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("JSESSIONID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
