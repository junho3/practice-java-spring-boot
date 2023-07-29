package com.example.demo.web;

import jakarta.validation.constraints.Positive;

public abstract class PageRequest {
    @Positive
    private final Integer pageNumber;

    @Positive
    private final Integer pageSize;

    protected PageRequest(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber == null ? 0 : pageNumber - 1;
    }

    public int getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }
}
