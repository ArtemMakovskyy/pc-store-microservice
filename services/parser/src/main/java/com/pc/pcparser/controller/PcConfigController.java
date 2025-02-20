package com.pc.pcparser.controller;

import com.pc.pcparser.dto.PcConfigDto;
import com.pc.pcparser.service.PcConfigService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pcs")
public class PcConfigController {
    private final PcConfigService pcConfigService;

    @GetMapping
    public List<PcConfigDto> getAll(){
        return pcConfigService.getAllPcConfigDto();
    }

    @GetMapping("/price/best")
    public List<PcConfigDto> getAllByBestPrice(){
        return pcConfigService.getAllPcConfigDtoByBestPrice();
    }

}
