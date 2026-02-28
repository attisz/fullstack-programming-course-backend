package hu.elte.fullstack.fullstack_programming_backend.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BasicToJwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final JwtService jwtService;

    public BasicToJwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = securityContextHolderStrategy.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication instanceof UsernamePasswordAuthenticationToken) {
            response.addHeader("Authorization", "Bearer " + jwtService.createJwtToken(authentication.getName()));
        }

        filterChain.doFilter(request, response);
    }
}
