package com.djt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;

    private List<String> permission;


    @Override
//    返回权限集合
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return permission.stream()
//                .map(users::new)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    class users implements GrantedAuthority{
        String role;
        public users(String role){
            this.role=role;
        }
        @Override
        public String getAuthority() {
            return role;
        }
    }
}
