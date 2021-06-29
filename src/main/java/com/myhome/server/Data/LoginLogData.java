package com.myhome.server.Data;

import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class LoginLogData {

    private String ip;
    private String countryCode;
    private String ipv;
}
