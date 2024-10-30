package com.example.lookkit.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InquiryAnswerVO {
    private long answerId;
    private long inquiryId;
    private LocalDateTime answerCreatedAt;
    private String answerContents;
}
