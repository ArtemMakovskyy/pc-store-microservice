package com.pc.stok.controller;

import com.pc.stok.service.PcProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final PcProductService pcProductService;

    @GetMapping
    public Integer getInt(){
      return   pcProductService.getAll().size();
    }

    @GetMapping("/s")
    public Integer getInt2(){
        return   pcProductService.getAllByBestPrice().size();
    }
}
