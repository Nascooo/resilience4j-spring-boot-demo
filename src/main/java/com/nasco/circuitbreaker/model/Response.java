package com.nasco.circuitbreaker.model;

import lombok.Data;

@Data
public class Response {
    private String responseMessage;
    private Integer code;
}
