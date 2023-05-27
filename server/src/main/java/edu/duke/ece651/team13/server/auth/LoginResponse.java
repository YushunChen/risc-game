package edu.duke.ece651.team13.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is used to organize and send user authentication result
 * between client side and server side
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String email;
    private String accessToken;
}
