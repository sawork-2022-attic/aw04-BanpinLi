package com.example.webpos.biz;

import com.example.webpos.db.PosDB;
import com.example.webpos.model.Cart;
import com.example.webpos.model.Item;
import com.example.webpos.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PosServiceImp implements PosService {

    private PosDB posDB;

    @Autowired
    public void setPosDB(PosDB posDB) {
        this.posDB = posDB;
    }

//    @Override
//    public Cart getCart() {
//
//        Cart cart = posDB.getCart();
//        if (cart == null){
//            cart = this.newCart();
//        }
//        return cart;
//    }

//    @Override
//    public Cart newCart() {
//        return posDB.saveCart(new Cart());
//    }

//    @Override
//    public void checkout(Cart cart) {
//
//    }

//    @Override
//    public void total(Cart cart) {
//
//    }

    @Override
    public boolean update(Product product, int amount, Cart cart) {
        if(product == null) return false;

        return posDB.updateItem(new Item(product, amount), cart);
    }

    @Override
    public boolean updateIncrement(String productId, Cart cart) {
        Item item = posDB.queryItemByProductId(productId, cart);
        if(item == null) return false;

        return update(productId, item.getQuantity() + 1, cart);
    }

    @Override
    public boolean updateDecrease(String productId, Cart cart) {
        Item item = posDB.queryItemByProductId(productId, cart);
        if(item == null) return false;

        if(item.getQuantity() == 1) return false;

        return update(productId, item.getQuantity() - 1, cart);
    }

    @Override
    public boolean update(String productId, int amount, Cart cart) {

        Product product = posDB.getProduct(productId);
        if(product == null) return false;

        return update(product, amount, cart);
    }

    @Override
    public boolean remove(String productId, Cart cart) {
        Product product = posDB.getProduct(productId);
        if(product == null) return false;

        Item item = new Item(product, 1);
        return posDB.deleteItem(item, cart);
    }

    @Override
    public boolean save(String productId, Cart cart) {
        Item item = posDB.queryItemByProductId(productId, cart);
        if(item != null) {
            return update(productId, item.getQuantity() + 1, cart);
        }

        Product product = posDB.getProduct(productId);
        if(product == null) return false;

        item = new Item(product, 1);
        return posDB.insertItem(item, cart);
    }

    @Override
    public boolean emptyCart(Cart cart) {
        return posDB.deleteAllItem(cart);
    }

    @Override
    public List<Product> products() {
        return posDB.getProducts();
    }
}
