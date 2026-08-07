package com.Tromer.SistemaProductosCongelados.exception;

import com.Tromer.SistemaProductosCongelados.model.EstadoPedido;

public class InvalidStateTransitionException extends RuntimeException {

    public InvalidStateTransitionException(EstadoPedido estadoActual, EstadoPedido estadoDestino) {
        super(String.format(
            "Transición de estado inválida: %s → %s",
            estadoActual, estadoDestino
        ));
    }
}
