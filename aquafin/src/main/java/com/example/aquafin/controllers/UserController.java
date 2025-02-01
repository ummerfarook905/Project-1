package com.example.aquafin.controllers;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.aquafin.Dto.UserDto;
import com.example.aquafin.models.User;
import com.example.aquafin.repositories.UserRepository;
import com.example.aquafin.services.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }
    
    @GetMapping("/register")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto){
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
        @ModelAttribute("user") UserDto userDto,
        Model model
    ) {
        try {
            userService.save(userDto);
            model.addAttribute("message", "Registration successful!");
            return "login";
        } catch (Exception e) {
            model.addAttribute("message", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/dashboard")
    public String userPage(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            model.addAttribute("user", user);
        }
        return "dashboard";
    }
    

    @GetMapping("/super-admin")
    @PreAuthorize("hasrole('SUPER_ADMIN')")
    public String superAdminPage(Model model,Principal principal){
        try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User superAdmin = userRepository.findByEmail(userDetails.getUsername());

        List<User> users = userRepository.findAll()
            .stream()
            .filter(user -> !user.getEmail().equals(superAdmin.getEmail()))
            .collect(Collectors.toList());

        model.addAttribute("adminUser", userDetails);
        model.addAttribute("user", superAdmin);
        model.addAttribute("users", users);

        return "super-admin";
    } catch (UsernameNotFoundException e) {
        model.addAttribute("errorMessage", "Super Admin account not found");
        return "login";
    }
    }


    @GetMapping("/admin")
	public String adminPage(Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		
    User user = userRepository.findByEmail(userDetails.getUsername());
    

	model.addAttribute("adminUser", userDetails);
    model.addAttribute("user", user);

	List<User> users = userRepository.findByRole("USER");
	model.addAttribute("users",users);

    return "admin"; 
	}
    
    // @GetMapping("/admin")
    // public String adminpage(Model model, Principal principal){
    //     try {
    //         UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    //         User Admin = userRepository.findByEmail(userDetails.getUsername());
    
    //         List<User> users = userRepository.findAll()
    //             .stream()
    //             .filter(user -> !user.getEmail().equals(Admin.getEmail()))
    //             .collect(Collectors.toList());
    //         model.addAttribute("user", Admin);
    //         model.addAttribute("users", users);
    
    //         return "admin";
    //     } catch (UsernameNotFoundException e) {
    //         model.addAttribute("errorMessage", " Admin account not found");
    //         return "error";
    //     }
    // }

    @PostMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/super-admin/delete-user/{id}")
    public String deleteUserAsSuperadmin(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/super-admin/change-role/{id}")
    public String changeUserRole(@PathVariable("id") Long id,@ModelAttribute("role") String role){
        User user = userRepository.findById(id).orElse(null);
        if(user!=null){
            user.setRole(role);
            userRepository.save(user);
        }
        return "redirect:/super-admin";   
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
    



}