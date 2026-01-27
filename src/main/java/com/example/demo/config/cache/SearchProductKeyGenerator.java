package com.example.demo.config.cache;

import com.example.demo.core.product.param.SearchProductParam;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SearchProductKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        final SearchProductParam param = (SearchProductParam) params[0];

        return String.format(
            "name=%s|min=%s|max=%s|status=%s|page=%d|size=%d",
            param.productName(),
            param.minProductAmount(),
            param.maxProductAmount(),
            param.productStatus(),
            param.pageable().getPageNumber(),
            param.pageable().getPageSize()
        );
    }
}
