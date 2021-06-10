package com.thyme.test.Data;

import lombok.Getter;

@Getter
public class Members {

    int numbers;
    String name;
    String phone;

    public Members () {
    }

    public Members(int num, String na, String ph) {
        this.numbers = num;
        this.name = na;
        this.phone = ph;
    }

}
