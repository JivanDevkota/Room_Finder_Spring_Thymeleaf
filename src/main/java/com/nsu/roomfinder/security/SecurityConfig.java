package com.nsu.roomfinder.security;

import com.nsu.roomfinder.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetails customUserDetails;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(auth->auth
                                .requestMatchers("/login","/home","/all/rooms","/paginated/rooms","users/register/**","users/login/**", "/resources/**", "/css/**" ,"/img/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated())
                        .formLogin(form -> form
                                .loginPage("/login")
                                .successHandler(authenticationSuccessHandler())   // Redirect to /home after login success
                                .permitAll()
                        )
                        .logout(logout->logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/home")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll())
                        .build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .findFirst()
                    .orElse("");

            if ("ROLE_ADMIN".equals(role)) {
                response.sendRedirect("/admin/dashboard");
            } else if ("ROLE_USER".equals(role)) {
                response.sendRedirect("/users/dashboard");
            } else {
                response.sendRedirect("/login?error");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder=http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetails).passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
