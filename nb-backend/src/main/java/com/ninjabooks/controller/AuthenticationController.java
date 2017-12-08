package com.ninjabooks.controller;

import com.ninjabooks.json.authentication.AuthenticationRequest;
import com.ninjabooks.json.authentication.AuthenticationResponse;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.security.utils.TokenUtils;
import com.ninjabooks.security.utils.SecurityHeaderUtils;

import javax.servlet.http.HttpServletRequest;

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

/**
 * Authenticate is based on jwt tokens.
 * <p>
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
 *
 * @author Piotr 'pitrecki' Nowak
 * @code {
 * <b>eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.ipevRNuRP6HflG8cFKnmUPtypruRC4fb1DWtoLL62SY</b>
 * }
 * <p>
 * Each section is base 64 encoded. The first section is the header, which at a minimum
 * needs to specify the algorithm used to sign the JWT. The second section is the body.
 * This section has all the claims of this JWT encoded in it. The final section is the
 * signature. It's computed by passing a combination of the header and body through
 * the algorithm specified in the header.
 * <p>
 * More detailed info, check this <a href="https://jwt.io/"> LINKI </a>
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

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method perform authorization based on jwt tokens.
     *
     * @param authenticationRequest - this object represent request from frontend
     * @param device                - automatically detects type device which is needed to generate token
     * @return HTTP status 200 (ok) with  generated token
     * @throws AuthenticationException if any exception with authorization ocurs
     */

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest,
                                                   Device device) throws AuthenticationException {
        logger.info("User: {} initiates authorization on the system", authenticationRequest.getEmail());

        performAuthentication(authenticationRequest);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String token = tokenUtils.generateToken(userDetails, device);

        logger.info("Successful generate token");
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    private void performAuthentication(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Perform token refresh.
     *
     * @param request - http reqest which contains jwt token in header
     * @return refreshed jwt token or HTTP bad request
     */

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        logger.info("Start refreshing the token");
        String header = request.getHeader("Authorization");
        String token = SecurityHeaderUtils.extractTokenFromHeader(header);
        String username = tokenUtils.getUsernameFromToken(token);

        SpringSecurityUser user = (SpringSecurityUser) userDetailsService.loadUserByUsername(username);
        if (tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            logger.info("Correctly refresh token");
            return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
        }
        else {
            logger.error("Failed to refresh the token");
            return ResponseEntity.badRequest().body(null);
        }
    }
}
