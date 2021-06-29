package com.myhome.server.Service;

import com.myhome.server.Crypt.Bcrypt;
import com.myhome.server.DTO.AuthenticationDataDTO;
import com.myhome.server.DTO.RegisterDTO;
import com.myhome.server.Data.LoginLogData;
import com.myhome.server.Entity.Log.LoginLog;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Entity.Word.FilterText;
import com.myhome.server.Repository.Log.LoginLogRepository;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Repository.Word.FilterTextRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.SizeLimitExceededException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberService {

    private MemberDetailRepository memberDetailRepository;
    private Bcrypt bcrypt;
    private BCryptPasswordEncoder pass = new BCryptPasswordEncoder();
    private FilterTextRepository filterTextRepository;
    private final LoginLogRepository loginLogRepository;
    public MemberService(MemberDetailRepository memberDetailRepository, Bcrypt bcrypt, FilterTextRepository filterTextRepository, LoginLogRepository loginLogRepository) {
        this.memberDetailRepository = memberDetailRepository;
        this.bcrypt = bcrypt;
        this.filterTextRepository = filterTextRepository;
        this.loginLogRepository = loginLogRepository;
    }

    public String Login(String email, String password, LoginLogData loginLogData) throws LoginException {
        /* TODO
        *   존재하는 아이디인지 확인 후
        *  비밀번호 대조 후 로그인 리턴 */
        Optional<MemberDetail> findMember = memberDetailRepository.findByEmail(email);
        if(findMember.isEmpty()){
            throw new LoginException("존재하지 않는 계정입니다.");
        }
        MemberDetail member = findMember.get();;
        String pw = member.getPassword();
        if(!bcrypt.matchesPassword(password,pw)){
            throw new LoginException("비밀번호가 틀립니다.");
        }


        LoginLog log = new LoginLog();
        log.setMember(member);
        log.setLoginDate(LocalDateTime.now());
        log.setCountryCode(loginLogData.getCountryCode());
        log.setIp(loginLogData.getIp());
        log.setIpv(loginLogData.getIpv());

        loginLogRepository.save(log);
        return member.getSESSION_UID();

    }
    public String Login(String email, String password) throws LoginException {
        /* TODO
         *   존재하는 아이디인지 확인 후
         *  비밀번호 대조 후 로그인 리턴 */
        Optional<MemberDetail> findMember = memberDetailRepository.findByEmail(email);
        if(findMember.isEmpty()){
            throw new LoginException("존재하지 않는 계정입니다.");
        }
        MemberDetail member = findMember.get();;
        String pw = member.getPassword();
        if(!bcrypt.matchesPassword(password,pw)){
            throw new LoginException("비밀번호가 틀립니다.");
        }


        return member.getSESSION_UID();
    }
    public void SignUp(RegisterDTO registerDTO) throws Exception {
        /* TODO
        *   회원가입정보 중 아이디 중복검사.
        *  및 각 데이터 조사 후 DB에 저장 */
        Optional<MemberDetail> findMember = memberDetailRepository.findByEmail(registerDTO.getEmail());
        if(findMember.isPresent()){
            throw new SQLDataException("이미 존재하는 아이디입니다.");
        }

        if(!registerDTO.getPassword().equals(registerDTO.getPassword2())){
            throw new Exception("비밀번호가 서로 다릅니다.");
        }
        String pwPattern = "/^[a-zA-Z0-9\\d~!@#$%^&*]{10,}$/";
        Matcher matcher = Pattern.compile(pwPattern).matcher(registerDTO.getPassword());
        if(!matcher.matches()){
            throw new Exception("비밀번호는 영문,숫자, 특수문자(필1)로 이루어진 10자 이상으로 설정가능합니다.");
        }

        DuplicateNickName(registerDTO.getNickname());

        MemberDetail member = new MemberDetail();
        member.setName(registerDTO.getName());
        member.setEmail(registerDTO.getEmail());
        member.setPassword(bcrypt.passwordEncoder(registerDTO.getPassword()));
        member.setCreated(LocalDateTime.now());
        member.setUpdated(LocalDateTime.now());
        member.setAddress(registerDTO.getAddress());
        member.setZipcode(registerDTO.getZipcode());
        member.setDetail_address(registerDTO.getDetail_address());
        member.setNickname(registerDTO.getNickname());
        MemberDetail savedMember = memberDetailRepository.save(member);
        String sessionUid = bcrypt.createSessionUid(savedMember.getId());
        savedMember.setSESSION_UID(sessionUid);
        memberDetailRepository.save(savedMember);
    }

    public AuthenticationDataDTO LoginAuthentication(String sessionUID) throws AuthenticationException {
        /* TODO
        *   로그인 정보 확인
        *  로그인 정보 리턴
        *  null or 정보 */
        Optional<MemberDetail> findMember = memberDetailRepository.findBySessionUID(sessionUID);
        if(findMember.isEmpty()){
            throw new AuthenticationException("올바르지 않은 형식의 토큰입니다.");
        }
        MemberDetail member = findMember.get();

        return new AuthenticationDataDTO(member);
    }

    public MemberDetail LoginCheck(String UID, HttpServletResponse httpServletResponse) throws Exception {
        Optional<MemberDetail> findMember = memberDetailRepository.findBySessionUID(UID);
        if(findMember.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"존재하지 않는 회원입니다.");
            throw new Exception("존재하지 않는 회원입니다.");
        }
        MemberDetail member = findMember.get();
        return member;
    }

    public void DuplicateNickName(String nickname) throws SizeLimitExceededException, ValidationException {
        Optional<MemberDetail> findMember = memberDetailRepository.findByNickname(nickname);
        if(findMember.isPresent()){
            throw new DuplicateRequestException("이미 존재하는 닉네임입니다");
        }
        if(nickname.length() < 2 && nickname.length() >= 12){
            throw new SizeLimitExceededException("닉네임은 2글자이상 12글자이하로 사용가능합니다.");
        }
        if(FilterText(nickname).size() > 0){
            throw new ValidationException("사용 불가능한 단어가 포함되어있습니다.");
        }

    }

    public List<String> FilterText(String text) {
        String notAvailableText = "";
        Optional<FilterText> filter = filterTextRepository.findByKinds("filter");
        if(filter.isEmpty()) notAvailableText =  "fuck|shit|개새끼|씨발|시발|운영자|ADMIN|병신|개시키";
        else{
            String getFilterText = filter.get().getText();
            notAvailableText =  getFilterText.replaceAll(",", "|");
        }
        Pattern p = Pattern.compile(notAvailableText, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        List<String> words = new ArrayList<>();
        while (m.find()) {
            System.out.println("words = " + m.group());
            words.add(m.group());
        }
        return words;
    }
}
