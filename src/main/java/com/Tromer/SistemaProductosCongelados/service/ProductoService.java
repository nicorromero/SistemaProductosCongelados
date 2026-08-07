package com.Tromer.SistemaProductosCongelados.service;

import com.Tromer.SistemaProductosCongelados.dto.ProductoDTO;
import com.Tromer.SistemaProductosCongelados.exception.ResourceNotFoundException;
import com.Tromer.SistemaProductosCongelados.model.Producto;
import com.Tromer.SistemaProductosCongelados.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    @Transactional
    public Producto crear(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setPrecio(dto.getPrecio());
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto producto = buscarPorId(id);
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setPrecio(dto.getPrecio());
        producto.setActivo(dto.isActivo());
        return productoRepository.save(producto);
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
