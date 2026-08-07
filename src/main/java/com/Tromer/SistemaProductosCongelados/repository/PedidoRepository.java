package com.Tromer.SistemaProductosCongelados.repository;

import com.Tromer.SistemaProductosCongelados.model.EstadoPedido;
import com.Tromer.SistemaProductosCongelados.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByClienteContainingIgnoreCase(String cliente);
}
