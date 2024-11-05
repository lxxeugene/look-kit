package com.example.lookkit.common.dto;

import lombok.*;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InquiryUserDTO {
    private long inquiryId;
    private String userUuid;
    private String inquiryTitle;
    private String inquiryContents;
    private LocalDateTime inquiryCreatedAt;
    private String answerState;
}