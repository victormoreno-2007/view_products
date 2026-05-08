package com.hexagonal.practica.infrastructure.adapters.in.web.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hexagonal.practica.domain.exception.BusinessErrorCode;
import com.hexagonal.practica.domain.exception.DomainException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handlerDomainException(DomainException ex){

        BusinessErrorCode Errorcode = ex.getBusinessErrorCode();

        HttpStatus status;

        switch (Errorcode) {
            case PRODUCT_NOT_FOUND:
            case USER_NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                break;
        
            case EMAIL_INVELID:
            case MESSAGE_NOT_EMPTY:
                status = HttpStatus.BAD_REQUEST; 
                break;
                
            case PASSWORD_INVALID:
                status = HttpStatus.UNAUTHORIZED; 
                break;
                
            default:
                status = HttpStatus.BAD_REQUEST; 
                break;
        }

        Map<String, String> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("error", "Conflicto de Reglas de Negocio");

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex){
        Map<String, String> response = new HashMap<>();

        response.put("message", "Ocurrió un error inesperado, por favor contacte al administrador ");

        response.put("Error", "Internal Server Error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityEception(DataIntegrityViolationException ex){
        Map<String, String> response = new HashMap<>();

        response.put("message", "No se puede eliminar este registro porque tiene historial asociado(userId)");

        response.put("Error", "Conflicto de Integración");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleArgumentInvalid(MethodArgumentNotValidException ex){
        Map<String, String> response = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {

            response.put(error.getField(), error.getDefaultMessage());
        });

        response.put("Error", "Entradas invalidas");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
}
