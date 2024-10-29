$(document).ready(function () {
    $("#findIdLink").click(function () {
        location.href = "/auth/findID";
    });
    $("#findPasswordLink").click(function () {
        location.href = "/auth/findPwd";
    });
    $("#signupLink").click(function () {
        location.href = "/auth/signUp";
    });

    $("#showPwdButton").click(function(){
        $(this).css('display','none');
        $("#NoShowPwdButton").css("display","block");
        $("#pwdInputBox").attr("type", "text");
    })

    $("#NoShowPwdButton").click(function(){
        $(this).css('display','none');
        $("#showPwdButton").css('display','block');
        $("#pwdInputBox").attr("type", "password");
    })

});
