package com.Tromer.SistemaProductosCongelados.repository;

import com.Tromer.SistemaProductosCongelados.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    /**
     * Descuento atómico de stock a nivel SQL.
     * Retorna el número de filas afectadas (0 = stock insuficiente).
     * La cláusula WHERE stock >= :cantidad previene sobre-venta
     * incluso bajo concurrencia extrema.
     */
    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :id AND p.stock >= :cantidad")
    int descontarStockAtomico(@Param("id") Long id, @Param("cantidad") int cantidad);

    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock + :cantidad WHERE p.id = :id")
    int reponerStockAtomico(@Param("id") Long id, @Param("cantidad") int cantidad);
}
