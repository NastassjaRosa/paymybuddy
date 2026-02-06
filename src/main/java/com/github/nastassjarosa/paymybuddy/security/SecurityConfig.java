package com.github.nastassjarosa.paymybuddy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité HTTP et des composants d'authentification.
 * Déclare l'encodeur de mot de passe, le provider d'authentification et la chaîne de filtres.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final com.github.nastassjarosa.paymybuddy.security.DatabaseUserDetailsService userDetailsService;

    public SecurityConfig(com.github.nastassjarosa.paymybuddy.security.DatabaseUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    /**
     * Fournit l'encodeur de mot de passe utilisé pour l'inscription, le changement de mot de passe
     * et la comparaison lors de l'authentification.
     *
     * @return encodeur BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
    /**
     * Fournit le provider d'authentification basé sur un UserDetailsService et un PasswordEncoder.
     *
     * @return AuthenticationProvider configuré
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider();
        // Chargement des utilisateurs depuis la base.
        prov.setUserDetailsService(userDetailsService);
        // Comparaison entre mot de passe saisi et hash stocké.
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
    }
    /**
     * Définit les règles d'accès, la page de login, et le mécanisme de logout.
     *
     * @param http configuration HTTP Spring Security
     * @return chaîne de filtres de sécurité
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
               // Déclare les routes publiques et protège le reste.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Configure l'authentification par formulaire.
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/transfer", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // Configure la déconnexion.
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }


}
