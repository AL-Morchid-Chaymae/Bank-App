package com.example.bankapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import com.example.bankapp.service.AccountService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authz -> authz
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll() // Utilisation de requestMatchers
                        .anyRequest().authenticated() // Toutes les autres requêtes doivent être authentifiées
                )
                .formLogin(form -> form
                        .loginPage("/login") // Page de connexion personnalisée
                        .loginProcessingUrl("/login") // URL pour le traitement du login
                        .defaultSuccessUrl("/dashboard", true) // Redirection après connexion réussie
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL de déconnexion
                        .logoutSuccessUrl("/login?logout") // Redirection après déconnexion
                        .invalidateHttpSession(true) // Invalider la session
                        .clearAuthentication(true) // Supprimer les informations d'authentification
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Autoriser les frames depuis la même origine
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }
}
