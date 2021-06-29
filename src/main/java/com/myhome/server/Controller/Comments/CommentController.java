package com.myhome.server.Controller.Comments;

import com.myhome.server.DTO.CommentDTO;
import com.myhome.server.Entity.Comment.BoardComment;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Comment.CommentRepository;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/myApi/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private  final MemberDetailRepository memberDetailRepository;
    public CommentController(CommentRepository commentRepository, MemberService memberService, MemberDetailRepository memberDetailRepository) {
        this.commentRepository = commentRepository;
        this.memberService = memberService;
        this.memberDetailRepository = memberDetailRepository;
    }

    @PostMapping("/nickname/get")
    public Page<CommentDTO> memberIsComments(
            @RequestParam("nickname") String nickname,
            @PathParam("size") String size,
            @PathParam("page") String page,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<MemberDetail> findUser = memberDetailRepository.findByNickname(nickname);
        if(findUser.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지 않는 사용자입니다.");
        MemberDetail memberDetail = findUser.get();
        if(size.isEmpty()) size = "10";
        if(page.isEmpty()) page = "0";
        PageRequest pageRequest = PageRequest.of(Integer.parseInt(page) ,Integer.parseInt(size), Sort.by("created").descending());
        Page<BoardComment> findMember = commentRepository.findByMember(memberDetail, pageRequest);
        return findMember.map(CommentDTO::new);
    }

    @DeleteMapping("/delete/{board_id}/{id}")
    public void deleteComments(
            @PathVariable("board_id") Long board_id,
            @PathVariable("id") Long id,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<BoardComment> findComments = commentRepository.findById(id);
        if(findComments.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재 하지 않는 댓글입니다.");
        if(findComments.get().getBoard().getId().equals(board_id)){
            commentRepository.delete(findComments.get());
        }else{
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"게시판 정보가 올바르지 않습니다.");
        }
    }

    @PutMapping("/{id}/update")
    public void updateComments(
            @PathVariable("id") Long id,
            @RequestParam(name = "description") String  description,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<BoardComment> findComments = commentRepository.findById(id);
        if(findComments.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재 하지 않는 댓글입니다.");

        BoardComment com = findComments.get();
        com.setUpdated(LocalDateTime.now());
        com.setDescription(description);
        commentRepository.save(com);
    }
}
