package com.sq.SYTreeHole.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private String code;
    private String msg;
    private T data;

}

