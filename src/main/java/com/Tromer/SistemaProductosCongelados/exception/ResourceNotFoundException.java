package com.Tromer.SistemaProductosCongelados.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String recurso, Long id) {
        super(String.format("%s con ID %d no encontrado", recurso, id));
    }
}
