package com.example.aquafin.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquafin.models.Order;
import com.example.aquafin.models.Product;
import com.example.aquafin.repositories.OrderRepository;
import com.example.aquafin.repositories.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductRepository productRepository;

    // @Autowired
    // private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void  addToCart(Long id,int quantity,String email){

        // Product product = productService.getProductById(id);

        // double totalPrice = calculateTotalAmount(id,quantity);

        // float totalPrice = product.getPrice(id) * quantity;

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found")); 

        float totalPrice = product.getPrice() * quantity;

        Order order = new Order();
        order.setEmail(email);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
    }

    
    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByEmail(email);
    }

    
    // @Override
    // public double calculateTotalAmount(Long id,int quantity){

    //     double productPrice = getProductPrice(id);

    //     return  productPrice * quantity;
    // }

    // @Override
    // public double getProductPrice(Long id) {
    //     return getProduct(id).getPrice();
    // }
    
    // private Product getProduct(Long id) {
    //     return productRepository.findById(id)
    //         .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    // }

    

}
