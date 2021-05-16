package com.myhome.server.Controller.Board;

import com.myhome.server.DTO.BoardWriteDTO;
import com.myhome.server.DTO.ImageDTO;
import com.myhome.server.Entity.Board.*;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Board.BoardRepository;
import com.myhome.server.Repository.Board.CategoryRepository;
import com.myhome.server.Repository.Board.ImageRepository;
import com.myhome.server.Repository.Board.RecommendRepository;
import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MemberService;
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
    public BoardController(MemberService memberService, BoardRepository boardRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, RecommendRepository recommendRepository) {
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.recommendRepository = recommendRepository;
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

            Board Board = boardRepository.save(board);
            return Board.getId();
        }

        return null;
    }

    @GetMapping("/{board}/get")
    public List<BoardWriteDTO> BoardData(
            @PathVariable("board") String board
    ) throws Exception {
        String board_cate = board.toUpperCase();

        Optional<Category> findCategory = categoryRepository.findByName(CategoryList.valueOf(board_cate));
        if(findCategory.isEmpty()) throw new Exception("존재하지 않는 카테고리입니다.");
        List<Board> findBoard = boardRepository.findByCategory(findCategory.get());

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
            @PathParam("category") String category,
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(name = "image", required = false) List<MultipartFile> files,
            @RequestParam(name = "video", required = false) MultipartFile videoFile,
            @RequestParam(name = "video_url", required = false) String video_url,
            HttpServletResponse httpServletResponse
    ) throws IOException {

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
//                boardWriteDTO.setId(board2.getId());
//                boardWriteDTO.setWriter(board2.getMember().getName());
//                boardWriteDTO.setCreated(board2.getCreated());
//                boardWriteDTO.setUpdated(board2.getUpdated());
//
//                boardWriteDTO.setTitle(board2.getTitle());
//                boardWriteDTO.setDescription(board2.getDescription());
//                boardWriteDTO.setCategoryList(CategoryList.valueOf(board_cate));
//                boardWriteDTO.setViews(board2.getViews());
//                boardWriteDTO.setRecommend(board2.getRecommendList().size());
//                List<Image> imageList = board2.getImageList();
//                List<ImageDTO> imageDTOList = new ArrayList<>();
//                for (Image image : imageList) {
//                    ImageDTO imageDTO = new ImageDTO();
//                    imageDTO.setId(image.getId());
//                    imageDTO.setImage_url(image.getImage_url());
//                    imageDTO.setOriginal_url(image.getOriginal_url());
//                    imageDTOList.add(imageDTO);
//                }
//                boardWriteDTO.setImageList(imageDTOList);