package com.example.webpos.web;

import com.example.webpos.biz.PosService;
import com.example.webpos.model.Cart;
import com.example.webpos.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PosController {

    private PosService posService;

    @Autowired
    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model, HttpSession session) {
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", getCart(session));
        return "index";
    }

    @GetMapping("/charge-success.html")
    public String chargeSuccessPage() {
        return "charge-success";
    }

    @GetMapping("/save/{productId}")
    public ModelAndView addProduct(@PathVariable("productId") String productId,
                                   HttpSession session) {
        Cart cart = getCart(session);
        posService.save(productId, cart);
        setCart(session, cart);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @GetMapping("/update/incr/{productId}")
    public ModelAndView updateItemIncrement(@PathVariable("productId") String productId,
                                            HttpSession session) {
        Cart cart = getCart(session);
        posService.updateIncrement(productId, cart);
        setCart(session, cart);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @GetMapping("/update/decr/{productId}")
    public ModelAndView updateItemDecrease(@PathVariable("productId") String productId,
                                           HttpSession session) {
        Cart cart = getCart(session);
        posService.updateDecrease(productId, cart);
        setCart(session, cart);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @GetMapping("/delete/{productId}")
    public ModelAndView deleteItem(@PathVariable("productId") String productId,
                                   HttpSession session) {
        Cart cart = getCart(session);
        posService.remove(productId, cart);
        setCart(session, cart);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    @GetMapping("/charge")
    public ModelAndView charge(HttpSession session) {
        Cart cart = getCart(session);
        posService.emptyCart(cart);
        setCart(session, cart);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/charge-success.html");
        return modelAndView;
    }

    @GetMapping("/cancel")
    public ModelAndView cancel(HttpSession session) {
        Cart cart = getCart(session);
        posService.emptyCart(cart);
        setCart(session, cart);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index.html");
        return modelAndView;
    }

    private Cart getCart(HttpSession session) {
        Cart cart;
        if((cart = (Cart)session.getAttribute("cart")) == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void setCart(HttpSession session, Cart cart) {
        session.setAttribute("cart", cart);
    }
}
