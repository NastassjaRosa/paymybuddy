package com.github.nastassjarosa.paymybuddy.security;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Permet à Spring Security de charger un utilisateur depuis la bdd
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Recherche un utilisateur par email ou username.
     * Retourne un objet UserDetails utilisé par Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Recherche d'abord par email, puis par username
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + usernameOrEmail));

        // Conversion en UserDetails Spring
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // ou user.getUsername(), selon ta logique
                .password(user.getPassword()) // hash bcrypt
                .roles("USER") // tu pourras plus tard ajouter ROLE_ADMIN etc.
                .build();
    }
}
