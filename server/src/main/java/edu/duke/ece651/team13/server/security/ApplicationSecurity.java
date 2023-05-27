package edu.duke.ece651.team13.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Configuration class for application security.
 */
@Configuration
public class ApplicationSecurity {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    /**
     * Provides a bean for BCryptPasswordEncoder.
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a bean for AuthenticationManager.
     * @param myUserDetailsService user details service for authentication
     * @param encoder password encoder
     * @return AuthenticationManager instance
     * @throws Exception if an error occurs
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService myUserDetailsService, PasswordEncoder encoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    /**
     * Configures HttpSecurity for application security.
     * @param http HttpSecurity instance
     * @return SecurityFilterChain instance
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/","/login", "/register", "/createGame/*", "/getGame", "/getGame/*",
                        "/submitOrder", "/getOrders/*", "/getFreeGames", "getGameForUser/*").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                );

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}