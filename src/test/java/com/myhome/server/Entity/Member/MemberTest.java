package com.myhome.server.Entity.Member;

import com.myhome.server.Crypt.Bcrypt;
import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.Category;
import com.myhome.server.Entity.Board.CategoryList;
import com.myhome.server.Repository.Board.BoardRepository;
import com.myhome.server.Repository.Board.CategoryRepository;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.FileUpload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @Autowired
    EntityManager em;
    @Autowired
    Bcrypt bcrypt;
    @Autowired
    MemberDetailRepository memberDetailRepository;
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Test
    public void MemberTest(){
        MemberDetail member = new MemberDetail();
        member.setEmail("test@test.com");
        member.setName("MemberA");
        member.setCreated(LocalDateTime.now());
        member.setUpdated(LocalDateTime.now());

        em.persist(member);
        member.setSESSION_UID(bcrypt.createSessionUid(member.getId()));
        em.flush();
        em.clear();
        MemberDetail memberDetail = em.find(MemberDetail.class, member.getId());
        System.out.println("memberDetail = " + memberDetail.getSESSION_UID());
    }

    @Test
    public void MemberSessionUIDTEST(){
        MemberDetail member = new MemberDetail();
        member.setEmail("test@test.com");
        member.setName("MemberA");
        member.setCreated(LocalDateTime.now());
        member.setUpdated(LocalDateTime.now());

        em.persist(member);
        member.setSESSION_UID(bcrypt.createSessionUid(member.getId()));

        String a = "$2a$10$B6uIVFiSRSXHPII4748GBOzC.ZuVKYizJwsqYw75k6RfjVDQ0XFCC";
        Optional<MemberDetail> bySessionUID = memberDetailRepository.findBySessionUID(member.getSESSION_UID());
        MemberDetail memberDetail = bySessionUID.get();
        String email = memberDetail.getEmail();
        System.out.println("email = " + email);
        Boolean aBoolean = bcrypt.matchesPassword("1111", a);
        System.out.println("aBoolean = " + aBoolean);
    }

    @Test
    public void BoardTest(){
        Optional<Category> byName = categoryRepository.findByName(CategoryList.VIDEO);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,
                "id"));
        Slice<Board> pageByCategory = boardRepository.findPageByCategory(byName.get(), pageRequest);

        System.out.println("pageByCategory = " + pageByCategory.get().count());
        for (Board board : pageByCategory) {
            System.out.println("board = " + board.getTitle());
        }

    }
}