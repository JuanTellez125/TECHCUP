package edu.dosw.TECHCUP.security.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Slf4j
@Service
public class JwtService {
    //la clave para la firma del token
    @Value("${jwt.secret}")
    private String secretKey;

    //la duracion del token que le pusimos una hora
    @Value("${jwt.expiration}")
    private long expirationMs;


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.debug("Generating JWT token for user: {}", userDetails.getUsername());
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername()) //el email es el subject
                .issuedAt(new Date(System.currentTimeMillis()))//la fecha de creacion
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // el tiempo de duracion que es una hora
                .signWith(getSigningKey())//la firma del token con la clave
                .compact();//esto convierte en JWT
    }

    //esto nos da los datos del token osea el email
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //nos da el token y lo que queremos sacar de el
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }



    private Claims extractAllClaims(String token) {
        return Jwts.parser() //usa JJWT
                .verifyWith(getSigningKey()) //usa la clave secreta
                .build()
                .parseSignedClaims(token)//verifica que el token no fue modificado y tenga una firma valida
                .getPayload();//extrae los claims
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            if (!valid) {
                log.warn("Token invalido para usuario: {}", userDetails.getUsername());
            }
            return valid;
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Firma JWT invalida: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Token JWT malformado: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("Token JWT no soportado: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Claims JWT vacios: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Construye la SecretKey, Se usa HMAC-SHA256 (HS256) para la firma.
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}