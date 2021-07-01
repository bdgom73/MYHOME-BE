package com.myhome.server.Controller.Board;

import com.myhome.server.DTO.BoardWriteDTO;
import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Repository.Board.BoardRepository;
import com.myhome.server.Repository.Board.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/myApi")
public class HomeController {

    private final BoardRepository boardRepository;
    private final SearchBoardRepository searchBoardRepository;
    public HomeController(BoardRepository boardRepository, SearchBoardRepository searchBoardRepository) {
        this.boardRepository = boardRepository;
        this.searchBoardRepository = searchBoardRepository;
    }

    @GetMapping("/search")
    public List<BoardWriteDTO> search(
            @PathParam("term") String term,
            @PathParam("page") int page,
            @PathParam("size") int size,
            @PathParam("sort") String sort,
            @PathParam("sub_sort") String sub_sort
    ){
        Sort s ;
        if ("views".equals(sub_sort)) {
            s = Sort.by("views").descending();
        } else {
            s = Sort.by("created").descending();
        }
        PageRequest pageRequest = PageRequest.of(page, size, s);
        List<String> kw = new ArrayList<>();
        if(term.contains(" ")){
            String[] terms = term.split(" ");
            kw.addAll(Arrays.asList(terms));
        }else{
            kw.add(term);
        }
        if ("start".equals(sort)) {
            Page<Board> boards3 = boardRepository.findByTitleStartingWith(term, pageRequest);
            return boards3.map(BoardWriteDTO::new).getContent();
        }
        List<Board> boards2 = searchBoardRepository.findByKeyword(kw, pageRequest);
        return boards2.stream().map(BoardWriteDTO::new).collect(Collectors.toList());


    }
}
