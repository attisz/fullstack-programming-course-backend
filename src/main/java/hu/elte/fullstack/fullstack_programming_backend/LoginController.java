package hu.elte.fullstack.fullstack_programming_backend;

import static hu.elte.fullstack.fullstack_programming_backend.auth.AuthorizationHeaders.parseBasicAuthorizationHeader;
import static hu.elte.fullstack.fullstack_programming_backend.auth.AuthorizationHeaders.parseBearerAuthorizationHeader;
import static org.springframework.http.HttpStatus.FORBIDDEN;

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

    private final JwtService jwtService;
    private final LoginService loginService;

    public LoginController(JwtService jwtService, LoginService loginService) {
        this.jwtService = jwtService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authorizationHeader) {

        Credentials credentials = parseBasicAuthorizationHeader(authorizationHeader);

        if (loginService.validateCredentials(credentials)) {
            String token = jwtService.createJwtToken(credentials.username());
            String base64EncodedToken = Base64.getEncoder().encodeToString(token.getBytes());
            return ResponseEntity.ok().header("Set-Cookie", "access_token=" + base64EncodedToken).build();
        } else {
            return ResponseEntity.status(FORBIDDEN).body("Invalid username or password");
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint(@RequestHeader("Authorization") String authorizationHeader) {
        DecodedJWT decodedJWT;

        try {
            String token = parseBearerAuthorizationHeader(authorizationHeader);
            decodedJWT = jwtService.verifyAndDecodeToken(token);
        } catch (Exception exception) {
            return ResponseEntity.status(FORBIDDEN).body("This is a protected endpoint");
        }

        return ResponseEntity.ok("Hello, " + decodedJWT.getSubject());
    }
}
