package com.github.nastassjarosa.paymybuddy.security;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * Service Spring Security chargé de charger un utilisateur pour l'authentification.
 * Recherche un utilisateur par email ou par nom d'utilisateur et construit un UserDetails.
 */

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Charge un utilisateur à partir d'un identifiant fourni par le formulaire de connexion.
     *
     * @param usernameOrEmail identifiant saisi
     * @return UserDetails utilisé par Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
// Recherche d'abord par email, puis par username.
        User user = userRepository.findByEmail(usernameOrEmail).or(() -> userRepository.findByUsername(usernameOrEmail)).orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + usernameOrEmail));
// L'identifiant de session est l'email, le mot de passe correspond au hash stocké en base.

        return org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(user.getPassword()).roles("USER").build();
    }
}
