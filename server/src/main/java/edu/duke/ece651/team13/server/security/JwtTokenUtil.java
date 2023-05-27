package edu.duke.ece651.team13.server.security;

import edu.duke.ece651.team13.server.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Generating, validating and parsing JWT tokens
 */
@Slf4j
@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    @Value("3gfO2FTfPh1VdMrbnXN3g1ZeR0sNaLC8Ksa6Iz8DxKmQlaa554VrFMrnFRXyxVDaoS2QAfesLwAbVvhm7mPQZg==")
    String SECRET_KEY;

    /**
     * Generates a JSON Web Token (JWT) access token for a given user entity. The access token includes the user's ID
     * and email as the subject, the issuer as "CodeJava", issued at the current date, and expires after a set duration.
     * The token is signed with the HS512 algorithm using the secret key stored in the instance variable.
     * @param user the user entity to generate the token for
     * @return the generated access token as a string
     */
    public String generateAccessToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("CodeJava")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * Validates the given access token by parsing its claims and verifying its signature using the secret key.
     * @param token The access token to be validated.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace" + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid" + ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported" + ex);
        }
        return false;
    }

    /**
     * Returns the subject of the JWT token.
     * @param token the JWT token to extract the subject from
     * @return the subject of the JWT token
     */
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Parses the claims of a JWT token using the provided SECRET_KEY.
     * @param token the JWT token to parse
     * @return the claims of the JWT token
     */
    Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}