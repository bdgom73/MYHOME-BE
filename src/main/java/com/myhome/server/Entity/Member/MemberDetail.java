package com.myhome.server.Entity.Member;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("address")
public class MemberDetail extends Member{
    private String zipcode;
    private String address;
    private String detail_address;
}
