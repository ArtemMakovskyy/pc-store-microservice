package com.pc.stok.service;

import com.pc.stok.dto.ComputerDto;
import com.pc.stok.dto.mapper.ComputerMapper;
import com.pc.stok.exception.ProductDataException;
import com.pc.stok.exception.ProductNotFoundException;
import com.pc.stok.feignclient.PcProductFeignClient;
import com.pc.stok.model.Computer;
import com.pc.stok.repository.ComputerRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class ComputerService implements ProductService<Computer>, PcConfigService {
    private final ComputerRepository computerRepository;
    private final ComputerMapper computerMapper;
    private final PcProductFeignClient pcProductFeignClient;

    public void init() {
        final BigDecimal bigDecimal = BigDecimal.valueOf(1.2);
        List<String> partNumbers = new ArrayList<>();
        partNumbers.add("4804521022025");
        partNumbers.add("4792821022025");
        partNumbers.add("4702121022025");
        restockConfigurations(partNumbers, bigDecimal);
    }

    @Override
    public List<ComputerDto> restockConfigurations(List<String> partNumbers, BigDecimal percentageIncrease) {

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
    public Computer save(Computer product) {
        if (product.getPartNumber() == null || product.getName() == null || product.getSellingPrice() == null) {
            throw new ProductDataException("Part number, name, and selling price must not be null");
        }
        return computerRepository.save(product);
    }

    @Override
    public Computer findById(Long id) {
        return computerRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Can't find Computer by id: " + id));
    }

    @Override
    public List<Computer> findAll() {
        List<Computer> computers = computerRepository.findAll();
        if (computers.isEmpty()) {
            throw new ProductNotFoundException("No computers found");
        }
        return computers;
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
    public Computer update(Long id, Computer updatedComputer) {
        Computer existingComputer = findById(id);

        existingComputer.setPartNumber(updatedComputer.getPartNumber());
        existingComputer.setName(updatedComputer.getName());
        existingComputer.setDescription(updatedComputer.getDescription());
        existingComputer.setCostPrice(updatedComputer.getCostPrice());
        existingComputer.setSellingPrice(updatedComputer.getSellingPrice());
        existingComputer.setCpu(updatedComputer.getCpu());
        existingComputer.setCpuUrl(updatedComputer.getCpuUrl());
        existingComputer.setMotherboard(updatedComputer.getMotherboard());
        existingComputer.setMotherboardUrl(updatedComputer.getMotherboardUrl());
        existingComputer.setMemory(updatedComputer.getMemory());
        existingComputer.setMemoryUrl(updatedComputer.getMemoryUrl());
        existingComputer.setGpu(updatedComputer.getGpu());
        existingComputer.setGpuUrl(updatedComputer.getGpuUrl());
        existingComputer.setSsd(updatedComputer.getSsd());
        existingComputer.setSsdUrl(updatedComputer.getSsdUrl());
        existingComputer.setPowerSupplier(updatedComputer.getPowerSupplier());
        existingComputer.setPowerSupplierUrl(updatedComputer.getPowerSupplierUrl());
        existingComputer.setPredictionFps(updatedComputer.getPredictionFps());
        existingComputer.setGamingScore(updatedComputer.getGamingScore());
        existingComputer.setPriceForFps(updatedComputer.getPriceForFps());

        return computerRepository.save(existingComputer);
    }

    @Override
    @Transactional
    public Computer updateSellingPrice(Long id, BigDecimal newSellingPrice) {
        if (newSellingPrice == null || newSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductDataException("Selling price must be greater than zero");
        }
        Computer computer = findById(id);
        computer.setSellingPrice(newSellingPrice);
        return computerRepository.save(computer);
    }

}
