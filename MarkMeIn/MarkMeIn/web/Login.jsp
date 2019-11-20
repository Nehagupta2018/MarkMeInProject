<%-- 
    Document   : Login
    Created on : 17 Feb, 2019, 11:41:09 AM
    Author     : hp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="JS/jQuery.js"></script>
        <script src="JS/Login.js"></script>
        <script src="JS/util.js"></script>
        <script src="JS/Bootstrap.js"></script>
        <link href="CSS/Bootstrap.css" rel="stylesheet">
        <title>MarkMeIn : Login</title>
        <style>
            .modal-content{
                background-color: #7dafd2;
                /*background-color: darkcyan;*/
            }
            .btn-link{
                color:black;
            }
            .modal-heading h2{
                color:black;
                /*color:#ffffff;*/
            }
            #forgetPass:hover,#newUser:hover,#login:hover{
                color:black;
                font-weight: bold;
                cursor:pointer;
            }
            .form-group {
                position: relative;
                margin-bottom: 1.5rem;
            }

            .form-control-placeholder {
                position: absolute;
                top: 0;
                padding: 7px 0 0 13px;
                transition: all 200ms;
                opacity: 0.7;
                color:black;
            }

            .form-control:focus + .form-control-placeholder,
            .form-control:valid + .form-control-placeholder {
                font-size: 75%;
                transform: translate3d(0, -100%, 0);
                opacity: 1;
                color:black;
                font-weight:bold;
            }

        </style>
    </head>
    <script>
        var UserID = '<%=request.getParameter("userId")%>';
        var UserNAME = '<%=request.getParameter("userName")%>';
        var stamp = '<%=request.getParameter("stamp")%>';
        var d = new Date();
        var todayDate = d.getDate() + "-" + ("0" + (d.getMonth() + 1)).slice(-2) + "-" + d.getFullYear();

    </script>
    <body style="background-color: aliceblue;">
        <div class="container">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-heading">
                        <h2 class="text-center">MarkMeIn</h2>
                    </div>
                    <hr/>
                    <div class="modal-body" id="loginDiv">
                        <div class="form-group">
                            <input type="text" class="form-control" id="userName" required/>
                            <label class="form-control-placeholder" for="userName">User Name</label>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="userPassword" required/>
                            <label class="form-control-placeholder" for="userPassword">Password</label>
                        </div>
                        <div class="form-group text-center">
                            <button type="button" class="btn btn-success btn-lg" id="hitLogin">Login</button>
                        </div>
                        <div class="form-group text-center">
                            <a class="btn btn-link" id="forgetPass" style="text-decoration: underline;color:black;" onclick="showContentBody('forget');">Forgot Password?</a>
                            <a class="btn btn-link" id="newUser" style="text-decoration: underline;color:black;" onclick="showContentBody('createUser');">New User?</a>
                        </div>
                    </div>
                    <div class="modal-body" id="resetPasswordDiv" hidden>
                        <div class="form-group" hidden>
                            <input type="text" class="form-control" id="forgotUserId" required hidden/>
                            <input type="text" class="form-control" id="forgotUserName" required hidden/>
                        </div>
                        <div class="form-group" >
                            <input type="password" class="form-control" id="newUserPassword"  onfocusout="validatePassword(document.getElementById('newUserPassword'));" required/>
                            <label class="form-control-placeholder" for="newUserPassword">Password</label>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="newUserConfirmPassword" required/>
                            <label class="form-control-placeholder" for="newUserConfirmPassword">Confirm Password</label>
                        </div>
                        <div class="form-group text-center">
                            <button type="button" class="btn btn-success btn-lg" id="savePassword" onclick="updatePassword();">Update</button>
                            <a class="btn btn-link" id="login" style="text-decoration: underline;color:black;" onclick="showContentBody('login');">Login!</a>
                        </div>
                    </div>
                    <div class="modal-body" id="forgetDiv" hidden>
                        <div class="form-group">
                            <input type="text" class="form-control" id="forgetUserName" required/>
                            <label class="form-control-placeholder" for="forgetUserName">User Name</label>
                        </div>
                        <!--                        <div class="form-group">
                                                    <input type="text" class="form-control" id="forgetUserMail" required/>
                                                    <label class="form-control-placeholder" for="forgetUserMail">User Mail</label>
                                                </div>-->
                        <div class="form-group text-center">
                            <button type="button" class="btn btn-success btn-lg" id="savePassword" onclick="sendPasswordMail();">Send Mail</button>
                            <a class="btn btn-link" id="login" style="text-decoration: underline;color:black;" onclick="showContentBody('login');">Login!</a>
                        </div>
                    </div>
                    <div class="modal-body" id="createUserDiv" hidden>
                        <div class="form-group">
                            <input type="text" class="form-control" id="createUserName" required/>
                            <label class="form-control-placeholder" for="createUserName">Name</label>
                        </div>
                        <div class="form-group">
                            <select class="form-control" id="createDesignation">
                                <option>Department Chair</option>
                                <option>Assistant Professor</option>
                                <option>Professor</option>
                                <option>Adjunct Faculty</option>
                            </select>
                            <!--<input type="text" class="form-control" id="createDesignation" required/>-->
                            <label class="form-control-placeholder" for="createDesignation">Designation</label>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="createPassword" onfocusout="validatePassword(document.getElementById('createPassword'));" required/>
                            <label class="form-control-placeholder" for="createPassword">Password</label>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="createEmail" onfocusout="validateMail(document.getElementById('createEmail'));" required/>
                            <label class="form-control-placeholder" for="createEmail">Email</label>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="createContact"  onfocusout="validateContact(document.getElementById('createContact'));" required/>
                            <label class="form-control-placeholder" for="createContact">Phone Number</label>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="createSchool" required/>
                            <label class="form-control-placeholder" for="createSchool">School Name</label>
                        </div>
                        <div class="form-group text-center">
                            <button type="button" class="btn btn-success btn-lg" id="createUser" onclick="insertUser();">Create</button>
                            <a class="btn btn-link" id="login" style="text-decoration: underline;color:black;" onclick="showContentBody('login');">Login!</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!--<nav class="navbar navbar-expand-lg navbar-light bg-light">-->
        <!--<div class="container-fluid">-->
        <div class="row">
            <div class="col-sm-12 col-md-30 col-xs-12 col-lg-30">
                <p style="text-align: center;font-weight:bold;font-size:large;color:black;">CS 490 - MarkMeIn | Neha Gupta | ngupta1@neiu.edu</p>
            </div>
        </div>
        <!--</div>-->
        <!--</nav>-->
    </body>
</html>
