package com.nneomablessyn.packagefinder.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
@Setter
public class Response<T> implements Serializable {
    private boolean status;
    private String message;
    private T data;

    public Response(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}