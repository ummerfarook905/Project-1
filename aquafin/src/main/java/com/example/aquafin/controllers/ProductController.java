package com.example.aquafin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.aquafin.models.Product;
import com.example.aquafin.repositories.ProductRepository;
import com.example.aquafin.services.ProductService;


@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/super-admin/add-product")
    @PreAuthorize("hasAthority('SUPER_ADMIN')")
    public  String getAddProduct(Model model){
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/super-admin/add-product")
    @PreAuthorize("hasAthority('SUPER_ADMIN')")
    public String addProduct(@ModelAttribute("product") Product product) {
        try {
            productService.addProduct(product);
            return "redirect:/super-admin/view-products";  
        } catch (Exception e) {
            return "redirect:/super-admin/add-product?error";
        }
    }

    @GetMapping("/super-admin/view-products")
    @PreAuthorize("hasAthority('SUPER_ADMIN')")
    public String viewProducts(Model model){
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products",products);
        return "view-products";
    }

    @GetMapping("/super-admin/update-products/{id}")
    @PreAuthorize("hasAthority('SUPER_ADMIN')")
    public String getEditProductPage(@PathVariable Long id, Model model) {  // Added @PathVariable
        Product product = productService.getProductById(id);
        if(product == null){
            model.addAttribute("error", "product not found");
            return "redirect:/super-admin/view-products";
        }
        model.addAttribute("product", product);
        return "update-product";
    }

    // @PostMapping("/super-admin/update-products/{id}")
    // @PreAuthorize("hasAthority('SUPER_ADMIN')")
    // public String updateProduct(@PathVariable Long id,@ModelAttribute("product") Product product){
    //     Product existProduct = productService.getProductById(id);

    //     if(existProduct != null){
    //         existProduct.setName(product.getName());
    //         existProduct.setDescription(product.getDescription());
    //         existProduct.setPrice(product.getPrice());
    //         existProduct.setQuantity(product.getQuantity());
    //         existProduct.setImage(product.getImage());

    //         productService.updateProduct(existProduct);
    //     }

    //     return "redirect:/super-admin/view-products";
    // }

    @PostMapping("/super-admin/update-products/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute Product updatedProduct,
                                RedirectAttributes redirectAttributes){
        Product product = productRepository.findById(id).orElse(null);

        if(product != null) {
            // Update basic details
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            product.setImage(updatedProduct.getImage());



            productRepository.save(product);
            redirectAttributes.addFlashAttribute("message", 
                "Product updated successfully");
        }
        else {
            redirectAttributes.addFlashAttribute("error", 
                "Product not found");
        }

        return "redirect::/super-admin/view-products";


    }

    @PostMapping("/super-admin/delete-product/{id}")
    @PreAuthorize("hasAthority('SUPER_ADMIN')")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return "redirect:/super-admin/view-products";
    }

    @GetMapping("/product-search")
    public String searchProducts(@RequestParam(value="name",required = false) String name,Model model){
        List<Product> products = productService.searchProducts(name);
        model.addAttribute("products", products);
        model.addAttribute("name", name);
        return "product-search";
    }

    // @PostMapping("/super-admin/delete-product/{id}")
    // @PreAuthorize("hasAthority('SUPER_ADMIN')")
    // public String deleteProducts(@PathVariable("id") Long id){
    //     productService.deleteProduct(id);
    //     return "redirect:/super-admin";
    // }

    
}
