package com.study.springboot.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SimpleBbsDto {
    private int id;
    private String writer;
    private String title;
    private String content;
}
