var pwdFlag = false;
$(document).ready(function () {
    if (stamp !== null && stamp !== "null" && typeof stamp !== "undefined" && stamp !== "") {
        if (stamp === todayDate) {
            debugger;
            showContentBody('resetPassword');
            $("#forgotUserId").val(UserID);
            $("#forgotUserName").val(UserNAME);
        } else {
            alert("Link Expired.");
            $("#forgotUserId").val("");
            $("#forgotUserName").val("");
            window.location = "../MarkMeIn/Login.jsp";
        }
    }

    $("#hitLogin").click(function () {
        var name = $("#userName").val();
        var password = $("#userPassword").val();

        if (name.trim() !== "" && password.trim() !== "") {
            $.ajax({
                url: "../MarkMeIn/MMIController",
                async: false,
                data: {
                    userName: name,
                    userPassword: password,
                    callType: "Login"
                },
                success: function (data) {
                    console.log(data);
                    var responseData = JSON.parse(data);
                    if (responseData.Login === "Success") {
                        localStorage.setItem("loginData", JSON.stringify(responseData));
                        window.location = "../MarkMeIn/Dashboard.jsp";
                    } else {
                        alert("Invalid Username or Password. Please try again.");
                    }
                },
                error: function (error) {
                    alert("Ajax error---" + error);
                }
            });
        } else {
            alert("Please enter username and password.");
        }
    });

    $("#newUserConfirmPassword").on("focusout", function () {
        if ($("#newUserConfirmPassword").val().trim() !== "") {
            if ($("#newUserPassword").val().trim() !== "") {
                if ($("#newUserPassword").val().trim() !== $("#newUserConfirmPassword").val().trim()) {
                    pwdFlag = false;
                    $("#newUserConfirmPassword").val("");
                    alert("Confirm password mismatch.");
                } else {
                    pwdFlag = true;
                }
            } else {
                pwdFlag = false;
                $("#newUserConfirmPassword").val("");
                alert("Please enter password.");
            }
        } else {
            pwdFlag = false;
            alert("Please confirm password first.");
        }
    });
});

function showContentBody(flag) {
    debugger;
    if (flag === "login") {
        window.location = "../MarkMeIn/Login.jsp";
    }
    $(".modal-body").hide();
    $("#" + flag + "Div").removeAttr("hidden");
    $("#" + flag + "Div").attr("style", "display:block;");
    $("input").val("");
}

//function matchQuestion() {
//    if ($("#forgetSecurityAns").val().trim() !== "") {
//        $.ajax({
//            url: "../MarkMeIn/MMIController",
//            async: false,
//            data: {
//                userName: $("#forgetUserName").val(),
//                securityAns: $("#forgetSecurityAns").val(),
//                callType: "matchSecurity"
//            },
//            success: function (data) {
//                if (data === "Success") {
//                    $(".divPassword").removeAttr("hidden");
//                } else {
//                    $("#forgetSecurityAns").val("");
//                    alert("Wrong Answer. Please try again!");
//                }
//            },
//            error: function (error) {
//                alert("Ajax error---" + error);
//            }
//        });
//    } else {
//        alert("Please enter your answer.");
//    }
//}

function updatePassword() {
    if (pwdFlag) {
        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: {
                userId: $("#forgotUserId").val(),
                userName: $("#forgotUserName").val(),
                password: $("#newUserPassword").val(),
                callType: "updatePassword"
            },
            success: function (data) {
                if (data === "Success") {
                    alert("Password updated. Please login to proceed!");
                      window.location = "../MarkMeIn/Login.jsp";
                } else {
                    alert("Invalid URL. Please try again!");
                    window.location = "../MarkMeIn/Login.jsp";
                }
            },
            error: function (error) {
                alert("Ajax error---" + error);
            }
        });
    } else {
        alert("Please update and confirm password.");
    }
}

function insertUser() {
    debugger;
    var loginData = {};
    var dataFlag = true;
    $("div#createUserDiv input").each(function () {
        if ($(this).val().trim() === "") {
            dataFlag = false;
            return false;
        }
    });

    if (dataFlag) {
        loginData['userName'] = $("#createUserName").val();
        loginData['designation'] = $("#createDesignation").val();
        loginData['password'] = $("#createPassword").val();
        loginData['eMail'] = $("#createEmail").val();
        loginData['contact'] = $("#createContact").val();
        loginData['schoolName'] = $("#createSchool").val();
//        loginData['securityQue'] = $("#createSecurityQue").val();
//        loginData['securityAns'] = $("#createSecurityAns").val();

        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: {
                data: JSON.stringify(loginData),
                callType: "insertUser"
            },
            success: function (data) {
                if (data === "User Exists!") {
                    alert("User already exists. Please login with your credentials!");
                } else if (data === "Success") {
                    $("input").val("");
                    alert("User Created. Please login now!");
                }
            },
            error: function (error) {
                alert("Ajax error---" + error);
            }
        });
    } else {
        alert("Please fill all details.");
    }
}

//function getSecurityQue() {
//    var user = $("#forgetUserName").val();
//    if (user.trim() !== "") {
//        $.ajax({
//            url: "../MarkMeIn/MMIController",
//            async: false,
//            data: {
//                userName: user,
//                callType: "getSecurityQue"
//            },
//            success: function (data) {
//                if (data === "") {
//                    alert("Please enter correct username.");
//                } else {
//                    $("#forgetSecurityQue").val(data);
//                }
//            },
//            error: function (error) {
//                alert("Ajax error---" + error);
//            }
//        });
//    } else {
//        alert("Please enter username.");
//    }
//}

function sendPasswordMail() {
    if ($("#forgetUserName").val().trim() !== "") {
        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: {
                userName: $("#forgetUserName").val().trim(),
                callType: "sendPasswordMail"
            },
            success: function (data) {
                if (data === "Success") {
                    alert("Please find link on your registered mail to reset the password.");
                    window.location = "../MarkMeIn/Login.jsp";
                } else {
                    alert("Please enter correct username.");
                }
            },
            error: function (error) {
                alert("Ajax error---" + error);
            }
        });
    } else {
        alert("Please enter username and email.");
    }
}