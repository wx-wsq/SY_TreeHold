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
        String s= "1,2,3,4,";
        System.out.println(Arrays.toString(s.split(",")));
    }

}
