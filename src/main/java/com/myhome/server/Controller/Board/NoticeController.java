package com.myhome.server.Controller.Board;

import com.myhome.server.DTO.BoardWriteDTO;
import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Repository.Board.BoardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/myApi/notice")
public class NoticeController {

    private final BoardRepository boardRepository;

    public NoticeController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    @GetMapping("/notice/get")
    public List<BoardWriteDTO> getNotice(
            @PathParam("top") Integer top,
            @PathParam("size") int size,
            @PathParam("page") int page
    ){
        PageRequest pageRequest = null;

        if(top != null){
             pageRequest = PageRequest.of(0,10, Sort.by("created").descending());
        }

        pageRequest = PageRequest.of(page, size, Sort.by("created").descending());

        Slice<Board> findNotice = boardRepository.findPageByNotice(Boolean.TRUE, pageRequest);
        List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
        for (Board board : findNotice) {
            BoardWriteDTO boardWriteDTO = new BoardWriteDTO(board);
            boardWriteDTOList.add(boardWriteDTO);
        }
        return boardWriteDTOList;
    }
}
