package com.nneomablessyn.packagefinder.security.auth;

import com.nneomablessyn.packagefinder.exceptions.CustomException;
import com.nneomablessyn.packagefinder.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Value("${jwt.token.reset-password.expire-length}")
    private long validityInMillisecondsForResetPassword;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, boolean isResetPassword) {

        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity;
        if(isResetPassword) {
            validity = new Date(now.getTime() + validityInMillisecondsForResetPassword);
        }else {
            validity = new Date(now.getTime() + validityInMilliseconds);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    protected Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    protected String resolveToken(HttpServletRequest req) {
        String token = null;
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        return token;
    }

    protected boolean validateToken(String token, HttpServletRequest httpServletRequest) {
        String errorMessage;
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException JWT Token Error ", e);
            errorMessage = "User Token Has Expired";
            httpServletRequest.setAttribute("actual-error", errorMessage);
            throw new CustomException(errorMessage, HttpStatus.UNAUTHORIZED);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT Token Validation Error ", e);
            errorMessage = "Invalid User Token Supplied";
            httpServletRequest.setAttribute("actual-error", errorMessage);
            throw new CustomException(errorMessage, HttpStatus.UNAUTHORIZED);
        }
    }

}
