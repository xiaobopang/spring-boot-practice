package com.example.domain;

import lombok.Data;

@Data
public class TestResp<T>{
    String code;
    String msg;
    T data;
}

