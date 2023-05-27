package edu.duke.ece651.team13.server.security;

import edu.duke.ece651.team13.server.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A filter that intercepts incoming requests and extracts
 * the JWT token from the "Authorization" header.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    public JwtTokenUtil jwtUtil;

    /**
     * Validates the JWT token, and if valid, sets the authentication context using information
     * stored in the token.
     * @param request the incoming request
     * @param response the response to be sent
     * @param filterChain the filter chain to be executed
     * @throws ServletException if an error occurs while processing the request
     * @throws IOException if an error occurs while processing the request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);

        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the request has an "Authorization" header with a value starting with "Bearer".
     * @param request the incoming request
     * @return true if the header exists and starts with "Bearer", false otherwise
     */
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }

    /**
     * Extracts the JWT token from the "Authorization" header.
     * @param request the incoming request
     * @return the JWT token as a string
     */
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    /**
     * Sets the authentication context using the information stored in the JWT token.
     * @param token the JWT token
     * @param request the incoming request
     */
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Extracts user details from the JWT token.
     * @param token the JWT token
     * @return a UserDetails object representing the authenticated user
     */
    private UserDetails getUserDetails(String token) {
        UserEntity userDetails = new UserEntity();
        String[] jwtSubject = jwtUtil.getSubject(token).split(",");

        userDetails.setId(Long.parseLong(jwtSubject[0]));
        userDetails.setEmail(jwtSubject[1]);

        return userDetails;
    }
}
