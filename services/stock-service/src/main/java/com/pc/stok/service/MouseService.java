package com.pc.stok.service;

import com.pc.stok.exception.ProductDataException;
import com.pc.stok.exception.ProductNotFoundException;
import com.pc.stok.model.Mouse;
import com.pc.stok.repository.MouseRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MouseService implements ProductService<Mouse> {
    private final MouseRepository mouseRepository;

    @Override
    public Mouse save(Mouse product) {
        if (product.getPartNumber() == null || product.getName() == null || product.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        return mouseRepository.save(product);
    }

    @Override
    public Mouse findById(Long id) {
        return mouseRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Mouse by id: " + id));
    }

    @Override
    public List<Mouse> findAll() {
        return mouseRepository.findAll();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!mouseRepository.existsById(id)) {
            throw new ProductNotFoundException("Mouse with id " + id + " does not exist");
        }
        mouseRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public Mouse update(Long id, Mouse updatedMouse) {
        Mouse existingMouse = findById(id);
        existingMouse.setPartNumber(updatedMouse.getPartNumber());
        existingMouse.setName(updatedMouse.getName());
        existingMouse.setDescription(updatedMouse.getDescription());
        existingMouse.setCostPrice(updatedMouse.getCostPrice());
        existingMouse.setSellingPrice(updatedMouse.getSellingPrice());
        return mouseRepository.save(existingMouse);
    }

    @Override
    @Transactional
    public Mouse updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Mouse mouse = findById(id);
        mouse.setSellingPrice(newSellingPrice);
        return mouseRepository.save(mouse);
    }
}
