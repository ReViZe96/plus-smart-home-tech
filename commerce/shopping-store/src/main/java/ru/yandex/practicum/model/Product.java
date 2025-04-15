package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.enums.ProductState;

import javax.validation.constraints.Min;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "id")
    private String productId;
    @Column(name = "name", nullable = false)
    private String productName;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "image_src")
    private String imageSrc;
    @Column(name = "quantity_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;
    @Column(name = "product_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState productState;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    @Min(1)
    @Column(name = "price", nullable = false)
    private Double price;

}
