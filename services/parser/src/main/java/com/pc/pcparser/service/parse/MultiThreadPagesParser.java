package com.pc.pcparser.service.parse;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface MultiThreadPagesParser<T> extends PagesParser<T> {
    List<T> parseAllMultiThread(ExecutorService executor);
}
