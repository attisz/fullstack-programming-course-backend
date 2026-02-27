package hu.elte.fullstack.fullstack_programming_backend;

import static hu.elte.fullstack.fullstack_programming_backend.auth.AuthorizationHeaders.parseBasicAuthorizationHeader;
import static hu.elte.fullstack.fullstack_programming_backend.auth.AuthorizationHeaders.parseBearerAuthorizationHeader;
import static hu.elte.fullstack.fullstack_programming_backend.auth.AuthorizationHeaders.validateAuthBasicHeader;
import static hu.elte.fullstack.fullstack_programming_backend.auth.AuthorizationHeaders.validateAuthBearerHeader;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.status;

import com.auth0.jwt.interfaces.DecodedJWT;
import hu.elte.fullstack.fullstack_programming_backend.auth.Credentials;
import hu.elte.fullstack.fullstack_programming_backend.auth.JwtService;
import hu.elte.fullstack.fullstack_programming_backend.auth.LoginService;
import java.util.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    public static final String AUTHORIZATION = "Authorization";
    
    private final LoginService loginService;
    private final JwtService jwtService;

    public LoginController(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader(AUTHORIZATION) String authorizationHeader) {

        if (validateAuthBasicHeader(authorizationHeader)) {
            return status(UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        Credentials credentials = parseBasicAuthorizationHeader(authorizationHeader);

        if (loginService.validateCredentials(credentials)) {
            String token = jwtService.createJwtToken(credentials.username());
            String base64EncodedToken = Base64.getEncoder().encodeToString(token.getBytes());
            return ResponseEntity.noContent().header("Set-Cookie", "access_token=" + base64EncodedToken).build();
        } else {
            return status(UNAUTHORIZED).body("Invalid username or password");
        }
    }


    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint(@RequestHeader(AUTHORIZATION) String authorizationHeader) {

        if (validateAuthBearerHeader(authorizationHeader)) {
            return status(UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        try {
            String token = parseBearerAuthorizationHeader(authorizationHeader);
            DecodedJWT decodedJWT = jwtService.verifyAndDecodeToken(token);
            return ResponseEntity.ok("Hello, " + decodedJWT.getSubject());
        } catch (Exception exception) {
            return status(UNAUTHORIZED).body("This is a protected endpoint");
        }
    }


}
