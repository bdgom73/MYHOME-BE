package com.myhome.server.Controller.Member;

import com.myhome.server.Crypt.Bcrypt;
import com.myhome.server.DTO.AuthenticationDataDTO;
import com.myhome.server.DTO.RegisterDTO;
import com.myhome.server.Data.FileResult;
import com.myhome.server.Data.LoginLogData;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Comment.CommentRepository;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.AWS_S3Upload;
import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MailService;
import com.myhome.server.Service.MemberService;
import com.myhome.server.Service.MyModel.MyModel;
import com.myhome.server.Service.MyModel.Templates;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.AuthenticationException;
import javax.naming.SizeLimitExceededException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/myApi/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberDetailRepository memberDetailRepository;
    private final CommentRepository commentRepository;
    private final JavaMailSender javaMailSender;
    private final MailService mailService;
    private final AWS_S3Upload aws_s3Upload;
    private Templates templates = new Templates();

    public MemberController(MemberService memberService, MemberDetailRepository memberDetailRepository, CommentRepository commentRepository, JavaMailSender javaMailSender, MailService mailService, AWS_S3Upload aws_s3Upload) {
        this.memberService = memberService;
        this.memberDetailRepository = memberDetailRepository;

        this.commentRepository = commentRepository;
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
        this.aws_s3Upload = aws_s3Upload;
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
        RegisterDTO registerDTO = new RegisterDTO(name, email, password, password2, zipcode, address, detail_address, nickname);
        try{
            memberService.SignUp(registerDTO);
        } catch (Exception e){
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "로그인에 실패했습니다.");
        }
    }

    @PostMapping("/login")
    public String login(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "password") String password,
        @RequestParam(name = "ip") String ip,
        @RequestParam(name = "countryCode") String countryCode,
        @RequestParam(name = "ipv") String ipv,
        HttpServletResponse httpServletResponse
    ) throws IOException {
        try{
            LoginLogData loginLogData = new LoginLogData();
            loginLogData.setCountryCode(countryCode);
            loginLogData.setIp(ip);
            loginLogData.setIpv(ipv);
            String login = memberService.Login(email, password,loginLogData);
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


    @GetMapping("/{nickname}")
    public AuthenticationDataDTO userFind(
            @PathVariable("nickname") String nickname,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<MemberDetail> findEmail = memberDetailRepository.findByNickname(nickname);
        if(findEmail.isEmpty()) httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,"현재 이메일을 가진 유저가 없습니다.");
        MemberDetail member = findEmail.get();

        return new AuthenticationDataDTO(member);
    }

    @PutMapping("/update")
    public void updateMember(
            @RequestHeader("Authorization") String UID,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "zipcode") String zipcode,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "detail_address") String detail_address,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        if (!member.getNickname().equals(nickname)) {
            memberService.DuplicateNickName(nickname);
            member.setNickname(nickname);
        }
        member.setZipcode(zipcode);
        member.setAddress(address);
        member.setDetail_address(detail_address);
        memberDetailRepository.save(member);
    }

    @GetMapping("/find/password")
    public void findPassword(
            @PathParam("email") String email,
            HttpServletResponse httpServletResponse
    ) throws IOException, MessagingException {
        Optional<MemberDetail> findEmail = memberDetailRepository.findByEmail(email);
        if(findEmail.isEmpty()) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"존재하지않는 이메일입니다.");
            return;
        }
        MemberDetail member = findEmail.get();
        // 메일 기본설정
        String to = member.getEmail();
        String from = "MyDomus@mydomus.home";
        String subject = templates.MEMBER_REGISTRATION_AUTHENTICATION_TITLE;

        // 메일 내용 세팅
        StringBuilder body = new StringBuilder();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        MyModel myModel = new MyModel();
        FileUpload fileUpload = new FileUpload();
        String rs = fileUpload.RandomString(10);
        myModel.setModel("auth_num",rs);
        String am = mailService.addTemplates(templates.MEMBER_REGISTRATION_AUTHENTICATION,myModel);
        body.append(am);

        // 메일 보내기설정
        mimeMessageHelper.setFrom(from,"MyDomus");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);
        javaMailSender.send(message);

        // 패스워드 변경설정
        Bcrypt bcrypt = new Bcrypt();
        String changePassword = bcrypt.passwordEncoder(rs);
        member.setPassword(changePassword);
        memberDetailRepository.save(member);
    }

    @PostMapping("/change/password")
    public void changePassword(
            @RequestHeader("Authorization") String UID,
            @RequestParam("current_password") String current_password,
            @RequestParam("password") String password,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        Bcrypt bcrypt = new Bcrypt();
        if(bcrypt.matchesPassword(current_password,member.getPassword())){
            String new_password = bcrypt.passwordEncoder(password);
            member.setPassword(new_password);
            memberDetailRepository.save(member);
        }
    }

    @PostMapping("/equality/password/check")
    public Map<String, Object> passwordEqualityCheck (
            @RequestHeader("Authorization") String UID,
            @RequestParam("password") String password,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        Bcrypt bcrypt = new Bcrypt();
        Map<String , Object> result = new HashMap<>();
        if(bcrypt.matchesPassword(password,member.getPassword())){
           result.put("message","비밀번호가 동일합니다");
           result.put("result",true);
           return result;
        }
        result.put("message","비밀번호가 다릅니다.");
        result.put("result",false);
        return result;
    }

    @PostMapping("/change/avatar")
    public void changeAvatar(
            @RequestHeader("Authorization") String UID,
            @RequestParam(name = "avatar") MultipartFile avatar,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        FileResult result = aws_s3Upload.upload(avatar, "static/avatar");

        member.setAvatar_url(result.getUrl());

        memberDetailRepository.save(member);
    }

    @PostMapping("/change/introduce")
    public void changeIntroduce(
            @RequestHeader("Authorization") String UID,
            @RequestParam(name = "introduce") String introduce,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        member.setSelf_introduction(introduce);
        memberDetailRepository.save(member);
    }

}
