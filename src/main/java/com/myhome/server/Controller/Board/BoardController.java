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
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MemberService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/myApi/bbs")
public class BoardController {

    private final MemberService memberService;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final MemberDetailRepository memberDetailRepository;
    public BoardController(MemberService memberService, BoardRepository boardRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, RecommendRepository recommendRepository, CommentRepository commentRepository, MemberDetailRepository memberDetailRepository) {
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.recommendRepository = recommendRepository;
        this.commentRepository = commentRepository;
        this.memberDetailRepository = memberDetailRepository;
    }

    @GetMapping("/view/{id}/{category}")
    public BoardWriteDTO boardDetail(
            @PathVariable("id") Long id,
            @PathVariable("category") String category,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        String s = category.toUpperCase();
        Optional<Category> findCategory = categoryRepository.findByName(CategoryList.valueOf(s));
        if(findCategory.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"해당 카테고리는 없습니다.");
        Optional<Board> findBoard = boardRepository.findByIdAndCategory(id,findCategory.get());

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
        @RequestParam("keyword") List<String> keywordlist,
        @RequestParam(name = "images[]" , required = false) MultipartFile[] files,
        @RequestParam(name = "video", required = false) MultipartFile videoFile,
        @RequestParam(name = "video_url", required = false) String video_url,
        @RequestParam(name = "category_type", required = false) String category_type,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        String s = category.toUpperCase();
        Optional<Category> cate = categoryRepository.findByName(CategoryList.valueOf(s));
        if(cate.isEmpty()) s = "FREE";
        Category category1 = cate.get();
        StringBuilder keywordString = new StringBuilder();
        for(int i =0 ; i<keywordlist.size() ; i++){
            if(i == (keywordlist.size()-1)){
                keywordString.append(keywordlist.get(i));
            }else{
                keywordString.append(keywordlist.get(i)).append(",");
            }
        }
        if(s.equals(CategoryList.FREE.toString())){
            Board board = boardInit(title, description, category1, member,keywordString.toString());
            Board value = boardRepository.save(board);

            return value.getId();
        }else if(s.equals(CategoryList.PHOTO.toString())){
            FileUpload fileUpload = new FileUpload();
            Board board = boardInit(title, description, category1, member, keywordString.toString());
            Board boardSave = boardRepository.save(board);
            for (MultipartFile multipartFile : files) {
                Image image = new Image();
                Map<String, String> fileValue = fileUpload.Save(multipartFile);
                image.setOriginal_url(fileValue.get("ABSOLUTE_PATH"));
                image.setImage_url(fileValue.get("PATH"));
                image.setFilename(fileValue.get("FILENAME"));
                image.addImageToBoard(boardSave);
                imageRepository.save(image);
            }
            return boardSave.getId();
        }else if(s.equals(CategoryList.VIDEO.toString())){
            FileUpload fileUpload = new FileUpload();
            fileUpload.setPATHNAME("/static/board/video/");
            Board board = boardInit(title, description, category1, member,keywordString.toString());
            if(video_url != null){
                switch (category_type) {
                    case "TWITCH":
                        board.setVideoType(VideoType.TWITCH);
                        break;
                    case "YOUTUBE":
                        board.setVideoType(VideoType.YOUTUBE);
                        break;
                    case "AFREECA":
                        board.setVideoType(VideoType.AFREECA);
                        break;
                    default:
                        board.setVideoType(VideoType.NONE);
                        break;
                }
                board.setVideo_url(video_url);
            }
            if(videoFile != null){
                board.setVideoType(VideoType.LOCAL);
                Map<String, String> videoFileSave = fileUpload.Save(videoFile);
                board.setOriginal_url(videoFileSave.get("ABSOLUTE_PATH"));
                board.setVideo_url(videoFileSave.get("PATH"));
            }
            if(video_url == null && videoFile == null){
                board.setVideoType(VideoType.NONE);
            }
            Board board1 = boardRepository.save(board);
            return board1.getId();
        }

        return null;
    }

    @PostMapping("/nickname/member/get")
    public Page<BoardWriteDTO> memberIsComments(
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
        Page<Board> findMember = boardRepository.findByMember(memberDetail, pageRequest);
        return findMember.map(BoardWriteDTO::new);
    }

    @GetMapping("/{sort}/top10")
    public Object BoardTop10(
            @PathVariable("sort") String sort
    ){
        switch (sort){
            case "date" :
                PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("created").descending());
                Slice<Board> findCreated = boardRepository.findAll(pageRequest);
                return findCreated.map(BoardWriteDTO::new).getContent();
            case "views" :
                PageRequest pageRequest2 = PageRequest.of(0, 10, Sort.by("views").descending());
                Slice<Board> findViews = boardRepository.findAll(pageRequest2);
                return findViews.map(BoardWriteDTO::new).getContent();
            case "recommend" :
                List<Board> all = boardRepository.findAll();
                all.sort((o1, o2) -> o2.getRecommendList().size() - o1.getRecommendList().size());
                List<Board> boards = all.subList(0, 10);
                return boards.stream().map(BoardWriteDTO::new);
            default:
                PageRequest pageRequest4 = PageRequest.of(0, 10, Sort.by("id").descending());
                Slice<Board> findId = boardRepository.findAll(pageRequest4);
                return findId.map(BoardWriteDTO::new).getContent();
        }
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

