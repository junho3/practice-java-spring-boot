package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.SearchProductParam;

import java.util.List;

public interface ProductCustomRepository {

    List<Product> search(SearchProductParam param);
}
