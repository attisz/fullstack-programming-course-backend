package hu.elte.fullstack.fullstack_programming_backend;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        return ok("Hello, public!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEndpoint() {
        return noContent().build();
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return ok("Hello, " + name);
    }


}
