package com.Tromer.SistemaProductosCongelados.service;

import com.Tromer.SistemaProductosCongelados.dto.CrearPedidoRequest;
import com.Tromer.SistemaProductosCongelados.dto.DetallePedidoRequest;
import com.Tromer.SistemaProductosCongelados.exception.InsufficientStockException;
import com.Tromer.SistemaProductosCongelados.exception.ResourceNotFoundException;
import com.Tromer.SistemaProductosCongelados.model.*;
import com.Tromer.SistemaProductosCongelados.repository.PedidoRepository;
import com.Tromer.SistemaProductosCongelados.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    /**
     * Crea un pedido con reserva atómica de stock.
     * Si algún producto no tiene stock suficiente, se hace rollback completo.
     */
    @Transactional
    public Pedido crearPedido(CrearPedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setCliente(request.getCliente());
        pedido.setDireccion(request.getDireccion());

        for (DetallePedidoRequest detalleReq : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleReq.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", detalleReq.getProductoId()));

            // Descuento atómico a nivel SQL — previene over-selling
            int filasAfectadas = productoRepository.descontarStockAtomico(
                producto.getId(), detalleReq.getCantidad()
            );

            if (filasAfectadas == 0) {
                // Rollback automático: RuntimeException dentro de @Transactional
                throw new InsufficientStockException(
                    producto.getNombre(),
                    producto.getStock(),
                    detalleReq.getCantidad()
                );
            }

            DetallePedido detalle = new DetallePedido(pedido, producto, detalleReq.getCantidad());
            pedido.agregarDetalle(detalle);
        }

        pedido.transicionarA(EstadoPedido.RESERVADO);
        return pedidoRepository.save(pedido);
    }

    /**
     * Cancela un pedido y libera el stock reservado.
     */
    @Transactional
    public Pedido cancelarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        pedido.transicionarA(EstadoPedido.CANCELADO);

        // Liberar stock de todos los productos del pedido
        for (DetallePedido detalle : pedido.getDetalles()) {
            productoRepository.reponerStockAtomico(
                detalle.getProducto().getId(),
                detalle.getCantidad()
            );
        }

        return pedidoRepository.save(pedido);
    }

    /**
     * Avanza el pedido al siguiente estado lógico.
     */
    @Transactional
    public Pedido avanzarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(pedidoId);
        pedido.transicionarA(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}
