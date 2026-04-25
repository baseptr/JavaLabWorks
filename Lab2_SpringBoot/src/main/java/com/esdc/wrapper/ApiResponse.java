package com.esdc.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
    private T data;
    private int count;
    private LocalDateTime timestamp;
}
