package security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Map<String,UserDetails> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var  user = users.get(username);
        if(user == null){ throw new UsernameNotFoundException(username);}
        return user;
    }

    public void save(UserDetails user){
        users.put(user.getUsername(),user);
    }

    public boolean exists(String username) {
        return users.containsKey(username);
    }

    @PostConstruct
    public void init(){
        save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin222"))
                .roles("ADMIN")
                .build());
    }

}
