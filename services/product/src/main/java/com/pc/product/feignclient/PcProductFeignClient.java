package com.pc.product.feignclient;

import com.pc.product.dto.PcConfigDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PC-CONFIG-PARSER-SERVICE")
public interface PcProductFeignClient {

    @GetMapping("/pcs")
    public List<PcConfigDto> getAll();

    @GetMapping("/pcs/price/best")
    public List<PcConfigDto> getAllByBestPrice();
}
