package com.citizenv.app.secirity;

import com.citizenv.app.entity.User;
import com.citizenv.app.exception.ResourceNotFoundException;
import com.citizenv.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );
        return new CustomUserDetail(user);

    }
}
