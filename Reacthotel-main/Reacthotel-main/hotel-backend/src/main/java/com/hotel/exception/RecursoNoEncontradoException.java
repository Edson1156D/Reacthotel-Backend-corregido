package com.hotel.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException() {
        super("El recurso solicitado no fue encontrado");
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
