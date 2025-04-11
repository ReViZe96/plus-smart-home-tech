package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Carts")
@Getter
@Setter
@NoArgsConstructor
public class Cart {

    @Id
    @Column(name = "id")
    private String shoppingCartId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @ElementCollection
    @CollectionTable(name = "Products", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Integer> products = new HashMap<>();

}
