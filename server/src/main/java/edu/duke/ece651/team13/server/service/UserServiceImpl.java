package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.auth.RegisterRequest;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    final UserRepository repository;

    @Override
    @Transactional
    public UserEntity createUser(String fullName, String email, String password) {
        UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        return repository.save(user);
    }

    @Override
    public UserEntity getUserById(Long id) {
        Optional<UserEntity> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        Optional<UserEntity> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String isUserPresent(RegisterRequest registerRequest) {
        Optional<UserEntity> existingUserEmail = repository.findByEmail(registerRequest.getEmail());
        if (existingUserEmail.isPresent()) {
            return "An account with the same email already exists!";
        }
        return null;
    }

    @Override
    @Transactional
    public UserEntity updateUserPassword(Long id, String password) {
        UserEntity user = getUserById(id);
        user.setPassword(password);
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
