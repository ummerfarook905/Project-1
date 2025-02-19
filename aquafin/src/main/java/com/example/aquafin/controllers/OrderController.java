package com.example.aquafin.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.aquafin.models.ConfirmOrder;
import com.example.aquafin.models.Order;
import com.example.aquafin.repositories.ConfirmOrderRepository;
import com.example.aquafin.services.ConfirmOrderService;
import com.example.aquafin.services.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    private ConfirmOrderService confirmOrderService;

    @Autowired
    private ConfirmOrderRepository confirmOrderRepository;

    // @Autowired
    // ProductService productService;

    @PostMapping("/user/addToCart")
    public String addToCart(@RequestParam String id ,  @RequestParam int quantity,@RequestParam String productName,@RequestParam float price,Principal principal,Model model){

        String email = principal.getName();
    

        // double product = productservice.getProductById(Long id);

        // double totalPrice = orderService.calculateTotalAmount(quantity);

        orderService.addToCart(id, quantity,email,productName,price);

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
    Principal principal,
    // @RequestParam("totalAmount") double totalAmount,  // Make sure to get totalAmount from form
    RedirectAttributes redirectAttributes) {

    try {
        String userEmail = principal.getName();
        
        // Get user's orders
        List<Order> orders = orderService.getOrdersByEmail(userEmail);
        
        Stripe.apiKey = "sk_test_51QtoigP8frw7MYicvGrF7NeXuskG7EavK91IxGTaFBqz4AVNgosp2YhHzulQOIAULWldBfpwmMu5RCBOyK4gpXhf00Mtafchc3";

        // Create line items for each order
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        
        for (Order order : orders) {
            lineItems.add(
                SessionCreateParams.LineItem.builder()
                    .setQuantity((long) order.getQuantity())  // Set quantity
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(Math.round(order.getPrice() * 100))
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(order.getProductName())  // Use product name
                                    .setDescription("Quantity: " + order.getQuantity())  // Add quantity info
                                    .build()
                            )
                            .build()
                    )
                    .build()
            );
        }

        // Create session with all line items
        SessionCreateParams params = SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/success")
            .setCancelUrl("http://localhost:8080/cancel")
            .addAllLineItem(lineItems)  // Add all line items
            .setCustomerEmail(userEmail)  // Add customer email
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
    public String handlePaymentSuccess(
            @RequestParam("session_id") String sessionId,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            // Retrieve the session from Stripe
            Stripe.apiKey = "sk_test_51QtoigP8frw7MYicvGrF7NeXuskG7EavK91IxGTaFBqz4AVNgosp2YhHzulQOIAULWldBfpwmMu5RCBOyK4gpXhf00Mtafchc3";
            Session session = Session.retrieve(sessionId);

            // Check if payment was successful
            if ("complete".equals(session.getStatus())) {
                String email = principal.getName();
                String paymentId = session.getPaymentIntent();

                // Create confirm orders and clear cart
                confirmOrderService.createConfirmOrdersFromCart(email, paymentId);

                redirectAttributes.addFlashAttribute("success", 
                    "Payment successful! Your order has been confirmed.");
                return "redirect:/order-confirmation";
            }

            redirectAttributes.addFlashAttribute("error", 
                "Payment was not completed successfully.");
            return "redirect:/user-cart";

        } catch (StripeException e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error processing payment confirmation: " + e.getMessage());
            return "redirect:/user-cart";
        }
    }

    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(Model model, Principal principal) {
        String email = principal.getName();
        List<ConfirmOrder> confirmOrders = confirmOrderRepository.findByEmail(email);
        model.addAttribute("orders", confirmOrders);
        return "order-confirmation";
    }

    @GetMapping("/cancel")
    public String paymentCancelled(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error", "Payment was canceled. Please try again.");
        return "redirect:/order-view";
    }

    
}
