package com.myhome.server.Controller.Board;

import com.myhome.server.DTO.BoardWriteDTO;
import com.myhome.server.DTO.ImageDTO;
import com.myhome.server.Entity.Board.*;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Board.BoardRepository;
import com.myhome.server.Repository.Board.CategoryRepository;
import com.myhome.server.Repository.Board.ImageRepository;
import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MemberService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
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

    public BoardController(MemberService memberService, BoardRepository boardRepository, CategoryRepository categoryRepository, ImageRepository imageRepository) {
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/write")
    public Long writePost(
        @RequestHeader("Authorization") String UID,
        @PathParam("category") String category,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam(value = "image", required = false) List<MultipartFile> files,
        @RequestParam(value = "video", required = false) MultipartFile videoFile,
        @RequestParam(value = "video_url", required = false) String video_url,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        String s = category.toUpperCase();
        Optional<Category> cate = categoryRepository.findByName(s);
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
            VideoBoard board = new VideoBoard();
            if(!video_url.isEmpty()){
                board.setVideoType(VideoType.YOUTUBE);
                board.setVideo_url(video_url);
            }
            if(!videoFile.isEmpty()){
                board.setVideoType(VideoType.LOCAL);
                Map<String, String> videoFileSave = fileUpload.Save(videoFile);
                board.setOriginal_url(videoFileSave.get("ABSOLUTE_PATH"));
                board.setVideo_url(videoFileSave.get("PATH"));
            }
            board.setTitle(title);
            board.setDescription(description);
            board.setCategory(category1);
            board.setCreated(LocalDateTime.now());
            board.setUpdated(LocalDateTime.now());
            board.setViews(0);
            board.setMember(member);
            VideoBoard videoBoard = boardRepository.save(board);
            return videoBoard.getId();
        }

        return null;
    }

    @GetMapping("/{board}/get")
    public List<BoardWriteDTO> BoardData(
            @PathVariable ("board") String board
    ) throws Exception {
        String board_cate = board.toUpperCase();
        CategoryList category = CategoryList.FREE;
        if(board_cate.equals("PHOTO")) category = CategoryList.PHOTO;
        if(board_cate.equals("VIDEO")) category = CategoryList.VIDEO;

        Optional<Category> findCategory = categoryRepository.findByName(category.toString());
        if(findCategory.isEmpty()) throw new Exception("존재하지 않는 카테고리입니다.");
        List<VideoBoard> findBoard = boardRepository.findByCategory(findCategory.get());

       if(category.equals(CategoryList.PHOTO)){
            List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
            for (VideoBoard videoBoard : findBoard) {
                BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
                boardWriteDTO.setTitle(videoBoard.getTitle());
                boardWriteDTO.setDescription(videoBoard.getDescription());
                boardWriteDTO.setCategoryList(category);

                List<Image> imageList = videoBoard.getImageList();
                List<ImageDTO> imageDTOList = new ArrayList<>();
                for (Image image : imageList) {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setId(image.getId());
                    imageDTO.setImage_url(image.getImage_url());
                    imageDTO.setOriginal_url(image.getOriginal_url());
                    imageDTOList.add(imageDTO);
                }
                boardWriteDTO.setImageList(imageDTOList);
                boardWriteDTOList.add(boardWriteDTO);
            }
            return boardWriteDTOList;
        }else if(category.equals(CategoryList.VIDEO)){
            List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
            for (VideoBoard videoBoard : findBoard) {
                BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
                boardWriteDTO.setTitle(videoBoard.getTitle());
                boardWriteDTO.setDescription(videoBoard.getDescription());
                boardWriteDTO.setCategoryList(category);
                boardWriteDTO.setVideo_url(videoBoard.getVideo_url());
                boardWriteDTO.setVideoType(videoBoard.getVideoType());
                boardWriteDTOList.add(boardWriteDTO);
            }
            return boardWriteDTOList;
        }else{
            List<BoardWriteDTO> boardWriteDTOList = new ArrayList<>();
            for (VideoBoard videoBoard : findBoard) {
                BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
                boardWriteDTO.setTitle(videoBoard.getTitle());
                boardWriteDTO.setDescription(videoBoard.getDescription());
                boardWriteDTO.setCategoryList(category);
                boardWriteDTOList.add(boardWriteDTO);
            }
            return boardWriteDTOList;
        }

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
