package com.Tromer.SistemaProductosCongelados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter @Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El cliente es obligatorio")
    @Column(nullable = false)
    private String cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // --- Métodos de dominio ---

    public void transicionarA(EstadoPedido nuevoEstado) {
        if (!this.estado.puedeTransicionarA(nuevoEstado)) {
            throw new IllegalStateException(
                "Transición inválida: " + this.estado + " → " + nuevoEstado
            );
        }
        this.estado = nuevoEstado;
    }

    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = detalles.stream()
            .map(DetallePedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
