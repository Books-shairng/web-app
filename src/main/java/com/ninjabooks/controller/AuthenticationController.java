package com.ninjabooks.controller;

import com.ninjabooks.json.authentication.AuthenticationRequest;
import com.ninjabooks.json.authentication.AuthenticationResponse;
import com.ninjabooks.security.SpringSecurityUser;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.util.SecurityHeaderFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Authenticate is based on jwt tokens.
 *
 * JWT is a means of transmitting information between two parties in a compact,
 * verifiable form. The bits of information encoded in the body of a JWT are
 * called claims. The expanded form of the JWT is in a JSON format, so each claim
 * is a key in the JSON object. JWTs can be cryptographically signed (making it a JWS)
 * or encrypted (making it a JWE). This adds a powerful layer of verifiability to the
 * user of JWTs. The receiver has a high degree of confidence that the JWT has not been
 * tampered with by verifying the signature, for instance. The compacted
 * representation of a signed JWT is a string that has three parts, each separated
 * by a .:
 * exmaple:
 * @code {
 *  <b>eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.ipevRNuRP6HflG8cFKnmUPtypruRC4fb1DWtoLL62SY</b>
 * }
 *
 * Each section is base 64 encoded. The first section is the header, which at a minimum
 * needs to specify the algorithm used to sign the JWT. The second section is the body.
 * This section has all the claims of this JWT encoded in it. The final section is the
 * signature. It's computed by passing a combination of the header and body through
 * the algorithm specified in the header.
 *
 * More detailed info, check this <a href="https://jwt.io/"> LINKI </a>
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController
{
    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;
    private final SecurityHeaderFinder securityHeaderFinder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserDetailsService userDetailsService, SecurityHeaderFinder securityHeaderFinder) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
        this.securityHeaderFinder = securityHeaderFinder;
    }

    /**
     * This method perform authorization based on jwt tokens.
     *
     * @param authenticationRequest - this object represnt request from frontend
     * @param device - automatically detects type device which is needed to generate token
     * @return HTTP status 200 (ok) with  generated token
     * @throws AuthenticationException if any exception with authorization ocurs
     */

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the authentication
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-authentication so we can generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String token = tokenUtils.generateToken(userDetails, device);

        logger.info("Successful generate token");
        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    /**
     * Perform token refresh.
     *
     * @param request - http reqest which contains jwt token in header
     * @return refreshed jwt token or HTTP bad request
     */

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = null;

        if (securityHeaderFinder.hasSecurityPattern(header))
            token = securityHeaderFinder.extractToken(header);

        String username = tokenUtils.getUsernameFromToken(token);
        SpringSecurityUser user = (SpringSecurityUser) userDetailsService.loadUserByUsername(username);
        if (tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
        }
        else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
