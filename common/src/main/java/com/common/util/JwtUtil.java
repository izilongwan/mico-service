package com.common.util;

import java.util.Date;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

@ConfigurationProperties("jwt-config")
@Configuration
@Data
public class JwtUtil {
    String key = "@#!@WIIWTWRFWFWQ%$##";

    long ttl = 0;

    public JwtBuilder buiJwtBuilder(String id, String name) {
        long exp = System.currentTimeMillis() + ttl;

        return Jwts.builder()
                .setId(id)
                .setSubject(name)
                .setIssuedAt(new Date())
                .setExpiration(ttl > 0 ? new Date(exp) : null)
                .signWith(SignatureAlgorithm.HS256, key);
    }

    public String buildJwt(String id, String name, Map<String, Object> map) {
        return buiJwtBuilder(id, name).addClaims(map).compact();
    }

    public String buildJwt(String id, String name) {
        return buiJwtBuilder(id, name).compact();
    }

    public Claims parseJwt(String token) {

        try {
            Claims parse = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            return parse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
