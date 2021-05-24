package com.myhome.server.Controller.Member;

import com.myhome.server.DTO.RegisterDTO;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.MemberService;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.naming.SizeLimitExceededException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberDetailRepository memberDetailRepository;

    public MemberController(MemberService memberService, MemberDetailRepository memberDetailRepository) {
        this.memberService = memberService;
        this.memberDetailRepository = memberDetailRepository;
    }

    @PostMapping("/register")
    public void register(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "password2") String password2,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "zipcode") String zipcode,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "detail_address") String detail_address,
            @RequestParam(name = "nickname") String nickname,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        System.out.println("password + \" \"+ password2 = " + password + " "+ password2);
        RegisterDTO registerDTO = new RegisterDTO(name, email, password, password2, zipcode, address, detail_address, nickname);
        try{
            memberService.SignUp(registerDTO);
        } catch (Exception e){
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "password") String password,
        HttpServletResponse httpServletResponse
    ) throws IOException {
        try{
            String login = memberService.Login(email, password);
            return login;
        } catch (LoginException e){
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        return null;
    }

    @GetMapping("/authorization")
    public Object authorization(
            @RequestHeader("Authorization") String UID,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        try{
            return memberService.LoginAuthentication(UID);
        }catch (AuthenticationException ae){
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"데이터 유효성 검사에 실패했습니다.");
        }
        return null;
    }

    @GetMapping("/duplicate/email={email}")
    public void duplicateEmail(
        @PathVariable("email") String email,
        HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<MemberDetail> findEmail = memberDetailRepository.findByEmail(email);
        if(findEmail.isPresent()) httpServletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,"이미 존재하는 이메일입니다.");
    }
    @GetMapping("/duplicate/nickname={nickname}")
    public void duplicateNickname(
            @PathVariable("nickname") String nickname,
            HttpServletResponse httpServletResponse
    ) throws IOException {
       try{
           memberService.DuplicateNickName(nickname);
       }catch (DuplicateRequestException | ValidationException | SizeLimitExceededException d){
           httpServletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,d.getMessage());
       }
    }

}
