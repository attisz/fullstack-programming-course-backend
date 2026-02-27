package hu.elte.fullstack.fullstack_programming_backend.auth;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MyRsaKeyProvider implements RSAKeyProvider {

    public static final String RSA_PUB_PEM_LOCATION = "/pki/rsa_pub.pem";
    public static final String RSA_PRIV_PEM_LOCATION = "/pki/rsa_priv.pem";

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public MyRsaKeyProvider() throws Exception {
        publicKey = readX509PublicKey();
        privateKey = readPKCS8PrivateKey();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String keyId) {
        return publicKey;
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }

    private RSAPublicKey readX509PublicKey() throws Exception {
        try (InputStream resourceAsStream = getClass().getResourceAsStream(RSA_PUB_PEM_LOCATION)) {
            String key = new String(resourceAsStream.readAllBytes());
            String publicKeyPEM = normalizePem(key);

            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
    }

    private RSAPrivateKey readPKCS8PrivateKey() throws Exception {
        try (InputStream resourceAsStream = getClass().getResourceAsStream(RSA_PRIV_PEM_LOCATION)) {
            String key = new String(resourceAsStream.readAllBytes());
            String privateKeyPEM = normalizePem(key);

            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        }
    }

    private static @NonNull String normalizePem(String key) {
        return key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
    }
}
