package gr.rongasa.agregator.config;

import gr.rongasa.agregator.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfiguration {

    @SuppressWarnings("Convert2Lambda")
    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return new UserDetailsService() {
            @Override
            @Transactional
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return accountRepository.findById(username).map(
                        user -> new org.springframework.security.core.userdetails.User(user.getEmail(),
                                user.getPassword(),
                                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                ).orElseThrow(() -> new UsernameNotFoundException("Email " + username + "was not found"));
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder encoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }
}
