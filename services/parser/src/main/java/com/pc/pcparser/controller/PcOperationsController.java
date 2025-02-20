package com.pc.pcparser.controller;

import com.pc.pcparser.service.PcConfigService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/operations")
@RequiredArgsConstructor
@Log4j2
public class PcOperationsController {

    private final PcConfigService pcConfigService;

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping
    public String showOperationsPage() {
        return "operations";
    }

    @PostMapping("/execute")
    @ResponseBody
    public Map<String, Object> executeOperation(
            @RequestParam(name = "updateUserBenchmarkCpu", required = false)
            boolean updateUserBenchmarkCpu,
            @RequestParam(name = "updateUserBenchmarkGpu", required = false)
            boolean updateUserBenchmarkGpu,
            @RequestParam(name = "updateHotline", required = false)
            boolean updateHotline,
            @RequestParam(name = "createPcList", required = false)
            boolean createPcList,
            @RequestParam(name = "saveReportToExcel", required = false)
            boolean saveReportToExcel) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = pcConfigService.updateDataAndCreatePcList(
                    updateUserBenchmarkCpu,
                    updateUserBenchmarkGpu,
                    updateHotline,
                    createPcList,
                    saveReportToExcel);

            response.put("message", result ? "Операція виконана успішно!"
                    : "Помилка виконання операції!");
            response.put("success", result);
        } catch (Exception e) {
            log.error("Error occurred while executing operation", e);
            response.put("message", "Виникла помилка: " + e.getMessage());
            response.put("success", false);
        }
        return response;
    }
}
