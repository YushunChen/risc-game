package edu.duke.ece651.team13.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is used to organize and send user login
 * information between client side and server side
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
