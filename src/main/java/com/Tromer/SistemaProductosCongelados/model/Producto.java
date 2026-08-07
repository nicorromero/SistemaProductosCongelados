package com.Tromer.SistemaProductosCongelados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private int stock;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean activo = true;

    @Version
    private Long version;

    // --- Métodos de dominio (Rich Domain Model) ---

    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }

    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser positiva");
        }
        if (!tieneStockSuficiente(cantidad)) {
            throw new IllegalArgumentException(
                "Stock insuficiente para '" + nombre +
                "'. Disponible: " + stock + ", Solicitado: " + cantidad
            );
        }
        this.stock -= cantidad;
    }

    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser positiva");
        }
        this.stock += cantidad;
    }
}
