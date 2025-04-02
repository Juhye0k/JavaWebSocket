package me.juhye0k.dto;


import me.juhye0k.ExceptionType;


public class BusinessException extends RuntimeException{
    private final ExceptionType exceptionType;

    public BusinessException(ExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }
    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
