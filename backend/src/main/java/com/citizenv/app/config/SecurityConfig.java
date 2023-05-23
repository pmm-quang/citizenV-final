package com.citizenv.app.config;

import com.citizenv.app.secirity.CustomUserService;
import com.citizenv.app.secirity.jwt.JwtFilter;
import com.citizenv.app.secirity.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private final CustomUserService service;

//    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(CustomUserService service) {
        this.service = service;
//        this.tokenProvider = tokenProvider;
    }

    @Bean
    public JwtFilter authenticationJwtTokenFilter() {
        return new JwtFilter();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(service);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }


    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .antMatchers("/api/v1/auth/login").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/v1/province/**").hasAuthority("READ")
//                .antMatchers( "**/province/save", "**/province/save/**").hasRole("A1")
//                .antMatchers("**/district/save", "**/district/save/**", "**/district/by-province").hasRole("A2")
//                .antMatchers("**/ward/save", "**/ward/save/**", "**/ward/by-district").hasRole("A3")
//                .antMatchers("**/hamlet/save", "**/hamlet/save/**", "**/hamlet/by-ward").hasRole("B1")
//                .antMatchers("**/citizen/save","**/citizen/save/**").hasAnyRole("B1", "B2")
//                .antMatchers("**/user/save").hasAnyRole("A1", "A2", "A3", "B1")
//                .antMatchers("**/user/change-password/**").hasAnyRole("A1", "A2", "A3", "B1", "B2")
                .anyRequest().authenticated();
//                .anyRequest().permitAll()
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");
        return http.build();
    }
}
