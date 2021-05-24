package com.myhome.server.Crypt;

import com.myhome.server.Entity.Word.FilterText;
import com.myhome.server.Repository.Word.FilterTextRepository;
import com.myhome.server.Service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class BcryptTest {

    @Autowired
    Bcrypt bcrypt;
    @Autowired
    MemberService memberService;
    @Autowired
    FilterTextRepository filterTextRepository;
    @Test
    public void encodeTest(){
        String s = bcrypt.passwordEncoder("1");
        Boolean aBoolean = bcrypt.matchesPassword("1", s);
        System.out.println("aBoolean = " + aBoolean);
    }

    @Test
    public void RexTest(){
        Optional<FilterText> filter = filterTextRepository.findByKinds("filter");
        System.out.println("filter = " + filter.get().getText());
    }



}