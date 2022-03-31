package com.example.webpos.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart implements Serializable {

    private List<Item> items = new ArrayList<>();

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public boolean updateItem(Item item) {
        for(Item i : items) {
            if(i.getProduct().getId().equals(item.getProduct().getId())) {
                i.setQuantity(item.getQuantity());
                return true;
            }
        }
        return false;
    }

    public boolean deleteItem(Item item) {
        for(int i = 0;i < items.size();i++) {
            if(items.get(i).getProduct().getId().equals(item.getProduct().getId())) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public Item queryItemByProductId(String productId) {
        for(Item item : items) {
            if(item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    public boolean clear() {
        items.clear();
        return true;
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getQuantity() * items.get(i).getProduct().getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        if (items.size() ==0){
            return "Empty Cart";
        }
        double total = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cart -----------------\n"  );

        for (int i = 0; i < items.size(); i++) {
            stringBuilder.append(items.get(i).toString()).append("\n");
            total += items.get(i).getQuantity() * items.get(i).getProduct().getPrice();
        }
        stringBuilder.append("----------------------\n"  );

        stringBuilder.append("Total...\t\t\t" + total );

        return stringBuilder.toString();
    }

}
