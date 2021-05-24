package com.myhome.server.DTO;

import lombok.Data;

@Data
public class RegisterDTO {

    private String name;
    private String email;
    private String password;
    private String password2;
    private String zipcode;
    private String address;
    private String detail_address;
    private String nickname;

    public RegisterDTO(String name, String email, String password, String password2, String zipcode, String address, String detail_address,String nickname) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.zipcode = zipcode;
        this.address = address;
        this.detail_address = detail_address;
        this.nickname = nickname;
    }
}
