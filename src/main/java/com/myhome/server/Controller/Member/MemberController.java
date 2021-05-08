package com.myhome.server.Controller.Member;

import com.myhome.server.DTO.RegisterDTO;
import com.myhome.server.Service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
            HttpServletResponse httpServletResponse
    ) throws IOException {
        System.out.println("password + \" \"+ password2 = " + password + " "+ password2);
        RegisterDTO registerDTO = new RegisterDTO(name, email, password, password2, zipcode, address, detail_address);
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


}
