package com.myhome.server.DTO;

import com.myhome.server.Entity.Log.LoginLog;
import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class LoginLogDTO {

    private Long id;

    private LocalDateTime loginDate;
    private String ip;
    private String countryCode;
    private String ipv;

    public LoginLogDTO(LoginLog log) {
        this.id = log.getId();
        this.loginDate = log.getLoginDate();
        this.ip = log.getIp();
        this.countryCode = log.getCountryCode();
        this.ipv = log.getIpv();
    }
}
