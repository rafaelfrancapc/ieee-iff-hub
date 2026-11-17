package br.com.ieeeiffcampos.hub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain publicCatalog(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .requestCache(cache -> cache.disable())
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable())
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; script-src 'self'; style-src 'self'; font-src 'self'; "
                    + "img-src 'self'; connect-src 'self'; object-src 'none'; "
                    + "base-uri 'self'; form-action 'self'; frame-ancestors 'none'"))
                .referrerPolicy(policy -> policy.policy(
                    org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))
            .build();
    }

    @Bean
    UserDetailsService noLocalAccounts() {
        return new InMemoryUserDetailsManager();
    }
}
