package com.pc.stok.service;

import com.pc.stok.dto.PcConfigDto;
import com.pc.stok.dto.PcProductDto;
import com.pc.stok.dto.mapper.PcProductMapper;
import com.pc.stok.feignclient.PcProductFeignClient;
import com.pc.stok.model.PcProduct;
import com.pc.stok.repository.PcProductRepository;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PcProductService {
    private final PcProductFeignClient pcProductFeignClient;
    private final PcProductMapper pcProductMapper;
    private final PcProductRepository pcProductRepository;

    public List<PcConfigDto> getAll() {
        return pcProductFeignClient.getAll();
    }

    public List<PcConfigDto> getAllByBestPrice() {
        return pcProductFeignClient.getAllByBestPrice();
    }

    public List<PcProductDto> addToStock(){
        final List<PcProduct> list = getAllByBestPrice().stream()
                .map(pcProductMapper::toEntity)
                .limit(5)
                .toList();

        pcProductRepository.saveAll(list);
        return Collections.emptyList();
    }

}
