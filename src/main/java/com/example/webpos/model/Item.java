package com.example.webpos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
    private Product product;
    private int quantity;

    @Override
    public String toString(){
        return product.toString() +"\t" + quantity;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Item item = (Item) o;
//        return product.equals(item.product);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(product);
//    }
}
