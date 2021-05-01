package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.Recommend;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("select r from Recommend r where " +
            "r.member = :member and r.board = :board and r.category = :category ")
    Optional<Recommend> findByRecommend(
            @Param("member") Member member, @Param("board") Board board, @Param("category") String category);
}
