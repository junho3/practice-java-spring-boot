package com.example.demo.infrastructure.persistence.stock;

import com.example.demo.core.stock.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductCode(String productCode);

    @Query("SELECT s FROM Stock s")
    Stream<Stock> streamAll();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.productCode = :productCode")
    Optional<Stock> findByProductCodeForUpdate(String productCode);

    @Modifying
    @Query(
        value = """
            UPDATE stock SET quantity = (quantity - :quantity)
            WHERE product_code = :productCode
            AND quantity >= :quantity
            """,
        nativeQuery = true
    )
    int decreaseQuantity(
        @Param("productCode") String productCode,
        @Param("quantity") long quantity
    );
}
