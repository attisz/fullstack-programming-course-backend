package hu.elte.fullstack.fullstack_programming_backend.auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public boolean validateCredentials(Credentials credentials) {
        return "username".equals(credentials.username()) && "password".equals(credentials.password());
    }
}
