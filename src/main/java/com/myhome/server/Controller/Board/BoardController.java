package com.myhome.server.Controller.Board;

import com.myhome.server.DTO.BoardWriteDTO;
import com.myhome.server.DTO.CommentDTO;
import com.myhome.server.Entity.Board.*;
import com.myhome.server.Entity.Comment.BoardComment;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Board.BoardRepository;
import com.myhome.server.Repository.Board.CategoryRepository;
import com.myhome.server.Repository.Board.ImageRepository;
import com.myhome.server.Repository.Board.RecommendRepository;
import com.myhome.server.Repository.Comment.CommentRepository;
import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MemberService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bbs")
public class BoardController {

    private final MemberService memberService;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    public BoardController(MemberService memberService, BoardRepository boardRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, RecommendRepository recommendRepository, CommentRepository commentRepository) {
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.recommendRepository = recommendRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/view/{id}")
    public BoardWriteDTO boardDetail(
            @PathVariable("id") Long id,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<Board> findBoard = boardRepository.findById(id);
        if(findBoard.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재 하지 않는 게시글입니다.");
        Board board = findBoard.get();
        BoardWriteDTO boardWriteDTO = new BoardWriteDTO(board);
        return boardWriteDTO;
    }

    @PostMapping("/write")
    public Long writePost(
        @RequestHeader("Authorization") String UID,
        @PathParam("category") String category,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam(name = "image", required = false) List<MultipartFile> files,
        @RequestParam(name = "video", required = false) MultipartFile videoFile,
        @RequestParam(name = "video_url", required = false) String video_url,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        String s = category.toUpperCase();
        Optional<Category> cate = categoryRepository.findByName(CategoryList.valueOf(s));
        if(cate.isEmpty()) s = "FREE";
        Category category1 = cate.get();

        if(s.equals(CategoryList.FREE.toString())){
            Board board = boardInit(title, description, category1, member);
            Board value = boardRepository.save(board);

            return value.getId();
        }else if(s.equals(CategoryList.PHOTO.toString())){
            FileUpload fileUpload = new FileUpload();
            Board board = boardInit(title, description, category1, member);
            Board boardSave = boardRepository.save(board);
            for (MultipartFile multipartFile : files) {
                Image image = new Image();
                Map<String, String> fileValue = fileUpload.Save(multipartFile);
                image.setOriginal_url(fileValue.get("ABSOLUTE_PATH"));
                image.setImage_url(fileValue.get("PATH"));
                image.addImageToBoard(boardSave);
                imageRepository.save(image);
            }
            return boardSave.getId();
        }else if(s.equals(CategoryList.VIDEO.toString())){
            FileUpload fileUpload = new FileUpload();
            fileUpload.setPATHNAME("/board/video/");
            Board board = boardInit(title, description, category1, member);
            Board boardSave = boardRepository.save(board);


            boardRepository.save(boardSave);

            if(video_url != null){
                board.setVideoType(VideoType.YOUTUBE);
                board.setVideo_url(video_url);
            }
            if(videoFile != null ){
                board.setVideoType(VideoType.LOCAL);
                Map<String, String> videoFileSave = fileUpload.Save(videoFile);
                board.setOriginal_url(videoFileSave.get("ABSOLUTE_PATH"));
                board.setVideo_url(videoFileSave.get("PATH"));
            }
            if(video_url == null && videoFile == null){
                board.setVideoType(VideoType.NONE);
            }
            Board Board = boardRepository.save(board);
            return Board.getId();
        }

        return null;
    }
    @GetMapping("/{board}/size")
    public Integer BoardSize(
            @PathVariable("board") String board
    ) throws Exception {
        String board_cate = board.toUpperCase();
        Optional<Category> findCategory = categoryRepository.findByName(CategoryList.valueOf(board_cate));
        if(findCategory.isEmpty()) throw new Exception("존재하지 않는 카테고리입니다.");
        List<Board> byCategory = boardRepository.findByCategory(findCategory.get());
        return byCategory.size();
    }
    @GetMapping("/{board}/get")
    public List<BoardWriteDTO> BoardData(
            @PathVariable("board") String board,
            @PathParam("size") int size,
            @PathParam("page") int page
    ) throws Exception {
        String board_cate = board.toUpperCase();

        Optional<Category> findCategory = categoryRepository.findByName(CategoryList.valueOf(board_cate));
        if(findCategory.isEmpty()) throw new Exception("존재하지 않는 카테고리입니다.");
        PageRequest pageRequest = PageRequest.of(page,size,Sort.by("created").descending());
        Slice<Board> findBoard = boardRepository.findPageByCategory(findCategory.get(),pageRequest);

       if(CategoryList.valueOf(board_cate).equals(CategoryList.PHOTO)){
            List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
            for (Board board2 : findBoard) {
                BoardWriteDTO boardWriteDTO = new BoardWriteDTO(board2);
                boardWriteDTOList.add(boardWriteDTO);
            }
            return boardWriteDTOList;
        }else if(CategoryList.valueOf(board_cate).equals(CategoryList.VIDEO)){
            List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
            for (Board board2 : findBoard) {
                BoardWriteDTO boardWriteDTO = new BoardWriteDTO(board2);
                boardWriteDTOList.add(boardWriteDTO);
            }
            return boardWriteDTOList;
        }else{
            List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
            for (Board board2 : findBoard) {
                BoardWriteDTO boardWriteDTO = new BoardWriteDTO(board2);
                boardWriteDTOList.add(boardWriteDTO);
            }

            return boardWriteDTOList;

        }
    }

    @PutMapping("/update")
    public void updateBoard(
            @RequestHeader("Authorization") String UID,
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(name = "image", required = false) List<MultipartFile> files,
            @RequestParam(name = "video", required = false) MultipartFile videoFile,
            @RequestParam(name = "video_url", required = false) String video_url,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);

        Optional<Board> findBoard = boardRepository.findById(id);
        if(findBoard.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지않는 게시글입니다.");
        Board board = findBoard.get();
        board.setUpdated(LocalDateTime.now());
        board.setTitle(title);
        board.setDescription(description);
        board.setVideo_url(video_url);


        if(video_url != null){
            board.setVideoType(VideoType.YOUTUBE);
            board.setVideo_url(video_url);
        }
        if(videoFile != null ){
            FileUpload fileUpload = new FileUpload();
            fileUpload.setPATHNAME("/board/video/");
            board.setVideoType(VideoType.LOCAL);
            Map<String, String> videoFileSave = fileUpload.Save(videoFile);
            board.setOriginal_url(videoFileSave.get("ABSOLUTE_PATH"));
            board.setVideo_url(videoFileSave.get("PATH"));
        }
        if(video_url == null && videoFile == null){
            board.setVideoType(VideoType.NONE);
        }
        if(files.size() > 0){
            FileUpload fileUpload = new FileUpload();
            for (MultipartFile multipartFile : files) {
                Image image = new Image();
                Map<String, String> fileValue = fileUpload.Save(multipartFile);
                image.setOriginal_url(fileValue.get("ABSOLUTE_PATH"));
                image.setImage_url(fileValue.get("PATH"));
                image.addImageToBoard(board);
                imageRepository.save(image);
            }
            List<Image> imageList = imageRepository.findByBoard(board);
            if(imageList.size() > 0){
                for (Image image : imageList) {
                    image.setState(false);
                }
            }
        }
    }

    @GetMapping("/{board_id}/{member_id}/recommend")
    public void recommend(
        @PathVariable("board_id") Long board_id,
        @PathVariable("member_id") String member_id,
        HttpServletResponse httpServletResponse
    ) throws IOException {
        MemberDetail member = memberService.LoginCheck(member_id, httpServletResponse);
        Optional<Board> findBoard = boardRepository.findById(board_id);
        if(findBoard.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지 않는 게시글입니다.");
        }
        Board board = findBoard.get();
        Optional<Recommend> findRecommend = recommendRepository.findByRecommend(member, board, board.getCategory().getName().toString());
        if(findRecommend.isEmpty()){
            Recommend recommend = new Recommend();
            recommend.setBoard(board);
            recommend.setState(true);
            recommend.setMember(member);
            recommend.setCategory(board.getCategory().getName().toString());
            recommendRepository.save(recommend);
        }else{
            if(findRecommend.get().getState()){
                httpServletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
            }else{
                findRecommend.get().setState(true);
                recommendRepository.save(findRecommend.get());
            }
        }
    }

    @GetMapping("/{board_id}/check/recommend")
    public boolean checkRecommend(
            @PathVariable("board_id") Long board_id,
            @RequestHeader("Authorization") String UID,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        Optional<Board> findBoard = boardRepository.findById(board_id);
        if(findBoard.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지 않는 게시글입니다.");
        }
        Board board = findBoard.get();
        Optional<Recommend> findRecommend = recommendRepository.findByRecommend(member, board, board.getCategory().getName().toString());
        if(findRecommend.isEmpty()){
            return true;
        }else{
            if(findRecommend.get().getState()){
                return false;
            }else{
                return true;
            }
        }
    }

    @PostMapping("/{board_id}/write/comment")
    public CommentDTO writeComment(
        @PathVariable("board_id") Long board_id,
        @RequestHeader("Authorization") String UID,
        @RequestParam("description") String description,
        HttpServletResponse httpServletResponse
    ) throws IOException {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        Optional<Board> findBoard = boardRepository.findById(board_id);
        if(findBoard.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지 않는 게시글입니다.");
        }
        Board board = findBoard.get();
        BoardComment boardComment = new BoardComment();
        boardComment.setCreated(LocalDateTime.now());
        boardComment.setDescription(description);
        boardComment.setUpdated(LocalDateTime.now());
        boardComment.addComment(board);
        boardComment.setMember(member);
        BoardComment save = commentRepository.save(boardComment);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(save.getId());
        commentDTO.setAvatar_url(save.getMember().getAvatar_url());
        commentDTO.setName(save.getMember().getName());
        commentDTO.setBoard_id(save.getBoard().getId());
        commentDTO.setMember_id(save.getMember().getId());
        commentDTO.setUpdated(save.getUpdated());
        commentDTO.setCreated(save.getCreated());
        commentDTO.setDescription(save.getDescription());
        commentDTO.setRank(save.getMember().getRank());

        return commentDTO;
    }

    private Board boardInit(String title, String description, Category category1, MemberDetail member) {
        Board board = new Board();
        board.setTitle(title);
        board.setDescription(description);
        board.setCategory(category1);
        board.setCreated(LocalDateTime.now());
        board.setUpdated(LocalDateTime.now());
        board.setViews(0);
        board.setMember(member);
        return board;
    }


}