package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.Recommend;
import com.myhome.server.Entity.Member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecommendRepositoryTest {

    @Autowired
    RecommendRepository recommendRepository;

    @Autowired
    EntityManager em;
    @Test
    public void recommendTest(){
        Member member = new Member();
        member.setEmail("A");
        em.persist(member);
        Board board = new Board();
        board.setTitle("A");
        em.persist(board);
        Optional<Recommend> free = recommendRepository.findByRecommend(member, board, "free");
        System.out.println("free = " + free);
    }
}