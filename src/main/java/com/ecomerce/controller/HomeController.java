package com.ecomerce.controller;

import com.ecomerce.service.CategoryService;
import com.ecomerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping({"/","/home"})
    public String home(Model model){

        return "index";
    }
    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProduct());
        return "shop";
    }
    @GetMapping("/shop/category/{id}")
    public String shopByCategory(@PathVariable int id, Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductByCategoryId(id));
        return "shop";
    }
    @GetMapping("/shop/viewproduct/{id}")
    public String shopViewpro(@PathVariable long id, Model model){
        model.addAttribute("product", productService.getProducById(id).get());
        return "viewProduct";
    }


}
