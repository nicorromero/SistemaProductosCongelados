package com.Tromer.SistemaProductosCongelados.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;

    private boolean activo;
}
