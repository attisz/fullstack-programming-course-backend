package hu.elte.fullstack.fullstack_programming_backend;

import static org.springframework.security.config.Customizer.withDefaults;

import hu.elte.fullstack.fullstack_programming_backend.auth.JwtService;
import hu.elte.fullstack.fullstack_programming_backend.auth.filters.BasicAuthOnlyOnEndpointFilter;
import hu.elte.fullstack.fullstack_programming_backend.auth.filters.BasicToJwtAuthenticationFilter;
import hu.elte.fullstack.fullstack_programming_backend.auth.filters.JwtPreAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {

    public static final String LOGIN_ENDPOINT = "/login";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService) {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .addFilterBefore(new JwtPreAuthFilter(jwtService), BasicAuthenticationFilter.class)
                .addFilterBefore(new BasicAuthOnlyOnEndpointFilter(LOGIN_ENDPOINT), BasicAuthenticationFilter.class)
                .addFilterAfter(new BasicToJwtAuthenticationFilter(jwtService), BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}