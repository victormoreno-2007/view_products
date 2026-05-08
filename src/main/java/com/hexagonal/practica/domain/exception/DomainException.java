package com.hexagonal.practica.domain.exception;

public class DomainException extends RuntimeException{

    private final BusinessErrorCode businessErrorCode;
    
    public DomainException(BusinessErrorCode errorCode) {
        super(errorCode.getReason());
        this.businessErrorCode = errorCode;
    }

    public DomainException(BusinessErrorCode errorCode, Throwable cause) {
        super(errorCode.getReason(), cause);
        this.businessErrorCode = errorCode;
    }

        
    public BusinessErrorCode getBusinessErrorCode() {
        return businessErrorCode;
    }

}
