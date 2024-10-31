$(document).ready(function() {
    var selectedFiles = []; // 선택한 파일들
    // 단일 이미지 업로드
    $('.image-placeholder').on('click', function() {
        var index = $(this).data('index');
        var inputElement = $('<input type="file" accept="image/*" style="display:none;" />');
        var placeholders = $('.image-placeholder');

        // 기존에 생성된 input 제거
        $(this).next('input[type="file"]').remove();

        // 새로운 input 추가 후 파일 선택
        $(this).after(inputElement);
        inputElement.click();

        // 파일 선택 후 처리
        inputElement.on('change', function(event) {
            var file = event.target.files[0];

            if (file) {
                // 선택한 파일을 배열에 저장 (단일 파일)
                selectedFiles[index - 1] = file;
                updateImagePreview(placeholders);
            }
        });
    });

    // 다중 이미지 업로드 버튼 클릭
    $('.upload-button').on('click', function() {
        selectedFiles = [];

        // 기존 input을 제거하여 이벤트 중복 바인딩을 방지
        $(this).next('input[type="file"]').remove();

        // 새로운 input 생성
        var newInput = $('<input type="file" accept="image/*" style="display:none;" multiple/>');
        $(this).after(newInput);

        // 새로운 input에 파일 선택 이벤트 바인딩
        newInput.on('change', function(event) {
            var files = event.target.files; // 선택된 파일 가져오기
            var placeholders = $('.image-placeholder');

            // 파일 배열에 추가
            $.each(files, function(i, file) {
                if (selectedFiles.length < 3) { // 최대 3개의 파일만 허용
                    selectedFiles.push(file);
                }
            });
            // 미리보기 업데이트
            updateImagePreview(placeholders);
        });
        // 파일 선택창 열기
        newInput.click();
    });

    // 이미지 미리보기 업데이트 함수
    function updateImagePreview(placeholders) {
        // 모든 placeholder를 비우고 다시 그리기
        $.each(placeholders, function(index, placeholder) {
            $(placeholder).empty().text('+'); // 기존 내용 지우기
        });

        // 배열에 있는 파일들로 미리보기 업데이트
        $.each(selectedFiles, function(i, file) {
            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    $(placeholders[i]).empty().append($('<img>').attr('src', e.target.result));
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // 이미지 전송 이벤트
    $('.btn-submit').on('click', function() {
        var title = $('.input-title').val();
        var content = $('.input-content').val();
        var isValid = true;

        // 에러 메시지 초기화
        $('.error-message').hide();

        // 유효성 검사
        if (!title) {
            $('#title-error').show(); // 제목이 없을 때 에러 메시지 표시
            isValid = false;
        }

        if (!content) {
            $('#content-error').show(); // 내용이 없을 때 에러 메시지 표시
            isValid = false;
        }

        if (!isValid) {
            return; // 유효성 검사를 통과하지 못하면 Ajax 요청을 중지
        }

        // 유효성 검사를 통과하면 Ajax 요청을 보냄
        var formData = new FormData();
        formData.append('title', title);
        formData.append('content', content);

        // 선택된 이미지 파일들을 FormData에 추가
        $.each(selectedFiles, function(index, file) {
            if (file) {
                formData.append('files', file); // 파일 배열로 추가
            }
        });
        // 서버로 이미지 전송
        $.ajax({
            url: '/mypage/inquiry/create',
            type: 'POST',
            data: formData,                // FormData 객체를 서버로 전송
            contentType: false,            // 브라우저가 자동으로 Content-Type을 설정하도록 함
            processData: false,            // jQuery가 데이터를 변환하지 않도록 설정
            success: function(response) {
                alert(response);
                window.location.href = "/mypage/inquiry/list";
            },
            error: function(e) {
                console.log(e);
            }
        });
    });
});