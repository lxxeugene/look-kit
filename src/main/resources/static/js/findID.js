$(document).ready(() => {
    $("#findIdBtn").click(() => {
        let name = $('input[name="name"]').val();
        let email = $('input[name="email"]').val();
        console.log(name);
        console.log(email);
        if(!name || !email){
            alert("이름과 이메일을 입력해주세요.");
            return;
        }

        //            location.href = "/auth/findIdOK";
        $("#findIdForm").submit();
        //              $(".notice-label").css({"color","#FF294F"},{"display","inline"});
    });
});
