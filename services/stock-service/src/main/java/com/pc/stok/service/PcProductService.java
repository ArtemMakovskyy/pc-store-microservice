package com.pc.stok.service;

import com.pc.stok.dto.PcConfigDto;
import com.pc.stok.feignclient.PcProductFeignClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PcProductService {
    private final PcProductFeignClient pcProductFeignClient;

    public List<PcConfigDto> getAll() {
        return pcProductFeignClient.getAll();
    }

    public List<PcConfigDto> getAllByBestPrice() {
        return pcProductFeignClient.getAllByBestPrice();
    }

}
