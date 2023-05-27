package edu.duke.ece651.team13.server.security;

import edu.duke.ece651.team13.server.entity.UserEntity;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class JwtTokenUtilTest {

    private static final String SECRET_KEY = "3gfO2FTfPh1VdMrbnXN3g1ZeR0sNaLC8Ksa6Iz8DxKmQlaa554VrFMrnFRXyxVDaoS2QAfesLwAbVvhm7mPQZg==";

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtil.SECRET_KEY = SECRET_KEY;
        when(userEntity.getId()).thenReturn(123L);
        when(userEntity.getEmail()).thenReturn("testuser@test.com");
    }

    @Test
    public void testGenerateAccessToken() {
        String token = jwtTokenUtil.generateAccessToken(userEntity);
        assertNotNull(token);
    }

    @Test
    public void testValidateAccessToken() {
        String token = jwtTokenUtil.generateAccessToken(userEntity);
        boolean result = jwtTokenUtil.validateAccessToken(token);
        assertTrue(result);
    }

    @Test
    public void testValidateAccessTokenWithInvalidToken() {
        boolean result = jwtTokenUtil.validateAccessToken("invalid_token");
        assertFalse(result);
    }

    @Test
    public void testGetSubject() {
        String token = jwtTokenUtil.generateAccessToken(userEntity);
        String subject = jwtTokenUtil.getSubject(token);
        assertEquals(String.format("%s,%s", userEntity.getId(), userEntity.getEmail()), subject);
    }

    @Test
    public void testParseClaims() {
        String token = jwtTokenUtil.generateAccessToken(userEntity);
        Claims claims = jwtTokenUtil.parseClaims(token);
        assertNotNull(claims);
    }
}
