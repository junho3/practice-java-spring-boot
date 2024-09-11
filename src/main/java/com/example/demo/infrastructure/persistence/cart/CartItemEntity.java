package com.example.demo.infrastructure.persistence.cart;

import com.example.demo.config.persistence.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart_item")
public class CartItemEntity extends AuditEntity {
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "code_a")
    private String codeA;

    @Column(name = "code_b")
    private String codeB;

    public CartItemEntity(Long cartItemId, Long userId, Long productId, Long quantity, String codeA, String codeB) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.codeA = codeA;
        this.codeB = codeB;
    }

    public CartItemEntity(Long userId, Long productId, Long quantity, String codeA) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.codeA = codeA;
    }

    public void increaseQuantity(final Long quantity) {
        this.quantity = this.quantity + quantity;
    }
}
