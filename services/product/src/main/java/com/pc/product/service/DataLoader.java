package com.pc.product.service;

import com.pc.product.dto.PcConfigDto;
import com.pc.product.dto.mapper.ComputerMapper;
import com.pc.product.feignclient.PcProductFeignClient;
import com.pc.product.model.Computer;
import com.pc.product.model.Keyboard;
import com.pc.product.model.Monitor;
import com.pc.product.model.Mouse;
import com.pc.product.repository.ComputerRepository;
import com.pc.product.repository.KeyboardRepository;
import com.pc.product.repository.MonitorRepository;
import com.pc.product.repository.MouseRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ComputerRepository computerRepository;
    private final MonitorRepository monitorRepository;
    private final KeyboardRepository keyboardRepository;
    private final MouseRepository mouseRepository;
    private final PcProductFeignClient pcProductFeignClient;
    private final ComputerMapper computerMapper;

    @Override
    public void run(String... args) throws Exception {
        loadComputers();
        loadKeyMonitors();
        loadKeyBoard();
        loadKeyMouses();
    }

    private void loadComputers() {
        final List<Computer> computers = computerRepository.findAll();
        if (computers.isEmpty()) {
            final List<Computer> computerList = pcProductFeignClient.getAllByBestPrice().stream()
                    .limit(10)
                    .map(computerMapper::configToEntity)
                    .toList();
            if (computerList.isEmpty()) {
                throw new RuntimeException("Can't add Computers from parser. Data is empty");
            }
            computerRepository.saveAll(computerList);
        }
    }

    private void loadKeyMonitors() {
        final List<Monitor> monitors = monitorRepository.findAll();
        if (monitors.isEmpty()){
            Monitor monitor = new Monitor();
            monitor.setName("Monitor");
            monitor.setDescription("Monitor description");
            monitor.setResolution("1280X1090");
            monitor.setPartNumber("monitortestserialnumber");
            monitor.setCostPrice(BigDecimal.valueOf(80.00));
            monitor.setSellingPrice(BigDecimal.valueOf(100.50));
            monitorRepository.save(monitor);
        }
    }

    private void loadKeyBoard() {
        final List<Keyboard> keyboards = keyboardRepository.findAll();
        if (keyboards.isEmpty()){
            Keyboard keyboard = new Keyboard();
            keyboard.setName("Keyboard");
            keyboard.setDescription("Keyboard description");
            keyboard.setPartNumber("keyboardtestserialnumber");
            keyboard.setCostPrice(BigDecimal.valueOf(5.00));
            keyboard.setSellingPrice(BigDecimal.valueOf(7.50));
            keyboardRepository.save(keyboard);
        }
    }



    private void loadKeyMouses() {
        final List<Mouse> mouses = mouseRepository.findAll();
        if (mouses.isEmpty()){
            Mouse mouse = new Mouse();
            mouse.setName("Mouse");
            mouse.setDescription("Mouse description");
            mouse.setPartNumber("mousetestserialnumber");
            mouse.setCostPrice(BigDecimal.valueOf(5.50));
            mouse.setSellingPrice(BigDecimal.valueOf(8.50));
            mouseRepository.save(mouse);
        }
    }

}
