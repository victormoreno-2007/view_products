package com.hexagonal.practica.domain.exception;

public enum BusinessErrorCode {
    PRODUCT_NOT_FOUND("El producto no fúe encontrado."),
    EMAIL_INVELID("Email invalido, por favor verifica"),
    MESSAGE_NOT_EMPTY("Este campo no puede ser vacío"),
    PASSWORD_INVALID("Error al ingresar la contraseña, por favor verifica"),
    USER_NOT_FOUND("El usuario no fue encontrado."),
    ACCESS_DENEGATE("No puedes realizar acción porque no tenienes permiso");
    private String reason;

    BusinessErrorCode(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }

}
