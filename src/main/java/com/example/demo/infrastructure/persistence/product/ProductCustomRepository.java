package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;
import org.springframework.data.domain.Page;

public interface ProductCustomRepository {

    Page<Product> search(SearchProductParam param);
}
