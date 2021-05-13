package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.Category;
import com.myhome.server.Entity.Board.VideoBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<VideoBoard> findByCategory(Category category);

}
