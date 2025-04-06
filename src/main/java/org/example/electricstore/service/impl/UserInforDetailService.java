package org.example.electricstore.service.impl;

import org.example.electricstore.model.User;
import org.example.electricstore.model.UserRole;
import org.example.electricstore.repository.IUserRepository;
import org.example.electricstore.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInforDetailService implements UserDetailsService {
    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IUserRoleRepository iUserRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<UserRole> userRoles = iUserRoleRepository.findAllByUser(user);
        return new UserInfoUserDetails(user, userRoles);
    }
    public User saveUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty!");
        }
        if (iUserRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username '" + user.getUsername() + "' already exists!");
        }

        return iUserRepository.save(user);
    }

}