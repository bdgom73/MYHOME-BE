package com.myhome.server.Entity.Member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("address")
@Getter @Setter
public class MemberDetail extends Member{
    private String zipcode;
    private String address;
    private String detail_address;
}
