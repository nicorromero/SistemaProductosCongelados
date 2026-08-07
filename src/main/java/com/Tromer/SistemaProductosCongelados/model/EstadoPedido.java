package com.Tromer.SistemaProductosCongelados.model;

import java.util.Set;

public enum EstadoPedido {

    PENDIENTE(Set.of()),
    RESERVADO(Set.of()),
    PAGO_REALIZADO(Set.of()),
    EN_PREPARACION(Set.of()),
    ENTREGADO(Set.of()),
    CANCELADO(Set.of()),
    RECHAZADO_SIN_STOCK(Set.of());

    private Set<EstadoPedido> transicionesPermitidas;

    EstadoPedido(Set<EstadoPedido> transicionesPermitidas) {
        this.transicionesPermitidas = transicionesPermitidas;
    }

    static {
        PENDIENTE.transicionesPermitidas = Set.of(RESERVADO, RECHAZADO_SIN_STOCK, CANCELADO);
        RESERVADO.transicionesPermitidas = Set.of(PAGO_REALIZADO, CANCELADO);
        PAGO_REALIZADO.transicionesPermitidas = Set.of(EN_PREPARACION, CANCELADO);
        EN_PREPARACION.transicionesPermitidas = Set.of(ENTREGADO);
        ENTREGADO.transicionesPermitidas = Set.of();
        CANCELADO.transicionesPermitidas = Set.of();
        RECHAZADO_SIN_STOCK.transicionesPermitidas = Set.of();
    }

    public boolean puedeTransicionarA(EstadoPedido destino) {
        return this.transicionesPermitidas.contains(destino);
    }

    public boolean esEstadoFinal() {
        return this.transicionesPermitidas.isEmpty();
    }

    public boolean requiereDevolucionStock() {
        return this == CANCELADO;
    }
}
