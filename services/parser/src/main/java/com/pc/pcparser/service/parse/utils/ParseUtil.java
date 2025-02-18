package com.pc.pcparser.service.parse.utils;

import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ParseUtil {
    public static Double stringToDoubleIfErrorReturnMinusOne(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        try {
            if (text.matches("\\d+(\\.\\d+)?")) {
                return Double.parseDouble(text);
            }
        } catch (NumberFormatException e) {
            //ignore and return -1
        }
        return -1.0;
    }

    public static Integer stringToIntIfErrorReturnMinusOne(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            if (text.matches("\\d+")) {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            //ignore and return -1
        }
        return -1;
    }

    public static void applyRandomDelay(DelayInSeconds delayInSeconds, boolean isDelay) {
        applyRandomDelay(delayInSeconds.getFromSec(), delayInSeconds.getToSec(), isDelay);
    }

    public static void applyRandomDelay(int fromSec, int toSec, boolean isDelay) {
        if (isDelay) {
            if (fromSec < 0 || toSec < 0 || fromSec > toSec) {
                log.warn("Invalid delay range specified. Ensure 0 <= fromSec <= toSec.");
                return;
            }

            if (fromSec == 0 && toSec == 0) {
                log.info("No delay as both fromSec and toSec are zero.");
                return;
            }

            Random random = new Random();
            int delay = fromSec + random.nextInt(toSec - fromSec + 1);

            try {
                log.info("Delay for " + delay + " seconds.");
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted during sleep", e);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    public static class DelayInSeconds {
        private int fromSec;
        private int toSec;
    }

}
