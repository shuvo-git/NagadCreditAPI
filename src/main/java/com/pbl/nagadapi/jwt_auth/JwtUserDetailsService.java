package com.pbl.nagadapi.jwt_auth;

import com.pbl.nagadapi.ApiUserPrincipal;
import com.pbl.nagadapi.repository.ApiUserRepo;
import com.pbl.nagadapi.entity.ApiUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ApiUserRepo apiUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApiUsers apiUsers = apiUserRepo.findByUsername(username);
        System.out.println(apiUsers.toString());

        if(apiUsers==null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        else return new ApiUserPrincipal(apiUsers);
        /*if ("javainuse".equals(username)) {
            return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }*/
    }
}
