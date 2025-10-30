package com.example.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestDTO {
    private String userName;
    private Long courseId;
    private String commentary;
    private String phone;
    private boolean handled;
}