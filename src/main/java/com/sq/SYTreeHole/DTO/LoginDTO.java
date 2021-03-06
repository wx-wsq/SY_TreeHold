package com.sq.SYTreeHole.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginDTO {
    private String code;
    private String msg;
    private Long expire;
    private String token;
    private String id;
}
