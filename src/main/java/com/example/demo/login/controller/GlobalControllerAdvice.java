package com.example.demo.login.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalControllerAdvice {
	
	@ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(Model model, DataAccessException e) {
    	model.addAttribute("error", "内部サーバーエラー：DataAccessException");
    	model.addAttribute("message", "DataAccessExceptionが発生しました");
    	model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
    	
    	return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        model.addAttribute("error", "内部サーバーエラー：ExceptionHandler");
        model.addAttribute("message", "Exceptionが発生しました");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        
        return "error";
    }
}
