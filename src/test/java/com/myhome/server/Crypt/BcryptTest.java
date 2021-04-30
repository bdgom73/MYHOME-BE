package com.myhome.server.Crypt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class BcryptTest {

    @Autowired
    Bcrypt bcrypt;
    @Test
    public void encodeTest(){
        String s = bcrypt.passwordEncoder("1");
        Boolean aBoolean = bcrypt.matchesPassword("1", s);
        System.out.println("aBoolean = " + aBoolean);
    }
}