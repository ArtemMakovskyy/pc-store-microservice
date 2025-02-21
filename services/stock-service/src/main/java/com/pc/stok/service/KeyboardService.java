package com.pc.stok.service;

import com.pc.stok.exception.ProductDataException;
import com.pc.stok.exception.ProductNotFoundException;
import com.pc.stok.model.Keyboard;
import com.pc.stok.repository.KeyboardRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeyboardService implements ProductService<Keyboard> {
    private final KeyboardRepository keyboardRepository;

    @Override
    public Keyboard save(Keyboard product) {
        if (product.getPartNumber() == null || product.getName() == null || product.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        return keyboardRepository.save(product);
    }

    @Override
    public Keyboard findById(Long id) {
        return keyboardRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Keyboard by id: " + id));
    }

    @Override
    public List<Keyboard> findAll() {
        return keyboardRepository.findAll();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!keyboardRepository.existsById(id)) {
            throw new ProductNotFoundException("Keyboard with id " + id + " does not exist");
        }
        keyboardRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public Keyboard update(Long id, Keyboard updatedKeyboard) {
        Keyboard existingKeyboard = findById(id);
        existingKeyboard.setPartNumber(updatedKeyboard.getPartNumber());
        existingKeyboard.setName(updatedKeyboard.getName());
        existingKeyboard.setDescription(updatedKeyboard.getDescription());
        existingKeyboard.setCostPrice(updatedKeyboard.getCostPrice());
        existingKeyboard.setSellingPrice(updatedKeyboard.getSellingPrice());
        existingKeyboard.setLayout(updatedKeyboard.getLayout());

        return keyboardRepository.save(existingKeyboard);
    }

    @Override
    @Transactional
    public Keyboard updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Keyboard keyboard = findById(id);
        keyboard.setSellingPrice(newSellingPrice);
        return keyboardRepository.save(keyboard);
    }
}
