package com.ninjabooks.security.utils;

import com.ninjabooks.error.exception.TokenException;
import com.ninjabooks.security.user.SpringSecurityUser;

import static com.ninjabooks.security.utils.Audience.MOBILE;
import static com.ninjabooks.security.utils.Audience.TABLET;
import static com.ninjabooks.security.utils.Audience.UNKNOWN;
import static com.ninjabooks.security.utils.Audience.WEB;
import static com.ninjabooks.security.utils.Claim.AUDIENCE;
import static com.ninjabooks.security.utils.Claim.CREATED;
import static com.ninjabooks.security.utils.Claim.USERNAME;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger Logger = LogManager.getLogger(TokenUtils.class);

    private static final LocalDateTime ACTUAL_DATE_TIME = LocalDateTime.now();
    private static final int WEEK_IN_DAYS = 7;

    private final String secretHashValue;

    public TokenUtils(@Value(value = "${jwt.hash-secret-code}") String secretHashValue) {
        this.secretHashValue = secretHashValue;
    }

    public String generateToken(UserDetails userDetails, Device device) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME.key(), userDetails.getUsername());
        claims.put(AUDIENCE.key(), generateAudience(device));
        claims.put(CREATED.key(), Timestamp.valueOf(ACTUAL_DATE_TIME));
        return generateToken(claims);
    }

    public String getUsernameFromToken(String token) {
        return Optional.ofNullable(getClaimsFromToken(token))
            .map(Claims::getSubject)
            .orElseThrow(() -> new TokenException("Unable to obtain username from claims"));
    }

    public LocalDateTime getCreatedDateFromToken(String token) {
        long timestamp = Optional.ofNullable(getClaimsFromToken(token))
            .map(claims -> (long) claims.get(CREATED.key()))
            .orElseThrow(() -> new TokenException("Unable to obtain creation date from claims"));

        return new Timestamp(timestamp).toLocalDateTime();
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        return Optional.ofNullable(getClaimsFromToken(token))
            .map(claims -> claims.getExpiration().toInstant())
            .map(instant -> instant.atZone(ZoneId.systemDefault()))
            .map(ZonedDateTime::toLocalDateTime)
            .orElseThrow(() -> new TokenException("Unable to obtain expiration date from claims"));
    }

    public Audience getAudienceFromToken(String token) {
        return Optional.ofNullable(getClaimsFromToken(token))
            .map(claims -> claims.get(AUDIENCE.key(), String.class))
            .map(Audience::valueOf)
            .orElseThrow(() -> new TokenException("Unable to obtain audience from claims"));
    }

    public boolean canTokenBeRefreshed(String token, LocalDateTime lastPasswordReset) {
        final LocalDateTime created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
            && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        return Optional.ofNullable(getClaimsFromToken(token))
            .map(claims -> {
                claims.put(CREATED.key(), Timestamp.valueOf(LocalDateTime.now()));
                return generateToken(claims);
            })
            .orElseThrow(() -> new TokenException("Unable to refresh token"));
    }

    public boolean isValid(String token, UserDetails userDetails) {
        SpringSecurityUser user = (SpringSecurityUser) userDetails;
        String username = getUsernameFromToken(token);
        LocalDateTime created = getCreatedDateFromToken(token);
        return (username.equals(user.getUsername()) &&
            !isTokenExpired(token) &&
            !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordReset()));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                .setSigningKey(secretHashValue)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            Logger.error("Unable to obtain claims from token", e);
        }
        return claims;
    }

    private Timestamp generateExpirationDate() {
        return Timestamp.valueOf(ACTUAL_DATE_TIME.plusDays(WEEK_IN_DAYS));
    }

    private boolean isTokenExpired(String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(ACTUAL_DATE_TIME);
    }

    private boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }

    private Audience generateAudience(Device device) {
        Audience audience = UNKNOWN;
        if (device.isNormal()) {
            audience = WEB;
        }
        else if (device.isTablet()) {
            audience = TABLET;
        }
        else if (device.isMobile()) {
            audience = MOBILE;
        }
        return audience;
    }

    private boolean ignoreTokenExpiration(String token) {
        Audience audience = getAudienceFromToken(token);
        return (audience == TABLET || audience == MOBILE);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, secretHashValue)
            .compact();
    }
}
