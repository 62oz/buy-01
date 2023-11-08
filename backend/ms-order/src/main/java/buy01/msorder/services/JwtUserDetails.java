package buy01.msorder.services;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

    private String userId;

    public JwtUserDetails(String userId) {
        this.userId = userId;
    }

    @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
        // For now this is not used because the whole point is to just get the userId in principal
       return Collections.emptyList();
   }

   @Override
   public String getPassword() {
       return null;
   }

   @Override
   public String getUsername() {
       return userId;
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

   public String getUserId() {
       return userId;
   }
}
