package com.nawid.ToDo.config;

import com.nawid.ToDo.security.JwtAuthEntryPoint;
import com.nawid.ToDo.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    private JwtAuthEntryPoint authEntryPoint;
    private JwtAuthFilter jwtAuthFilter;

    public SpringSecurityConfig(UserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers( "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authEntryPoint));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService); // your custom service
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
// THIS IS FOR IN MEMORY AUTH
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//
//    }
}
