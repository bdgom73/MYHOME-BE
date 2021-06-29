package com.myhome.server.Service.MyModel;

import lombok.Getter;

public class Templates {

    public String BASIC_URL = "http://localhost:8080";
    public String MEMBER_REGISTRATION_AUTHENTICATION
            = BASIC_URL+"/registerAuthMail.html";

    public String MEMBER_REGISTRATION_AUTHENTICATION_TITLE
            = "[MyDomus] 본인인증메일입니다";

}
