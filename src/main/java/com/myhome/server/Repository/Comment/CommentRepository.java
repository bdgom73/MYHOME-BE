package com.myhome.server.Repository.Comment;

import com.myhome.server.Entity.Comment.BoardComment;
import com.myhome.server.Entity.Member.Member;
import com.myhome.server.Entity.Member.MemberDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<BoardComment,Long> {

    Page<BoardComment> findByMember(Member member,Pageable pageable);

}
