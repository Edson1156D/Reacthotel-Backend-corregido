package com.hotel.exception;

public class ConflictoException extends RuntimeException {

    public ConflictoException() {
        super("La operación no puede completarse por un conflicto");
    }

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
