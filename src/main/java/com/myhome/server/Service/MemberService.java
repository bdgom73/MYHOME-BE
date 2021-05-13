package com.myhome.server.Service;

import com.myhome.server.Crypt.Bcrypt;
import com.myhome.server.DTO.AuthenticationDataDTO;
import com.myhome.server.DTO.RegisterDTO;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberService {

    private MemberDetailRepository memberDetailRepository;
    private Bcrypt bcrypt;
    BCryptPasswordEncoder pass = new BCryptPasswordEncoder();
    public MemberService(MemberDetailRepository memberDetailRepository, Bcrypt bcrypt) {
        this.memberDetailRepository = memberDetailRepository;
        this.bcrypt = bcrypt;
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

        MemberDetail member = new MemberDetail();
        member.setName(registerDTO.getName());
        member.setEmail(registerDTO.getEmail());
        member.setPassword(bcrypt.passwordEncoder(registerDTO.getPassword()));
        member.setCreated(LocalDateTime.now());
        member.setUpdated(LocalDateTime.now());
        member.setAddress(registerDTO.getAddress());
        member.setZipcode(registerDTO.getZipcode());
        member.setDetail_address(registerDTO.getDetail_address());

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
        AuthenticationDataDTO authData = new AuthenticationDataDTO();
        authData.setName(member.getName());
        authData.setEmail(member.getEmail());
        authData.setRank(member.getRank());
        authData.setAvatar_url(member.getAvatar_url());

        return authData;
    }



}
