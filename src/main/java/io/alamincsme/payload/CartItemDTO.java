package io.alamincsme.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private ProductDTO product;
    private Integer quantity;
    private double discount;
    private double productPrice;

}