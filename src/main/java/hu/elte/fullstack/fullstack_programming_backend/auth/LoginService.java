package hu.elte.fullstack.fullstack_programming_backend.auth;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final Map<String, String> users = Map.of(
            "user1", "password123",
            "user2", "password456"
    );

    public boolean validateCredentials(Credentials credentials) {
        return users.containsKey(credentials.username())
                && users.get(credentials.username()).equals(credentials.password());
    }
}
