package com.stevenk.wholefoods.security.jwt;

import com.stevenk.wholefoods.security.user.CustomerDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Component
public class JWTutils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expireTime}")
    private int expirationTime;

    public String generateToken(Authentication authentication) {
        CustomerDetails customerPrincipal = (CustomerDetails) authentication.getPrincipal();
        List<String> roles = customerPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(customerPrincipal.getEmail())
                .claim("id", customerPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(java.util.Date.from(java.time.Instant.now()))
                .setExpiration(java.util.Date.from(java.time.Instant.now().plusSeconds(expirationTime)))
                .signWith(key(), io.jsonwebtoken.SignatureAlgorithm.HS512).compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }
}
