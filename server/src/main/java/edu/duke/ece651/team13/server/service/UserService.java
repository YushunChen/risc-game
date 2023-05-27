package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.auth.RegisterRequest;
import edu.duke.ece651.team13.server.entity.UserEntity;

public interface UserService {
    /**
     * This method creates a new User entity and saves it to the database.
     */
    UserEntity createUser(String fullName, String email, String password);

    /**
     * This method finds a user in the database by their id
     *
     * @return the corresponding UserEntity if it exists,
     * throw NoSuchElementException if not exist
     */
    UserEntity getUserById(Long id);

    /**
     * This method finds a user in the database by their email address
     *
     * @return the corresponding UserEntity if it exists,
     * throw NoSuchElementException if not exist
     */
    UserEntity getUserByEmail(String email);

    /**
     * Check if the current user is already present (with the same email)
     *
     * @param registerRequest is the UserInput to check
     * @return null if the user is not present,
     * an error message is the user is present
     */
    String isUserPresent(RegisterRequest registerRequest);

    /**
     * Updates the password of a user.
     */
    UserEntity updateUserPassword(Long id, String password);
}
