package com.example.demo.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import io.jsonwebtoken.Jwts;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {

    private static final Logger logger  = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpiration;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7); //Remove Bearer from token
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+jwtExpiration))
                .signWith(key())
                .compact();
    }

    public String getUSerNameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            System.out.println("Validate");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT token: {}", authToken);
        }catch (ExpiredJwtException e){
            logger.error("JWT token is expired: {}", authToken);
        }catch(UnsupportedJwtException e){
            logger.error("JWT token is unsupported: {}",authToken);
        }catch(IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}",authToken);
        }
        return false;
    }


    private Key key(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
