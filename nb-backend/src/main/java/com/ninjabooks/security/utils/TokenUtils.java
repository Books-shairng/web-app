package com.ninjabooks.security.utils;

import com.ninjabooks.security.user.SpringSecurityUser;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * This container create, refresh and validate JWT token.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
@PropertySource(value = "classpath:jwt-options.properties")
public class TokenUtils implements Serializable
{
    private static final long serialVersionUID = -3301605591108950415L;

    private static final LocalDateTime ACTUAL_DATE_TIME = LocalDateTime.now();
    private static final int WEEK_IN_DAYS = 7;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    private String secretHashValue;

    public TokenUtils(@Value(value = "${jwt.hash-secret-code}") String  secretHashValue) {
        this.secretHashValue = secretHashValue;
    }

    public String generateToken(UserDetails userDetails, Device device) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, Timestamp.valueOf(ACTUAL_DATE_TIME));
        return generateToken(claims);
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public LocalDateTime getCreatedDateFromToken(String token) {
        LocalDateTime created;
        try {
            Claims claims = getClaimsFromToken(token);
            Long val = (Long) claims.get(CLAIM_KEY_CREATED);
            created = new Timestamp(val).toLocalDateTime();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        LocalDateTime expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public Boolean canTokenBeRefreshed(String token, LocalDateTime lastPasswordReset) {
        final LocalDateTime created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
            && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, Timestamp.valueOf(LocalDateTime.now()));
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean isValid(String token, UserDetails userDetails) {
        SpringSecurityUser user = (SpringSecurityUser) userDetails;
        String username = getUsernameFromToken(token);
        LocalDateTime created = getCreatedDateFromToken(token);
        return (username.equals(user.getUsername()) &&
            !isTokenExpired(token) &&
            !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordReset()));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                .setSigningKey(secretHashValue)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Timestamp generateExpirationDate() {
        return Timestamp.valueOf(ACTUAL_DATE_TIME.plusDays(WEEK_IN_DAYS));
    }

    private Boolean isTokenExpired(String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(ACTUAL_DATE_TIME);
    }

    private Boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }

    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        }
        else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        }
        else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, secretHashValue)
            .compact();
    }
}
