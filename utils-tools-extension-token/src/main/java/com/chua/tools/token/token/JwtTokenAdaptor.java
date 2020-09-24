package com.chua.tools.token.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chua.tools.token.constant.Strings;
import com.chua.tools.token.entity.TokenConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * jwt
 * @author CH
 * @since 1.0
 */
public class JwtTokenAdaptor implements ITokenAdaptor {

    private static final String SECRET = "com.cgy.utils";

    @Override
    public String token(TokenConfig tokenConfig) throws Exception {
        //过期时间
        long expire = tokenConfig.getExpire();
        //额外信息
        Map<String, Object> message = tokenConfig.getMessage();
        //签名时间
        Date issuedAt = new Date();
        //消息头
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        JWTCreator.Builder builder = JWT.create().withHeader(header).withIssuedAt(issuedAt);
        if(expire > 0) {
            //设置过期时间
            Date expiresAt = new Date(System.currentTimeMillis() + expire);
            builder.withExpiresAt(expiresAt);
        }

        if(null != message) {
            renderBuilder(message, builder);
        }


        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 渲染信息
     * @param message
     * @param builder
     */
    private void renderBuilder(Map<String, Object> message, JWTCreator.Builder builder) {

        for (Map.Entry<String, Object> entry : message.entrySet()) {
            String type = entry.getValue().getClass().getSimpleName().toLowerCase();
            if(Strings.CLASS_BOOLEAN.equalsIgnoreCase(type)) {
                builder.withClaim(entry.getKey(), (Boolean) entry.getValue());
            } else if(Strings.CLASS_STRING.equalsIgnoreCase(type)) {
                builder.withClaim(entry.getKey(), (String) entry.getValue());
            } else if(Strings.CLASS_INTEGER.equalsIgnoreCase(type)) {
                builder.withClaim(entry.getKey(), (Integer) entry.getValue());
            } else if(Strings.CLASS_LONG.equalsIgnoreCase(type)) {
                builder.withClaim(entry.getKey(), (Long) entry.getValue());
            } else if(Strings.CLASS_DOUBLE.equalsIgnoreCase(type)) {
                builder.withClaim(entry.getKey(), (Double) entry.getValue());
            } else if(Strings.CLASS_DATE.equalsIgnoreCase(type)) {
                builder.withClaim(entry.getKey(), (Date) entry.getValue());
            } else if(Strings.CLASS_INTEGER_ARRAY.equalsIgnoreCase(type)) {
                builder.withArrayClaim(entry.getKey(), (Integer[]) (entry.getValue()));
            } else if(Strings.CLASS_LONG_ARRAY.equalsIgnoreCase(type)) {
                builder.withArrayClaim(entry.getKey(), (Long[]) entry.getValue());
            } else if(Strings.CLASS_STRING_ARRAY.equalsIgnoreCase(type)) {
                builder.withArrayClaim(entry.getKey(), (String[]) entry.getValue());
            }
        }
    }

    @Override
    public boolean verify(String token, Predicate predicate)  {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return predicate.test(decodedJWT);
        } catch (Exception e) {
            return false;
        }
    }
}
