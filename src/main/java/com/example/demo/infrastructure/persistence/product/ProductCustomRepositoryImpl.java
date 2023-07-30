package com.example.demo.infrastructure.persistence.product;

import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.QProduct;
import com.example.demo.core.product.param.SearchProductParam;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ProductCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Product> search(SearchProductParam param) {
        final QProduct product = QProduct.product;

        JPQLQuery<Product> query = queryFactory
            .selectFrom(product)
            .where(
                eqProductStatus(product, param.getProductStatus()),
                goeProductAmount(product, param.getMinProductAmount()),
                loeProductAmount(product, param.getMaxProductAmount()),
                containProductName(product, param.getProductName())
            )
            .offset(param.getPageable().getOffset())
            .limit(param.getPageable().getPageSize());

        return new PageImpl<>(query.fetch(), param.getPageable(), query.fetchCount());
    }

    private BooleanExpression containProductName(QProduct product, String productName) {
        if (productName != null)  {
            return product.productName.contains(productName);
        }

        return null;
    }

    private BooleanExpression goeProductAmount(QProduct product, Long minProductAmount) {
        if (minProductAmount != null) {
            return product.productAmount.goe(minProductAmount);
        }

        return null;
    }

    private BooleanExpression loeProductAmount(QProduct product, Long maxProductAmount) {
        if (maxProductAmount != null) {
            return product.productAmount.loe(maxProductAmount);
        }

        return null;
    }

    private BooleanExpression eqProductStatus(QProduct product, ProductStatus productStatus) {
        if (productStatus != null) {
            return product.productStatus.eq(productStatus);
        }

        return null;
    }
}
