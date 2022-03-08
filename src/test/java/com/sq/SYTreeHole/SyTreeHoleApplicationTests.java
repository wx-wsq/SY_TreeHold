package com.sq.SYTreeHole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

//@SpringBootTest
class SyTreeHoleApplicationTests {

    @Test
    void contextLoads() {
        String str="123";
        MessageDigest messageDigest;
        String encodeStr="";
        try {
            messageDigest= MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            Base64.Encoder encoder = Base64.getEncoder();
            System.out.println(encoder.encodeToString(digest));
            System.out.println(encoder.encodeToString(digest).equals(encoder.encodeToString(messageDigest.digest("123".getBytes(StandardCharsets.UTF_8)))));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(encodeStr);
    }

}
