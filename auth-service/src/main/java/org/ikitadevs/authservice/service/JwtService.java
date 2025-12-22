package org.ikitadevs.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.ikitadevs.authservice.model.User;
import org.ikitadevs.authservice.model.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expiration;

    public String extractEmail(String token) {
        return (String) extractClaim(token, claims -> claims.get("email"));
    }
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public UUID extractId(String token) {
        return UUID.fromString(extractSubject(token));
    }
    private Date extractExpiration (String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public Set<Role> extractRoles(String token) {
        List<?> rolesRaw = extractClaim(token, claims -> claims.get("roles", List.class));
        if (rolesRaw == null) return Collections.emptySet();

        return rolesRaw.stream()
                .map(Object::toString)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }
    public String generateToken(Map<String, Object> extraClaims, User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        extraClaims.put("roles", roles);
        extraClaims.put("email", user.getEmail());
        return buildToken(extraClaims, user.getId().toString());
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private String buildToken(Map<String, Object> extraClaims, String subject) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuer("auth-service")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw e;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
