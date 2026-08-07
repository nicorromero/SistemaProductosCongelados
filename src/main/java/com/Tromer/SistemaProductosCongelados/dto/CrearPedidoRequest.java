package com.Tromer.SistemaProductosCongelados.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoRequest {

    @NotBlank(message = "El cliente es obligatorio")
    private String cliente;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<DetallePedidoRequest> detalles;
}
