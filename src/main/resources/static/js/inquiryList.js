$(document).ready(function() {
    // 삭제 버튼 클릭 이벤트 처리
    $('.delete-btn').on('click', function() {
        var inquiryId = $(this).attr('data-id'); // 버튼의 data-id 값 가져오기

        if (confirm("정말로 삭제하시겠습니까?")) {
            $.ajax({
                url: '/mypage/inquiry/delete/' + inquiryId,
                type: 'DELETE',
                success: function(result) {
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("삭제에 실패했습니다.");
                    console.error(error);
                }
            });
        }
        // 취소를 누른 경우 아무 동작도 하지 않음
    });

});