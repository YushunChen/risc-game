package edu.duke.ece651.team13.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is used to organize and send user registration
 * information between client side and server side
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
}
