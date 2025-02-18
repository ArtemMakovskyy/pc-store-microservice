package com.pc.pcparser.service.parse;

import java.util.List;

public interface PagesParser<T> extends PageParser<T> {
    List<T> parseAll();
}
