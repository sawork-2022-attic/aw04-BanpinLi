package com.example.webpos.db;

import com.example.webpos.model.Cart;
import com.example.webpos.model.Item;
import com.example.webpos.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class JD implements PosDB {

    private List<Product> products;

    @Override
    @Cacheable(value = "products")
    public List<Product> getProducts() {
        try {
            if (products == null)
                products = parseJD("Java");
        } catch (IOException e) {
            products = new ArrayList<>();
        }
        return products;
    }

    @Override
    @Cacheable(key = "#productId", cacheNames = "product")
    public Product getProduct(String productId) {
        System.out.println("query by id");
        for (Product p : getProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean deleteItem(Item item, Cart cart) {
        return cart.deleteItem(item);
    }

    @Override
    public boolean updateItem(Item item, Cart cart) {
        return cart.updateItem(item);
    }

    @Override
    public boolean insertItem(Item item, Cart cart) {
        return cart.addItem(item);
    }

    @Override
    public Item queryItemByProductId(String productId, Cart cart) {
        return cart.queryItemByProductId(productId);
    }

    @Override
    public boolean deleteAllItem(Cart cart) {
        return cart.clear();
    }

    public static List<Product> parseJD(String keyword) throws IOException {
        //获取请求https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        //解析网页
        Document document = Jsoup.parse(new URL(url), 10000);
        //所有js的方法都能用
        Element element = document.getElementById("J_goodsList");
        //获取所有li标签
        Elements elements = element.getElementsByTag("li");
//        System.out.println(element.html());
        List<Product> list = new ArrayList<>();

        //获取元素的内容
        for (Element el : elements
        ) {
            //关于图片特别多的网站，所有图片都是延迟加载的
            String id = el.attr("data-spu");
            String img = "https:".concat(el.getElementsByTag("img").eq(0).attr("data-lazy-img"));
            String price = el.getElementsByAttribute("data-price").text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            if (title.indexOf("，") >= 0)
                title = title.substring(0, title.indexOf("，"));
            if(id != null && id.length() != 0) {
                Product product = new Product(id, title, Double.parseDouble(price), img);
                list.add(product);
            }

        }
        return list;
    }

}
