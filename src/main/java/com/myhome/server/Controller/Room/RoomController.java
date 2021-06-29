package com.myhome.server.Controller.Room;

import com.myhome.server.DTO.RoomDTO;
import com.myhome.server.Entity.Chat.JoinRoom;
import com.myhome.server.Entity.Chat.Room;
import com.myhome.server.Entity.Chat.RoomType;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Chat.JoinRoomRepository;
import com.myhome.server.Repository.Chat.RoomRepository;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.MemberService;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/myApi/room")
public class RoomController {

    private final MemberService memberService;
    private final RoomRepository roomRepository;
    private final JoinRoomRepository joinRoomRepository;
    private final MemberDetailRepository memberDetailRepository;

    public RoomController(MemberService memberService, RoomRepository roomRepository, JoinRoomRepository joinRoomRepository, MemberDetailRepository memberDetailRepository) {
        this.memberService = memberService;
        this.roomRepository = roomRepository;
        this.joinRoomRepository = joinRoomRepository;
        this.memberDetailRepository = memberDetailRepository;
    }

    @PostMapping("/make/room/")
    public void createRoom(
        @RequestHeader("Authorization") String UID,
        @RequestParam(value = "title") String title,
        @RequestParam(value = "type") String type,
        @RequestParam(value = "password", required = false) String password,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        Room room = new Room();
        if(type.equals("public")){
            room.setPassword(password);
        }
        room.setTitle(title);
        room.setType(RoomType.valueOf(type.toUpperCase()));
        room.setLeader(member);
        room.setCreated(LocalDateTime.now());

        roomRepository.save(room);

        JoinRoom joinRoom = new JoinRoom();
        joinRoom.setRoom(room);
        joinRoom.setJoinDate(LocalDateTime.now());
        joinRoom.setJoinState(true);
        joinRoom.setConnectionStatus(false);
        joinRoom.setMember(member);

        joinRoomRepository.save(joinRoom);
    }

    @GetMapping("/random/read")
    public List<RoomDTO> randomReadRooms(){
        List<Room> findRooms = roomRepository.findByRandomData();
        return findRooms.stream().map(RoomDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<RoomDTO> readLists(
            @PathParam("condition") String condition,
            @PathParam("term") String term
    ){
        if(condition.equals("nickname")){
            Optional<MemberDetail> findNickname = memberDetailRepository.findByNickname(term);
            if(findNickname.isEmpty()){
                return new ArrayList<>();
            }
            Slice<Room> findNicknameList = roomRepository.findByLeader(findNickname.get());
            return findNicknameList.map(RoomDTO::new).toList();
        }else{
            Slice<Room> fintTitle = roomRepository.findByTitleContaining(term);
            return fintTitle.map(RoomDTO::new).toList();
        }

    }

    @GetMapping("/join/{room}")
    public Map<String, Object> currentRoom(
            @PathVariable("room") Long room,
            @RequestHeader("Authorization") String UID,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        Optional<Room> findId = roomRepository.findById(room);
        if(findId.isEmpty()) throw new Exception("존재하지 않는 방입니다.");
        Map<String ,Object> result = new HashMap<>();
        boolean check = Boolean.FALSE;
        String message = "";
        if(findId.get().getType().equals(RoomType.PUBLIC)){
            check = Boolean.TRUE;
        }else{
            Optional<JoinRoom> findJoinRoom = joinRoomRepository.findByRoomAndJoinStateAndMember(findId.get(), true, member);
            if(findJoinRoom.isPresent()){
                check = Boolean.TRUE;
            }
        }
        result.put("result", check);
        result.put("room", new RoomDTO(findId.get()));
        result.put("message", message);
        return result;
    }
}
