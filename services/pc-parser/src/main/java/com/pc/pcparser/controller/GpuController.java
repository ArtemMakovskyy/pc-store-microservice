package com.pc.pcparser.controller;

import com.pc.pcparser.service.userbenchmark.GpuUserBenchmarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GpuController {
    private final GpuUserBenchmarkService gpuUserBenchmarkService;

    @GetMapping("/gpus")
    public String listGpus(Model model) {
        model.addAttribute("gpus", gpuUserBenchmarkService.getAllWerePowerRequirementIsNull());
        return "gpus";
    }

    @PostMapping("/saveGpus")
    public String updateGpus(@RequestParam("gpuId") List<Long> gpuIds,
                             @RequestParam("powerRequirement") List<Integer> powerRequirements) {
        gpuUserBenchmarkService.updateGpusPowerRequirement(gpuIds, powerRequirements);
        return "redirect:/gpus";
    }
}
