package com.ecomerce.controller;

import com.ecomerce.dto.ProductDTO;
import com.ecomerce.model.Category;
import com.ecomerce.model.Product;
import com.ecomerce.service.CategoryService;
import com.ecomerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/productImages";

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getCat(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }

    //tao mot model san de hung data o view va chuen den view add
    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id) {
        categoryService.removeCatogyryById(id);
        return "redirect:/admin/categories";
    }
    @GetMapping ("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model){
        Optional<Category> category= categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category);
            return "categoriesAdd";
        }
        return "404";

    }
//    Produc Section
    @GetMapping("/admin/products")
    public String getPro(Model model) {
        model.addAttribute("products",productService.getAllProduct() );
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String getProAdd(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }

    @PostMapping ("/admin/products/add")
    public String postProAdd(@ModelAttribute ("productDTO") ProductDTO productDTO,
                             @RequestParam ("productImage")MultipartFile file,
                             @RequestParam ("imgName")String imgName) throws Exception{
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setDescription(productDTO.getDescription());
        product.setWeight(productDTO.getWeight());
        String imageUUID;
        if(!file.isEmpty()){
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
            Files.write(fileNameAndPath,file.getBytes());
        }else {
            imageUUID =imgName;
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/delete/{id}")
    public String deletePro(@PathVariable long id) {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updatePro(@PathVariable long id,Model model){
        Product product = productService.getProducById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setWeight(product.getWeight());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setImageName(product.getImageName());
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",productDTO);
        return "productsAdd";
    }
}














