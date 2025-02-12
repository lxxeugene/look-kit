package com.example.lookkit.common.exception;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//모든 컨트롤러에서 발생하는 예외를 전역으로 처리함
@ControllerAdvice
public class GlobalExceptionHandler {
    // UserNotFoundException 발생 시 처리
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorResponse("404", ex.getMessage());
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex) {
        ex.getMessage();
        return new ErrorResponse("500", "서버 내부 오류가 발생했습니다.");
    }

    // MyBatisSystemException 처리
    @ExceptionHandler(MyBatisSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleMyBatisException(MyBatisSystemException ex) {
        ex.getMessage();
        ex.printStackTrace();
        return new ErrorResponse("500", "데이터베이스 처리 중 오류가 발생했습니다.");
    }

}
