$(document).ready(function () {
    let idDuplicateChecked = false;
    //      -- [주소지 찾기] --
    $("#addressSearch").click(function () {
        new daum.Postcode({
            oncomplete: function (data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ""; // 주소 변수
                var extraAddr = ""; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === "R") {
                    // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else {
                    // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if (data.userSelectedType === "R") {
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if (data.buildingName !== "" && data.apartment === "Y") {
                        extraAddr +=
                        extraAddr !== ""
                        ? ", " + data.buildingName
                        : data.buildingName;
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if (extraAddr !== "") {
                        extraAddr = " (" + extraAddr + ")";
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                } else {
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.

                document.getElementById("user_address").value = addr;

                //주소 검색이 완료된 후 변하는 css 목록
                $(".field_address input").css("display", "block");
                $("#addressNo").text("재검색");

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("user_detail_address").focus();
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
                // 예제를 참고하여 다양한 활용법을 확인해 보세요.
                // http://postcode.map.daum.net/guide  api주소
            },
        }).open();
    });


    // --  [유효성 검사]  --

    /**아이디 중복체크 클릭이벤트 **/
    $(".duplicateCheckButton").click(() => {

        // 정규식
        // id
        let regId = /^[a-zA-Z0-9]{6,12}$/;
        // 이름
        let regName = /^[가-힣a-zA-Z]{2,15}$/;

        let inputId= $("#userUuid").val();
        console.log("입력한 아이디",inputId);
        if( !regId.test(inputId) ){
            $('#idCheckLabel').text('* 6~12자 영문 대소문자, 숫자만 입력하세요.').css({'display':'inline','color': '#FF294F'});
        }
        else{
            $('#idCheckLabel').text('');
            fetchDuplicateCheckId(inputId);
        }
    });

    /**비밀번호 검증 */
    $("#password").keyup( function() {
        let regPw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
        if( !regPw.test($(this).val())){
            $('#pwCheckLabel').text('* 8-16자 이내 영문자,숫자,특수문자를 모두 포함해야합니다.').css({'display':'inline','color': '#FF294F'});
        }else{
            $('#pwCheckLabel').css({'display':'none'});
        }
    })

    /**이메일 검증 */
    $("#email").keyup( function() {
        console.log($(this).val());
        // 이메일
        let regMail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
        if( !regMail.test($(this).val())){
            $('#emailCheckLabel').text('* 올바른 이메일 주소를 입력해주세요.').css({'display':'inline','color': '#FF294F'});
        }else{
            $('#emailCheckLabel').css({'display':'none'});
        }
    })


    /**아이디 중복요청 확인 요청 */
    const fetchDuplicateCheckId = (inputId)=>{
        fetch(`/auth/checkDuplicateId?id=${inputId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
            if (!response.ok) {
                throw new Error('응답에러 ' + response.statusText);
            }
            return response.json();
        })
            .then(result => {
            console.log('결과 : ', result);
            if (result) {
                $('#idCheckLabel').text('✓ 사용 가능한 아이디입니다.').css({'color': '#1FCE65','display':'inline'});
                idDuplicateChecked=true;
            } else {
                $('#idCheckLabel').text('* 이미 사용 중인 아이디입니다.').css({'display':'inline','color': '#FF294F'});
            }
        })
            .catch(error => {
            console.error('아이디 중복 체크 중 에러 발생 : ', error);
        });
    }


    // --  [submit 이벤트]  --

    /**폼제출 버튼 이벤트*/
    $(".signupButton").click((e) => {
        // 폼 기본 이벤트 막기
        e.preventDefault();
        // 기본주소와 상세주소 합치기
        let addr1 =$("#user_address").val();
        console.log('addr1',addr1);
        let addr2 =$("#user_detail_address").val();
        console.log('addr2',addr2);
        $("#user_total_address").val(addr1 + " " + addr2);
        // 폼 제출
        $("form").submit();
    });

    $(".cancelButton").click(() => {
        location.href = "/auth/login";
    });

    /**폼제출 이벤트 */
    $("#signUpForm").submit(function(e){
        e.preventDefault();
        let userId = $("#userUuid").val().trim();
        let password = $("#password").val().trim();
        let phone = $("#phone").val().trim();
        let birthDate = $("#birthDate").val().trim();
        let email = $("#email").val().trim();
        let addr1 = $("#user_address").val().trim();

        if(!userId || !password || !phone || !birthDate || !email || !addr1){
            alert("입력하지 않은 항목이 있습니다. 확인 후 입력해주세요.");
        }else if(!idDuplicateChecked){
            alert("아이디 중복확인을 해주세요.")
        }else{
            this.submit();
        }
    })

    /**성별 선택 클릭이벤트 */
    $(".genderBtn").click((e) => {
        let target = e.currentTarget;
        let gender = $(target).text();
        if(gender === 'Man') {
            let isChecked = $('#genderInputMan').prop('checked');
            $('#genderInputMan').prop('checked', !isChecked);
        } else{
            let isChecked = $('#genderInputWoman').prop('checked');
            $('#genderInputWoman').prop('checked', !isChecked);
        }
        $(".genderBtn").removeClass("checked-option"); // 일단 모두 제거 후 현재 선택요소에 재설정
        $(target).toggleClass("checked-option");
    });


});

