package com.sq.SYTreeHole.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String code;
    private String msg;
    private Long expire;
    private String token;
    private String id;
}
