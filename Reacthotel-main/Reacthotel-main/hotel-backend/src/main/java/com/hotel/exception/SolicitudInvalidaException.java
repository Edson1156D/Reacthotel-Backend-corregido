package com.hotel.exception;

public class SolicitudInvalidaException extends RuntimeException {

    public SolicitudInvalidaException() {
        super("La solicitud no es válida");
    }

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}
