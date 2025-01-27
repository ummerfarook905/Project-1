package com.example.aquafin.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.aquafin.services.CustomSuccessHandler;
import com.example.aquafin.services.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomSuccessHandler customSuccessHandler;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable())
    
        .authorizeHttpRequests(request -> request
                .requestMatchers("/superadmin-page").hasAuthority("SUPER_ADMIN")  
                .requestMatchers("/admin-page").hasAuthority("ADMIN")              
                .requestMatchers("/dashboard").hasAuthority("USER")                
                .requestMatchers("/register", "/css/**").permitAll()           
                .anyRequest().authenticated())                                     
         
            
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                // .defaultSuccessUrl("/dashboard", true)  
                .successHandler(customSuccessHandler)
                // .failureUrl("/login?error=true")  
                .permitAll()  
            )
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")  
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)  
                .expiredUrl("/login?expired")  
            );
        return http.build();
    }

    @Autowired
    public void configure (AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
