package com.academia.app.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component()
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // No es css ni js ni imagen
        if (!request.getRequestURI().startsWith("/css") && !request.getRequestURI().startsWith("/js") && !request.getRequestURI().startsWith("/img")) {
            if (!request.getRequestURI().endsWith("/login") && request.getSession().getAttribute("USUARIO") == null) {
                // Si quiero ir a una página distinta de login y no tengo sesión redirijo a login

                response.sendRedirect("/login");
                return;
            } else if (request.getRequestURI().endsWith("/login") && request.getSession().getAttribute("USUARIO") != null) {
                //Si quiero ir a login y ya tengo sesión voy al inicio

                response.sendRedirect("/");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
