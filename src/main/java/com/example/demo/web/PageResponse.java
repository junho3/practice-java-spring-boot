package com.example.demo.web;

import lombok.Getter;

@Getter
public abstract class PageResponse {
    private final long pageNumber;
    private final long pageSize;
    private final long totalCount;

    protected PageResponse(long pageNumber, long pageSize, long totalCount) {
        this.pageNumber = pageNumber + 1;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }
}
