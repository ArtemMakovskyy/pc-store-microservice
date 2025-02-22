package com.pc.product.service.impl;

import com.pc.product.dto.CreateKeyboardDto;
import com.pc.product.dto.KeyboardDto;
import com.pc.product.dto.mapper.KeyboardMapper;
import com.pc.product.exception.ProductDataException;
import com.pc.product.exception.ProductNotFoundException;
import com.pc.product.model.Keyboard;
import com.pc.product.repository.KeyboardRepository;
import com.pc.product.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeyboardService implements ProductService<CreateKeyboardDto, KeyboardDto> {

    private final KeyboardRepository keyboardRepository;
    private final KeyboardMapper keyboardMapper;

    @Override
    public KeyboardDto save(CreateKeyboardDto createDto) {
        if (createDto.getPartNumber() == null || createDto.getName() == null || createDto.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        final Keyboard keyboard = keyboardMapper.toEntity(createDto);
        return keyboardMapper.toDto(keyboardRepository.save(keyboard));
    }

    @Override
    public KeyboardDto findById(Long id) {
        return keyboardRepository.findById(id)
                .map(keyboardMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Keyboard by id: " + id));
    }

    @Override
    public List<KeyboardDto> findAll() {
        List<Keyboard> keyboards = keyboardRepository.findAll();
        return keyboards.stream()
                .map(keyboardMapper::toDto)
                .toList();
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
    public KeyboardDto update(Long id, CreateKeyboardDto createKeyboardDto) {
        Keyboard existingKeyboard = keyboardRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Keyboard by id: " + id));

        final Keyboard keyboard = keyboardMapper.toEntity(createKeyboardDto);
        existingKeyboard.setPartNumber(keyboard.getPartNumber());
        existingKeyboard.setName(keyboard.getName());
        existingKeyboard.setDescription(keyboard.getDescription());
        existingKeyboard.setCostPrice(keyboard.getCostPrice());
        existingKeyboard.setSellingPrice(keyboard.getSellingPrice());

        return keyboardMapper.toDto(keyboardRepository.save(existingKeyboard));
    }

    @Override
    @Transactional
    public KeyboardDto updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Keyboard keyboard = keyboardRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Keyboard by id: " + id));

        keyboard.setSellingPrice(newSellingPrice);
        return keyboardMapper.toDto(keyboardRepository.save(keyboard));
    }
}
