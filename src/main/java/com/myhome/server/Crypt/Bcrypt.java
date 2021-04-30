package com.myhome.server.Crypt;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Bcrypt {

    BCryptPasswordEncoder pass = new BCryptPasswordEncoder();

    public String passwordEncoder(String password){
        return pass.encode(password);
    }

    public Boolean matchesPassword(String password, String hashedPassword){
        return pass.matches(password, hashedPassword);
    }

    public String createSessionUid(Long id){
        return pass.encode(id.toString());
    }

}
