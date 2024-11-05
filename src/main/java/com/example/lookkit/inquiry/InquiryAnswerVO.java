package com.example.lookkit.inquiry;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InquiryAnswerVO {
    private long answerId;
    private long inquiryId;
    private LocalDateTime answerCreatedAt;
    private String answerContents;
}
