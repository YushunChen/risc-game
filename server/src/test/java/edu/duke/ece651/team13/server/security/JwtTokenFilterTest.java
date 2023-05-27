package edu.duke.ece651.team13.server.security;

import edu.duke.ece651.team13.server.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtTokenFilterTest {
    private JwtTokenUtil jwtUtil;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private JwtTokenFilter filter;

    @BeforeEach
    public void setUp() {
        jwtUtil = mock(JwtTokenUtil.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        filter = new JwtTokenFilter();
        filter.jwtUtil = jwtUtil;
    }

    @Test
    public void testDoFilterInternalWithoutAuthorizationBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalWithInvalidAccessToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.validateAccessToken("token")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalWithValidAccessToken() throws Exception {
        // Arrange
        String token = "valid.token";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        HttpServletResponse response = mock(HttpServletResponse.class);

        FilterChain filterChain = mock(FilterChain.class);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        JwtTokenUtil jwtUtil = mock(JwtTokenUtil.class);
        when(jwtUtil.validateAccessToken(token)).thenReturn(true);
        when(jwtUtil.getSubject(token)).thenReturn("1,test@example.com");

        JwtTokenFilter filter = new JwtTokenFilter();
        filter.jwtUtil = jwtUtil;

        // Act
        // Create a mock SecurityContext to avoid "Argument passed to verify() is not a mock!" error
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).validateAccessToken(token);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> argumentCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(securityContext).setAuthentication(argumentCaptor.capture());

        UsernamePasswordAuthenticationToken authenticationToken = argumentCaptor.getValue();
        assertNotNull(authenticationToken);
        UserEntity actual = (UserEntity) authenticationToken.getPrincipal();
        assertEquals(userDetails.getUsername(), actual.getUsername());

        verify(filterChain).doFilter(request, response);
    }
}
