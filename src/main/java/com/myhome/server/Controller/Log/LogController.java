package com.myhome.server.Controller.Log;

import com.myhome.server.DTO.LoginLogDTO;
import com.myhome.server.Entity.Log.LoginLog;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Log.LoginLogRepository;
import com.myhome.server.Service.MemberService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/myApi/log")
public class LogController {

    private final LoginLogRepository loginLogRepository;
    private final MemberService memberService;
    public LogController(LoginLogRepository loginLogRepository, MemberService memberService) {
        this.loginLogRepository = loginLogRepository;
        this.memberService = memberService;
    }

    @GetMapping("/login/top={top}")
    public List<LoginLogDTO> LoginTopN(
            @PathVariable("top") int top,
            @RequestHeader("Authorization") String UID,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        PageRequest pageRequest = PageRequest.of(0, top, Sort.by("loginDate").descending());
        Slice<LoginLog> findMember = loginLogRepository.findByMember(member, pageRequest);
        List<LoginLog> content = findMember.getContent();
        return content.stream().map(LoginLogDTO::new).collect(Collectors.toList());
    }

}
