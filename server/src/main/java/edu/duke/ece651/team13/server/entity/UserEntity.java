package edu.duke.ece651.team13.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * User
 */
@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @SequenceGenerator(name = "userSeq")
    private Long Id;

    @NotNull(message = "Full Name cannot be empty")
    @Column(name = "FULL_NAME")
    private String fullName;

    @NotNull(message = "Email cannot be empty")
    @Column(name = "EMAIL", unique = true)
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Length(min = 6, message = "Password should be atleast 6 characters long")
    private String password;

    /**
     * This method is part of the UserDetails interface which is
     * used for user authentication and authorization, needs to be
     * implemented to use spring security
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * This method is part of the UserDetails interface.
     * Returns true if the account is not expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * This method is part of the UserDetails interface.
     * Returns whether the user account is non-locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * This method is part of the UserDetails interface.
     * Returns true if the credentials of the account have not expired,
     * false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * This method is part of the UserDetails interface.
     * Indicates whether the user's account is enabled or disabled.
     * A disabled account cannot be authenticated.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
