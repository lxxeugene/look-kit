package com.example.lookkit.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InquiryAnswerDTO {
    private long answerId;
    private long inquiryId;
    private LocalDateTime answerCreatedAt;
    private String answerContents;
}
