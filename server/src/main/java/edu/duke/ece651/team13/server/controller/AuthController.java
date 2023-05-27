package edu.duke.ece651.team13.server.controller;

import edu.duke.ece651.team13.server.auth.LoginRequest;
import edu.duke.ece651.team13.server.auth.LoginResponse;
import edu.duke.ece651.team13.server.auth.RegisterRequest;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.security.JwtTokenUtil;
import edu.duke.ece651.team13.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the controller for authentication - login and register features
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    final BCryptPasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the given user input.
     *
     * @param registerRequest the user input containing the full name, email, and password of the user.
     * @return a ResponseEntity with a success message and the ID of the registered user if successful,
     * or an error message if the user already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        log.info("Received request on /register");
        // check if the user already exists in the database
        String existResult = userService.isUserPresent(registerRequest);
        if (existResult != null) {
            // user already exists, return an error response
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        // register the new user
        UserEntity savedUser = userService.createUser(registerRequest.getFullName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));

        // return a success response
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    /**
     * Endpoint to handle user login requests.
     * @param request a LoginRequest object containing the user's email and password
     * @return a ResponseEntity object with a LoginResponse object in the body if the login is successful,
     * or a ResponseEntity object with an HTTP status of UNAUTHORIZED if the login fails
     * @throws BadCredentialsException if the user's credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("Received request on /login");
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );
            UserEntity user = (UserEntity) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(user);
            LoginResponse response = new LoginResponse(user.getId(), user.getEmail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
