package com.netcoretech.netfaulttracker.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", "예기치 않은 오류가 발생했습니다: " + e.getMessage());
        return "error";
    }

    // 404 오류 발생 시, 페이지를 찾을 수 없다는 메시지를 에러 페이지로 전달.
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException e, Model model) {
        model.addAttribute("error", "요청하신 페이지를 찾을 수 없습니다.");
        return "error";
    }
}