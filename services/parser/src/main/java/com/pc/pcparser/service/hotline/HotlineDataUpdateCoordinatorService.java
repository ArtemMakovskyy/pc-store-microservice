package com.pc.pcparser.service.hotline;

import com.pc.pcparser.repository.PcConfigRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotlineDataUpdateCoordinatorService {
    private final List<HotlineDataUpdateService>
            hotlineDataUpdateServices;
    private final List<HotlineDatabaseSynchronizationService>
            hotlineDatabaseSynchronizationServices;
    private final PcConfigRepository
            pcConfigRepository;

    public void updateAllData() {
        pcConfigRepository.deleteAll();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);

        runAllDataUpdateServices(executor);
        runAllDatabaseSynchronizationServices();

        shutdownExecutor(executor);
    }

    private void runAllDataUpdateServices(ExecutorService executor) {
        log.info("Start of updating new hotline components ");

        for (HotlineDataUpdateService service : hotlineDataUpdateServices) {
            try {
                service.refreshDatabaseWithParsedData(executor);
            } catch (Exception e) {
                log.error("Error updating data in the service {}: {}",
                        service.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        log.info("Updates to new hotline components completed ");
    }

    private void runAllDatabaseSynchronizationServices() {
        log.info("Start of the process of synchronization of data of hotline and "
                + "tests of UserBenchmark for all services ");

        for (HotlineDatabaseSynchronizationService service
                : hotlineDatabaseSynchronizationServices) {
            try {
                service.synchronizeWithBenchmarkData();
            } catch (Exception e) {
                log.error("Error synchronizing hotline and UserBenchmark data {}: {}",
                        service.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        log.info("The process of synchronization of hotline data and "
                + "UserBenchmark tests has been completed ");

    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
