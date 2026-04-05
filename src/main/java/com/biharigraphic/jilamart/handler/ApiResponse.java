package com.biharigraphic.jilamart.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private boolean error;
    private T data;  // ✅ payload can be service details, list, or null
}
