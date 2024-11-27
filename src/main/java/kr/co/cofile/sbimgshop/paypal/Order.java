package kr.co.cofile.sbimgshop.paypal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String paypalOrderId;
    private String status;
    private Long productId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private Product product;
}