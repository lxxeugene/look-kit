package com.example.lookkit.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressVO {
    private long userId;
    private String address;
    private String phone;
    private String addressee;
}
