package vttp.csf.Final.Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    //@Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepo.findUserByUsername(username);
        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username:"+username);
        }
        else {
            User user = opt.get();
            return new org.springframework.security
                    .core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
                    true, true, true, getAuthorities("USER"));
        }

    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
