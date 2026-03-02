package hu.elte.fullstack.fullstack_programming_backend.api;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/public")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> publicEndpoint() {
        return ok("{\"message\": \"Hello, anonymous!\"}");
    }

    @PostMapping("/login")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "204",
            headers = @Header(name = SET_COOKIE, examples = @ExampleObject("access_token=<base64_encoded_jwt_token>")))
    @ApiResponse(responseCode = "401")
    public ResponseEntity<?> loginEndpoint() {
        return noContent().build();
    }

    @GetMapping("/protected")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401")
    public ResponseEntity<?> protectedEndpoint() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return ok("{\"message\": \"Hello, " + name + "!\"}");
    }
}
