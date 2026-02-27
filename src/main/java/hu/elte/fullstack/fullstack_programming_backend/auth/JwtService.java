package hu.elte.fullstack.fullstack_programming_backend.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public static final String ISSUER = "auth0";

    private final RSAKeyProvider rsaKeyProvider;
    private final JWTVerifier jwtVerifier;

    public JwtService(RSAKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;

        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        jwtVerifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
    }

    public String createJwtToken(String username) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public DecodedJWT verifyAndDecodeToken(String token) {
        return jwtVerifier.verify(token);
    }
}
