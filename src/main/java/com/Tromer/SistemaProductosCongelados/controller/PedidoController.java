package com.Tromer.SistemaProductosCongelados.controller;

import com.Tromer.SistemaProductosCongelados.dto.CrearPedidoRequest;
import com.Tromer.SistemaProductosCongelados.dto.PedidoResponse;
import com.Tromer.SistemaProductosCongelados.model.EstadoPedido;
import com.Tromer.SistemaProductosCongelados.model.Pedido;
import com.Tromer.SistemaProductosCongelados.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<PedidoResponse> response = pedidoService.listarTodos().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(pedidoService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody CrearPedidoRequest request) {
        Pedido pedido = pedidoService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(pedido));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(pedidoService.cancelarPedido(id)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido nuevoEstado) {
        return ResponseEntity.ok(toResponse(pedidoService.avanzarEstado(id, nuevoEstado)));
    }

    // --- Mapper privado ---

    private PedidoResponse toResponse(Pedido pedido) {
        List<PedidoResponse.DetalleResponse> detalles = pedido.getDetalles().stream()
            .map(d -> PedidoResponse.DetalleResponse.builder()
                .productoId(d.getProducto().getId())
                .productoNombre(d.getProducto().getNombre())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .build())
            .collect(Collectors.toList());

        return PedidoResponse.builder()
            .id(pedido.getId())
            .cliente(pedido.getCliente())
            .estado(pedido.getEstado().name())
            .direccion(pedido.getDireccion())
            .total(pedido.getTotal())
            .fechaCreacion(pedido.getFechaCreacion())
            .fechaActualizacion(pedido.getFechaActualizacion())
            .detalles(detalles)
            .build();
    }
}
