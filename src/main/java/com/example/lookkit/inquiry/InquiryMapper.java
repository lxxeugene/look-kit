package com.example.lookkit.inquiry;

import com.example.lookkit.common.dto.InquiryImagesDTO;
import com.example.lookkit.common.dto.InquiryUserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InquiryMapper {
    @Select("select inquiry_id, user_id, inquiry_title, inquiry_contents, inquiry_created_at, answer_state " +
            "from inquiries where user_id=#{userId} " +
            "order by inquiry_created_at desc")
    public List<InquiryVO> getList(long userId);

    @Delete("delete from inquiries where inquiry_id=#{inquiryId}")
    public void deleteInquiry(long inquiryId);


    @Insert("INSERT INTO inquiries (user_id, inquiry_title, inquiry_contents) " +
            "VALUES (#{userId}, #{inquiryTitle}, #{inquiryContents})")
    @Options(useGeneratedKeys = true, keyProperty = "inquiryId")
    public void uploadInquiry(InquiryVO vo);

    @Insert("INSERT INTO inquiries_images (inquiry_id, image_path) " +
            "VALUES (#{inquiryId}, #{imagePath})")
    public void uploadInquiryImage(InquiryImageVO vo);

    //문의-사진
    @Select("SELECT INQUIRY_ID, USER_ID, INQUIRY_TITLE, INQUIRY_CONTENTS, INQUIRY_CREATED_AT, ANSWER_STATE " +
            "FROM INQUIRIES WHERE INQUIRY_ID = #{inquiryId}")
    @Results({
            @Result(property = "inquiryId", column = "INQUIRY_ID"),
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "inquiryTitle", column = "INQUIRY_TITLE"),
            @Result(property = "inquiryContents", column = "INQUIRY_CONTENTS"),
            @Result(property = "inquiryCreatedAt", column = "INQUIRY_CREATED_AT"),
            @Result(property = "answerState", column = "ANSWER_STATE"),
            @Result(property = "inquiryImages", column = "INQUIRY_ID",
                    many = @Many(select = "getImages"))
    })
    InquiryImagesDTO getInquiryWithImages(long inquiryId);

    @Select("SELECT INQUIRY_IMAGE_ID, INQUIRY_ID, IMAGE_PATH " +
            "FROM INQUIRIES_IMAGES WHERE INQUIRY_ID = #{inquiryId}")
    List<InquiryImageVO> getImages(long inquiryId);

    @Select("Select ANSWER_ID, INQUIRY_ID, ANSWER_CREATED_AT, ANSWER_CONTENTS " +
            "FROM INQUIRIES_ANSWER WHERE INQUIRY_ID = #{inquiryId}")
    InquiryAnswerVO getAnswer(long inquiryId);


    // 모든 유저의 문의목록 가져오기
    @Select("SELECT i.inquiry_id, u.user_uuid AS userUuid, i.inquiry_title, i.inquiry_contents, " +
            "i.inquiry_created_at, i.answer_state " +
            "FROM inquiries i " +
            "JOIN users u ON i.user_id = u.user_id " +
            "ORDER BY i.inquiry_created_at DESC")
    @Results({
            @Result(property = "inquiryId", column = "inquiry_id"),
            @Result(property = "userUuid", column = "userUuid"),  // userUuid로 매핑
            @Result(property = "inquiryTitle", column = "inquiry_title"),
            @Result(property = "inquiryContents", column = "inquiry_contents"),
            @Result(property = "inquiryCreatedAt", column = "inquiry_created_at"),
            @Result(property = "answerState", column = "answer_state")
    })
    List<InquiryUserDTO> getAllInquiries();


    // 특정 문의 ID로 문의 데이터 가져오기
    @Select("SELECT i.inquiry_id, u.user_uuid AS userUuid, i.inquiry_title, i.inquiry_contents, " +
            "i.inquiry_created_at, i.answer_state " +
            "FROM inquiries i " +
            "JOIN users u ON i.user_id = u.user_id " +
            "WHERE i.inquiry_id = #{inquiryId}")
    @Results({
            @Result(property = "inquiryId", column = "inquiry_id"),
            @Result(property = "userUuid", column = "userUuid"),  // userUuid로 매핑
            @Result(property = "inquiryTitle", column = "inquiry_title"),
            @Result(property = "inquiryContents", column = "inquiry_contents"),
            @Result(property = "inquiryCreatedAt", column = "inquiry_created_at"),
            @Result(property = "answerState", column = "answer_state")
    })
    InquiryUserDTO findByInquiryId(@Param("inquiryId") int inquiryId);



        // 문의 답변하기
        @Insert("INSERT INTO inquiries_answer (inquiry_id, answer_contents, answer_created_at) " +
                "VALUES (#{inquiryId}, #{answerContents}, NOW())")
        @Options(useGeneratedKeys = true, keyProperty = "answerId")
        int insertAnswer(InquiryAnswerVO answer);

        // 문의 상태를 답변 완료로 업데이트하기
        @Update("UPDATE inquiries SET answer_state = 'Y' WHERE inquiry_id = #{inquiryId}")
        int updateInquiryAnswerState(@Param("inquiryId") long inquiryId);


    // 특정 문의 ID로 답변 데이터 조회
    @Select("SELECT answer_id, inquiry_id, answer_contents, answer_created_at " +
            "FROM inquiries_answer " +
            "WHERE inquiry_id = #{inquiryId}")
    @Results({
            @Result(property = "answerId", column = "answer_id"),
            @Result(property = "inquiryId", column = "inquiry_id"),
            @Result(property = "answerContents", column = "answer_contents"),
            @Result(property = "answerCreatedAt", column = "answer_created_at")
    })
    InquiryAnswerVO findAnswerByInquiryId(@Param("inquiryId") Integer inquiryId);


}
