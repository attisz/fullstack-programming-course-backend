package hu.elte.fullstack.fullstack_programming_backend.auth.filters;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.interfaces.DecodedJWT;
import hu.elte.fullstack.fullstack_programming_backend.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtPreAuthFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final JwtService jwtService;

    public JwtPreAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_HEADER_BEARER)) {
            String token = authorizationHeader.replace(AUTHORIZATION_HEADER_BEARER, "");

            try {
                DecodedJWT decodedJWT = jwtService.verifyAndDecodeToken(token);

                PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(decodedJWT.getSubject(), null);
                authentication.setAuthenticated(true);

                SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
                securityContext.setAuthentication(authentication);

                securityContextHolderStrategy.setContext(securityContext);
            } catch (Exception e) {
                Logger.getLogger(JwtPreAuthFilter.class.getName()).warning("Invalid JWT token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
