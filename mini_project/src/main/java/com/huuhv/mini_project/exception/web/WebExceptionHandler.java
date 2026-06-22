package com.huuhv.mini_project.exception.web;

import com.huuhv.mini_project.exception.DuplicateResourceException;
import com.huuhv.mini_project.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice(basePackages = "com.huuhv.mini_project.controller.web", annotations = Controller.class)
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        log.error("Resource not found: {}", ex.getMessage());
        return buildErrorView("error/404", "Resource not found", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ModelAndView handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        log.error("Resource conflict: {}", ex.getMessage());
        return buildErrorView("error/409", "Resource already exists", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return buildErrorView("error/500", "An unexpected error occurred", "Please try again later.", request.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        log.error("HTTP method not supported: {}", ex.getMessage());
        return buildErrorView("error/405", "HTTP method not supported", ex.getMessage(), request.getRequestURI());
    }

    private ModelAndView buildErrorView(String viewName, String title, String message, String path) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("title", title);
        modelAndView.addObject("message", message);
        modelAndView.addObject("path", path);
        return modelAndView;
    }
}

