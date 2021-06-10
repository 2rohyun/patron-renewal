package com.thyme.test.Data;

import lombok.Getter;

@Getter
public class Member {

    int numbers;
    String name;
    String phone;

    public Member () {
    }

    public Member(int num, String na, String ph) {
        this.numbers = num;
        this.name = na;
        this.phone = ph;
    }

}
