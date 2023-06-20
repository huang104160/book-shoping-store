package com.xuxu.bookstore;


import org.junit.Test;
import org.springframework.util.DigestUtils;

public class CommonTest {
    @Test
    public void getAdminSecret(){
        String password = "123456";
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("md5 = " + md5);
    }
}