    @PutMapping("/update/upload")
    public void updateVideoBoard(
            @RequestParam("id") Long id,
            @RequestParam(name = "video", required = false) MultipartFile videoFile,
            @RequestParam(name = "video_url", required = false) String video_url,
            @RequestParam(name = "category_type", required = false) String category_type,
            @RequestHeader("Authorization") String UID,
            @RequestParam("category") String category,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        memberService.LoginCheck(UID, httpServletResponse);
        String s = category.toUpperCase();
        Optional<Board> findBoard = boardRepository.findById(id);
        if(findBoard.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지않는 게시글입니다.");
        Board board = findBoard.get();
        if(videoFile != null && video_url == null && CategoryList.valueOf(s).equals(CategoryList.VIDEO)){
            FileUpload fileUpload = new FileUpload();
            fileUpload.setPATHNAME("/static/board/video/");
            Map<String, String> videoPath = fileUpload.Save(videoFile);
            board.setVideoType(VideoType.LOCAL);
            board.setVideo_url(videoPath.get("PATH"));
            board.setOriginal_url(videoPath.get("ABSOLUTE_PATH"));
        }

        if(video_url != null && videoFile == null &&  CategoryList.valueOf(s).equals(CategoryList.VIDEO)){
            switch (category_type.toUpperCase()) {
                case "TWITCH":
                    board.setVideoType(VideoType.TWITCH);
                    break;
                case "YOUTUBE":
                    board.setVideoType(VideoType.YOUTUBE);
                    break;
                case "AFREECA":
                    board.setVideoType(VideoType.AFREECA);
                    break;
                default:
                    board.setVideoType(VideoType.NONE);
                    break;
            }
            board.setVideo_url(video_url);
        }
        boardRepository.save(board);
    }
    @PutMapping("/update")
    public void updateBoard(
            @RequestHeader("Authorization") String UID,
            @RequestParam("category") String category,
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(name = "images[]", required = false) MultipartFile[] files,
            @RequestParam(name = "pre_image", required = false) Long[] preImageList ,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        memberService.LoginCheck(UID, httpServletResponse);
        String s = category.toUpperCase();
        Optional<Board> findBoard = boardRepository.findById(id);
        if(findBoard.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지않는 게시글입니다.");
        Board board = findBoard.get();
        board.setUpdated(LocalDateTime.now());
        board.setTitle(title);
        board.setDescription(description);

        try{
            if(files != null && CategoryList.valueOf(s).equals(CategoryList.PHOTO)){
                FileUpload fileUpload = new FileUpload();
                for (MultipartFile multipartFile : files) {
                    Image image = new Image();
                    Map<String, String> fileValue = fileUpload.Save(multipartFile);
                    image.setOriginal_url(fileValue.get("ABSOLUTE_PATH"));
                    image.setImage_url(fileValue.get("PATH"));
                    image.setFilename(multipartFile.getName());
                    image.addImageToBoard(board);
                    imageRepository.save(image);
                }

                List<Image> imageList = imageRepository.findByBoard(board);

                if( !imageList.isEmpty() && preImageList[0] != null){
                    for (Image image : imageList) {
                        for(Long image_id : preImageList){
                            if(image.getId().equals(image_id)){
                                image.setState(false);
                            }
                        }
                    }
                }
            }else if(files == null && preImageList != null && CategoryList.valueOf(s).equals(CategoryList.PHOTO)){

                List<Image> imageList = imageRepository.findByBoard(board);

                if( !imageList.isEmpty()){
                    for (Image image : imageList) {
                        for(Long image_id : preImageList){
                            System.out.println("image_id = " + image_id);
                            System.out.println("image_id = " + image.getId());
                            if(image.getId().equals(image_id)){
                                image.setState(false);
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException e){
            System.out.println("");
        }
        boardRepository.save(board);
    }

    @DeleteMapping("/delete")
    public void deleteBoard(
            @PathParam("id") Long id,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        System.out.println("id = " + id);
        Optional<Board> findBoard = boardRepository.findById(id);
        if(findBoard.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"존재하지않는 게시글입니다.");
        Board board = findBoard.get();

        boardRepository.delete(board);
    }
    @GetMapping("/{board_id}/recommend")
    public void recommend(
        @PathVariable("board_id") Long board_id,
        @RequestHeader("Authorization") String UID,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
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
    ) throws Exception {
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
    ) throws Exception {
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
        commentDTO.setNickname(save.getMember().getNickname());
        return commentDTO;
    }

    @PostMapping("/ckeditor/upload")
    public Object CKEditorImageUpload(
           @RequestParam("file") MultipartFile file
    ) throws IOException, SizeLimitExceededException {
        Map<String,Object> response = new HashMap<>();
        if(file.getSize() > 52428800 ) {
            throw new SizeLimitExceededException("사이즈가 너무 큽니다");
        }
        FileUpload fileUpload = new FileUpload();
        fileUpload.setPATHNAME("/static/board_images/");

        String s = fileUpload.ImageToBase64(file);
        response.put("url",s);
        response.put("access",true);
        return response;
    }
    private Board boardInit(String title, String description, Category category1, MemberDetail member,String keywords) {
        Board board = new Board();
        board.setTitle(title);
        board.setDescription(description);
        board.setCategory(category1);
        board.setCreated(LocalDateTime.now());
        board.setUpdated(LocalDateTime.now());
        board.setViews(0);
        board.addMember(member);
        board.setKeywords(keywords);
        return board;
    }


}