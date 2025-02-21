package com.pc.stok.service.impl;

import com.pc.stok.dto.CreateComputerDto;
import com.pc.stok.dto.ComputerDto;
import com.pc.stok.dto.mapper.ComputerMapper;
import com.pc.stok.exception.ProductDataException;
import com.pc.stok.exception.ProductNotFoundException;
import com.pc.stok.feignclient.PcProductFeignClient;
import com.pc.stok.model.Computer;
import com.pc.stok.repository.ComputerRepository;
import com.pc.stok.service.PcConfigService;
import com.pc.stok.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComputerService implements ProductService<CreateComputerDto, ComputerDto>, PcConfigService {

    private final ComputerRepository computerRepository;
    private final ComputerMapper computerMapper;
    private final PcProductFeignClient pcProductFeignClient;

    @Override
    public List<ComputerDto> addFromParser(List<String> partNumbers, BigDecimal percentageIncrease) {
        List<Computer> allComputerConfigs = pcProductFeignClient.getAll().stream()
                .map(computerMapper::configToEntity)
                .toList();

        List<Computer> matchingComputers = allComputerConfigs.stream()
                .filter(computer -> partNumbers.contains(computer.getPartNumber()))
                .toList();

        for (Computer computer : matchingComputers) {
            BigDecimal newPrice = computer.getSellingPrice()
                    .multiply(BigDecimal.ONE.add(percentageIncrease.divide(BigDecimal.valueOf(100))));
            computer.setSellingPrice(newPrice);
        }

        List<Computer> addedComputers = computerRepository.saveAll(matchingComputers);
        return addedComputers.stream()
                .map(computerMapper::toDto)
                .toList();
    }

    @Override
    public ComputerDto save(CreateComputerDto createDto) {
        if (createDto.getPartNumber() == null || createDto.getName() == null || createDto.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        final Computer computer = computerMapper.toEntity(createDto);
        return computerMapper.toDto(computerRepository.save(computer));
    }

    @Override
    public ComputerDto findById(Long id) {
        final Computer computer = computerRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Computer by id: " + id));

        return computerMapper.toDto(computer);
    }

    @Override
    public List<ComputerDto> findAll() {
        List<Computer> computers = computerRepository.findAll();
        return computers.stream()
                .map(computerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!computerRepository.existsById(id)) {
            throw new ProductNotFoundException("Computer with id " + id + " does not exist");
        }
        computerRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public ComputerDto update(Long id, CreateComputerDto createComputerDto) {
        Computer existingComputer = computerRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Computer by id: " + id));

        final Computer computer = computerMapper.toEntity(createComputerDto);
        existingComputer.setPartNumber(computer.getPartNumber());
        existingComputer.setName(computer.getName());
        existingComputer.setDescription(computer.getDescription());
        existingComputer.setCostPrice(computer.getCostPrice());
        existingComputer.setSellingPrice(computer.getSellingPrice());
        existingComputer.setCpu(computer.getCpu());
        existingComputer.setCpuUrl(computer.getCpuUrl());
        existingComputer.setMotherboard(computer.getMotherboard());
        existingComputer.setMotherboardUrl(computer.getMotherboardUrl());
        existingComputer.setMemory(computer.getMemory());
        existingComputer.setMemoryUrl(computer.getMemoryUrl());
        existingComputer.setGpu(computer.getGpu());
        existingComputer.setGpuUrl(computer.getGpuUrl());
        existingComputer.setSsd(computer.getSsd());
        existingComputer.setSsdUrl(computer.getSsdUrl());
        existingComputer.setPowerSupplier(computer.getPowerSupplier());
        existingComputer.setPowerSupplierUrl(computer.getPowerSupplierUrl());
        existingComputer.setPredictionFps(computer.getPredictionFps());
        existingComputer.setGamingScore(computer.getGamingScore());
        existingComputer.setPriceForFps(computer.getPriceForFps());

        return computerMapper.toDto(computerRepository.save(existingComputer));
    }

    @Override
    @Transactional
    public ComputerDto updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        final Computer computer = computerRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Computer by id: " + id));

        computer.setSellingPrice(newSellingPrice);
        return computerMapper.toDto(computerRepository.save(computer));
    }
}
