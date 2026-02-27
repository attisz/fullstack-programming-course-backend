package hu.elte.fullstack.fullstack_programming_backend.auth;

import java.util.Base64;

public final class AuthorizationHeaders {

    public static final String AUTHORIZATION_HEADER_BASIC = "Basic ";
    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    private AuthorizationHeaders() {
    }

    public static Credentials parseBasicAuthorizationHeader(String authorizationHeader) {
        String authToken = authorizationHeader.replace(AUTHORIZATION_HEADER_BASIC, "");
        String usernamePassword = new String(Base64.getDecoder().decode(authToken));
        String[] split = usernamePassword.split(":");

        String username = split[0];
        String password = split[1];

        return new Credentials(username, password);
    }

    public static String parseBearerAuthorizationHeader(String authorizationHeader) {
        String bearerToken = authorizationHeader.replace(AUTHORIZATION_HEADER_BEARER, "");
        byte[] jwtToken = Base64.getDecoder().decode(bearerToken);
        return new String(jwtToken);
    }

    public static boolean validateAuthBearerHeader(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_HEADER_BEARER);
    }

    public static boolean validateAuthBasicHeader(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_HEADER_BASIC);
    }
}
