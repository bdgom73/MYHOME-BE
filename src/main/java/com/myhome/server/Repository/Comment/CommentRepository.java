package com.myhome.server.Repository.Comment;

import com.myhome.server.Entity.Comment.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<BoardComment,Long> {
}
