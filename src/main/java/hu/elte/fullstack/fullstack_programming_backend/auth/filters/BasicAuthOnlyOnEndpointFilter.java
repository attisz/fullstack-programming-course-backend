package hu.elte.fullstack.fullstack_programming_backend.auth.filters;

import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

public class BasicAuthOnlyOnEndpointFilter extends OncePerRequestFilter {

    private final String loginEndpoint;

    public BasicAuthOnlyOnEndpointFilter(String loginEndpoint) {
        this.loginEndpoint = loginEndpoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(AUTHENTICATION_SCHEME_BASIC) && !request.getRequestURI().equals(loginEndpoint)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Basic authentication is only enabled on " + loginEndpoint);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
