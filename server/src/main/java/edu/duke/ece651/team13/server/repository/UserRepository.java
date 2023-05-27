package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Spring Data Repository for UserEntity
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    /**
     * Finds a UserEntity by email address.
     * @param email the email address to search for
     * @return an Optional containing the UserEntity, or an empty Optional if no user was found
     */
    Optional<UserEntity> findByEmail(String email);
}
