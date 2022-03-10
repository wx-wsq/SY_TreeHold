package com.sq.SYTreeHole.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

public class JwtUtils {

    public static String getToken(String message, String issuer) {
        JWTCreator.Builder builder = JWT.create();
        HashMap<String, Object> hs = new HashMap<>();
        hs.put("typ", "JWT");
        hs.put("alg", "HS256");
        return builder.withClaim("username", message)
                .withHeader(hs)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24000 * 3600))
                .withSubject(message)
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256("SY-server-sign"));
    }

    public static DecodedJWT getTokenData(String token, String issuer) {
        return JWT.require(Algorithm.HMAC256("SY-server-sign")).withIssuer(issuer).build().verify(token);
    }

}
