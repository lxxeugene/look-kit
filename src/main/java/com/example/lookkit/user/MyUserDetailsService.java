package com.example.lookkit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserMapper UserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO user = UserMapper.getUserByUuid(username);
        if(user != null ) {
            System.out.println(user.getUserUuid()+user.getAddress()+user.getGender());
        }
        if (user == null) {
            throw new UsernameNotFoundException("해당 아이디를 가진 사용자가 없습니다: " + username);
        }
        List<GrantedAuthority> 권한목록 = new ArrayList<>();
        권한목록.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(user.getUserUuid(), user.getPassword(), 권한목록);
    }
} 