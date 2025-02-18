package com.pc.pcparser.service.parse;

import java.util.List;

public interface PageParser<T> {
    List<T> parsePage(String url);
}
