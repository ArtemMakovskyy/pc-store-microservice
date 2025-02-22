package com.pc.product.service.impl;

import com.pc.product.dto.CreateMouseDto;
import com.pc.product.dto.MouseDto;
import com.pc.product.dto.mapper.MouseMapper;
import com.pc.product.exception.ProductDataException;
import com.pc.product.exception.ProductNotFoundException;
import com.pc.product.model.Mouse;
import com.pc.product.repository.MouseRepository;
import com.pc.product.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MouseService implements ProductService<CreateMouseDto, MouseDto> {
    private final MouseRepository mouseRepository;
    private final MouseMapper mouseMapper;



    @Override
    public MouseDto save(CreateMouseDto createDto) {
        if (createDto.getPartNumber() == null || createDto.getName() == null || createDto.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        final Mouse mouse = mouseMapper.toEntity(createDto);
        return mouseMapper.toDto(mouseRepository.save(mouse));
    }

    @Override
    public MouseDto findById(Long id) {
        return mouseRepository.findById(id)
                .map(mouseMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Mouse by id: " + id));
    }

    @Override
    public List<MouseDto> findAll() {
        List<Mouse> mice = mouseRepository.findAll();
        return mice.stream()
                .map(mouseMapper::toDto)
                .toList();
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
    public MouseDto update(Long id, CreateMouseDto createMouseDto) {
        Mouse existingMouse = mouseRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Mouse by id: " + id));

        final Mouse mouse = mouseMapper.toEntity(createMouseDto);
        existingMouse.setPartNumber(mouse.getPartNumber());
        existingMouse.setName(mouse.getName());
        existingMouse.setDescription(mouse.getDescription());
        existingMouse.setCostPrice(mouse.getCostPrice());
        existingMouse.setSellingPrice(mouse.getSellingPrice());

        return mouseMapper.toDto(mouseRepository.save(existingMouse));
    }

    @Override
    @Transactional
    public MouseDto updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Mouse mouse = mouseRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Mouse by id: " + id));

        mouse.setSellingPrice(newSellingPrice);
        return mouseMapper.toDto(mouseRepository.save(mouse));
    }
}
