package com.imooc.admin.controller;


import org.springframework.security.crypto.bcrypt.BCrypt;

public class PWDTest {
    public static void main(String[] args) {
        String pwd = BCrypt.hashpw("admin", BCrypt.gensalt());//加盐
        System.out.println(pwd);
    }
}
