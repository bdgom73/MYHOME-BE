package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.Category;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

    List<Board> findByCategory(Category category);

    Optional<Board> findById(Long id);

    Slice<Board> findPageByCategory(Category category, Pageable pageable);

    Optional<Board> findByIdAndCategory(Long id, Category category);

    Slice<Board> findPageByNotice(Boolean notice, Pageable pageable);

    Page<Board> findAll(Pageable pageable);

    Page<Board> findByTitleContaining(String title,Pageable pageable);

    Page<Board> findByTitleLike(String title,Pageable pageable);

    Page<Board> findByTitleStartingWith(String title,Pageable pageable);

    Page<Board> findByMember(Member member, Pageable pageable);

}
