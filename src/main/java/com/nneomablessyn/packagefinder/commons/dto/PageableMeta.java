package com.nneomablessyn.packagefinder.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageableMeta<T> {
    private final List<T> list;
    private final int page;
    private final int limit;
    private final long total;
}
