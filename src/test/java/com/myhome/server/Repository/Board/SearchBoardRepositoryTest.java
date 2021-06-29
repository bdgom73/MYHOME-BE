package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Chat.Room;
import com.myhome.server.Repository.Chat.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class SearchBoardRepositoryTest {

    @Autowired
    private SearchBoardRepository searchBoardRepository;

    @Autowired
    private RoomRepository roomRepository;
    @Test
    public void searchTest(){
        List<String> texts = new ArrayList<>();
        texts.add("test");
        texts.add("브레이브");
        PageRequest pageRequest = PageRequest.of(0,10,Sort.by("created").descending().and(Sort.by("board_id")));
        List<String> keywords = new ArrayList<>();
        keywords.add("싸이버거");
        List<Board> searchTitle = searchBoardRepository.findByKeyword(texts,pageRequest);
        for (Board board : searchTitle) {
            System.out.println("board = " + board.getTitle());
        }
//        System.out.println("searchTitle = " + searchTitle);
    }
    @Test
    public void randomTest(){
        List<Room> byRandomData = roomRepository.findByRandomData();
        System.out.println("byRandomData = " + byRandomData);
    }
}