package com.example.aquafin.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.aquafin.models.Order;
import com.example.aquafin.services.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    // @Autowired
    // ProductService productService;

    @PostMapping("/user/addToCart")
    public String addToCart(@RequestParam Long id ,  @RequestParam int quantity,@RequestParam String productName,Principal principal,Model model){

        String email = principal.getName();
    

        // double product = productservice.getProductById(Long id);

        // double totalPrice = orderService.calculateTotalAmount(quantity);

        orderService.addToCart(id, quantity,email,productName);

        List<Order> orders = orderService.getOrdersByEmail(email);
        model.addAttribute("orders", orders);

        double totalAmount = 0.0;
        for (Order order : orders) {
        totalAmount += order.getTotalPrice(); // Assuming Order has getPrice() method
        }
        model.addAttribute("totalAmount", totalAmount);
        return "user-cart";
    }
    @GetMapping("/user-cart")
    public String viewCart(Model model,Principal principal) {
        String email = principal.getName();
        List<Order> orders = orderService.getOrdersByEmail(email);

        double totalAmount = 0.0;
        for (Order order : orders) {
        totalAmount += order.getTotalPrice(); // Assuming Order has getPrice() method
        }
        model.addAttribute("orders", orders);
        model.addAttribute("totalAmount", totalAmount);
        
        return "user-cart";
    }

    @PostMapping("/pay")
    public String payForOrder(
        @RequestParam Long product,@RequestParam int quantity,
        Principal principal,RedirectAttributes redirectAttributes,double totalAmount){

            try{
                // String userEmail = principal.getName();

                Stripe.apiKey = "sk_test_51QLIbeGMcBpNczFvGfV2wOAKm4hY3Ehr9C7ObeR1hpFAJD0Ds7XACcc1gUBcMv8XO7lLARCPDEtzdrXXPhbb8J5300Wr5IzmEo";

                SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/success")
                    .setCancelUrl("http://localhost:8080/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity((long) quantity)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount((long) (totalAmount * 100)) // Amount in cents
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order Placed: " + product)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

                Session session = Session.create(params);

                return "redirect:" + session.getUrl();

            }
            catch(StripeException e){
                redirectAttributes.addFlashAttribute("error",  "Payment failed: " + e.getMessage());
                return "redirect:/user-cart";
            }
            
    }
    
    @GetMapping("/success")
    public String paymentSuccess(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "Payment successful! Your booking has been confirmed.");
        return "redirect:/order-view";
    }

    @GetMapping("/cancel")
    public String paymentCancelled(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error", "Payment was canceled. Please try again.");
        return "redirect:/order-view";
    }
    




}
