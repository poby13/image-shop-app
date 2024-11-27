package kr.co.cofile.sbimgshop.paypal;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
}
