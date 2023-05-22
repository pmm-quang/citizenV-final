package com.citizenv.app.config;

import com.citizenv.app.secirity.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.FilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    private CustomUserService service;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(service)
                .passwordEncoder(encoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                .antMatchers("/api/v1/auth/login").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/v1/province/**").hasAuthority("READ")
//                .antMatchers( "**/province/save", "**/province/save/**").hasRole("A1")
//                .antMatchers("**/district/save", "**/district/save/**", "**/district/by-province").hasRole("A2")
//                .antMatchers("**/ward/save", "**/ward/save/**", "**/ward/by-district").hasRole("A3")
//                .antMatchers("**/hamlet/save", "**/hamlet/save/**", "**/hamlet/by-ward").hasRole("B1")
//                .antMatchers("**/citizen/save","**/citizen/save/**").hasAnyRole("B1", "B2")
//                .antMatchers("**/user/save").hasAnyRole("A1", "A2", "A3", "B1")
//                .antMatchers("**/user/change-password/**").hasAnyRole("A1", "A2", "A3", "B1", "B2")
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
                .and().httpBasic();
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");
        return http.build();
    }
}
