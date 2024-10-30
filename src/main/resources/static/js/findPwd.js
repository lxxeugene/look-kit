//전역변수 선언
let userId;
$(document).ready(() => {
    let verificationCode; //인증코드
    let userEmail;
    let name;


    $("#goToLoginBtn").click(() => {
        location.href = "/auth/login";
    });


    /** 이름과 이메일 일치하는 유저아이디찾기*/
    function findUser(name ,userEmail) {

        fetch("/auth/findUser", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ userName: name,email:userEmail })
        })
            .then(response => response.text())
            .then(data => {

            // 유저가 존재하는지 확인 후 유저아이디 저장
            userId = data;
            console.log("저장된아이디: "+userId)
        })
            .catch(error => {
            console.error("Error:", error);
        });
    }





    // 이메일인증 버튼 클릭 이벤트
    $("#authenticationBtn").click(async() => {
        console.log("sss")
        name = $("input[name='name']").val();
        userEmail = $("input[name='email']").val();
        console.log("입력한 이름:", name);
        console.log("입력한 이메일:", userEmail);

        // 유효성 검사 (이메일 형식 확인)
        let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (name.trim() === "") {
            alert("이름을 입력해 주세요.");
            return;
        }
        if (!emailPattern.test(userEmail)) {
            alert("올바른 이메일 형식을 입력해 주세요.");
            return;
        }

        // 이메일 인증 요청
        await sendEmailVerification(name, userEmail);
        $("#authenticationBtn").prop("disabled", true).text("인증코드 전송중...");
        // 실제 유저 찾기
        await findUser(name,userEmail);
    });
});

/** 이메일 인증 요청 함수 */
function sendEmailVerification(name, userEmail) {

    fetch("/mailsender", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: userEmail })
    })
        .then(response => response.text())
        .then(data => {
        alert(data);
        verificationCode= data;
        alert("입력하신 이메일로 인증번호를 발송했습니다.");
        $(".formContainer").css("display", "none");
        $(".formContainer2").css("display", "block");
    })
        .catch(error => {
        console.error("Error:", error);
        alert("이메일 인증에 실패했습니다. 다시 시도해 주세요.");
    });

    $("#authenticationOKBtn").click(() => {
        let veriCode = $("input[name='veriCode']").val();
        if(verificationCode == veriCode){
            confirm("인증에 성공하였습니다.");
            $(".formContainer2").css("display", "none");
            $(".formContainer3").css("display", "flex");
        }else{
            $(".verificationNotice").text("* 인증번호가 일치하지 않습니다.").css("color","red");
            //일치하지않을경우 버튼 숨기고 재인증버튼을 나타나도록 설정
        }
    });

    // 비밀번호 수정 버튼 클릭이벤트
    $("#ModifyBtn").click( async () => {
        //      alert("비밀번호 수정요청")
        let newPwd = $("#newPwd").val();
        console.log("새비번:"+newPwd);
        let newPwd2 = $("#newPwd2").val();
        console.log("새비번2:"+newPwd2);
        if(newPwd == newPwd2){
            await  updatePassword(userId,newPwd);
        }
    });

    /** 비밀번호 수정 요청 함수*/
    function updatePassword(userId ,newPwd) {
        fetch("/auth/updatePwd", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ userUuid: userId,password:newPwd })
        })
            .then(response => response.text())
            .then(data => {
            console.log("비밀번호 변경요청으로 응답받음 ")
            //폼체인지
            //            alert(data);
            $(".formContainer3").css("display", "none");
            $(".formContainer4").css("display", "block");
        })
            .catch(error => {
            console.error("Error:", error);
        });
    }



};
