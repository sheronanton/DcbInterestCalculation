//package com.twad.interestCalculator.config;
//
////SecurityConfig.java
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     http
//         .csrf(csrf -> csrf.disable())
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/api/auth/login").permitAll()
//             .anyRequest().authenticated()
//         )
//         .httpBasic(httpBasic -> {}); // for testing other endpoints
//
//     return http.build();
// }
//
// @Bean
// public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//     UserDetails user = User.withUsername("sheronanton")
//             .password(encoder.encode("password123"))
//             .roles("USER")
//             .build();
//     return new InMemoryUserDetailsManager(user);
// }
//
// @Bean
// public PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();
// }
//
// @Bean
// public AuthenticationManager authenticationManager(
//         AuthenticationConfiguration config) throws Exception {
//     return config.getAuthenticationManager();
// }
//}
