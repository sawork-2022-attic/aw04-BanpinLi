package com.example.webpos.biz;

import com.example.webpos.model.Cart;
import com.example.webpos.model.Item;
import com.example.webpos.model.Product;

import java.util.List;

public interface PosService {
//    public Cart getCart();

//    public Cart newCart();

//    public void checkout(Cart cart);

//    public void total(Cart cart);

    public boolean update(Product product, int amount, Cart cart);

    public boolean updateIncrement(String productId, Cart cart);

    public boolean updateDecrease(String productId, Cart cart);

    public boolean update(String productId, int amount, Cart cart);

    boolean remove(String productId, Cart cart);

    boolean save(String productId, Cart cart);

    boolean emptyCart(Cart cart);

    public List<Product> products();
}
