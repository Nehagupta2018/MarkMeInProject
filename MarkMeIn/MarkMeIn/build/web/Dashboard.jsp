<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">

        <title>MarkMeIn : Dashboard</title>

        <!-- Bootstrap CSS CDN -->
        <link rel="stylesheet" href="CSS/Bootstrap.css">
        <link rel="stylesheet" href="CSS/jquery-ui.min.css">
        <!-- Our Custom CSS -->
        <link rel="stylesheet" href="CSS/Dashboard.css">
        <link rel="stylesheet" href="CSS/FontAwesome.css">

        <!-- Font Awesome JS -->
        <script defer src="JS/Solid.js"></script>
        <script defer src="JS/FontAwsome.js"></script>
        <script src="JS/jQuery.js"></script>
        <script src="JS/Login.js"></script>
        <script src="JS/jQuery-ui.js"></script>
        <script src="JS/util.js"></script>
        <!-- Popper.JS -->
        <script src="JS/Popper.js"></script>
        <!-- Bootstrap JS -->
        <script src="JS/Bootstrap.js"></script>
        <script src="JS/Dashboard.js"></script>
        <script src="JS/Moment.js"></script>
        <style>
            .tblScrollDiv{
                width:100%;
                height:400px;
                overflow: auto;
            }
            .table-scroll {
                position:relative;
                max-width:95%;
                /*max-width:600px;*/
                margin:auto;
                overflow:hidden;
                border:1px solid #000;
            }
            .table-wrap {
                width:100%;
                overflow:auto;
            }
            .table-scroll table {
                width:100%;
                margin:auto;
                border-collapse:separate;
                border-spacing:0;
            }
            .table-scroll th, .table-scroll td {
                padding:5px 10px;
                border:1px solid #000;
                background: #fff;
                white-space:nowrap;
                vertical-align:top;
            }
            .table-scroll thead, .table-scroll tfoot {
                background:#f9f9f9;
            }
            .clone {
                position:absolute;
                top:0;
                left:0;
                pointer-events:none;
            }
            .clone th, .clone td {
                visibility:hidden;
            }
            .clone td, .clone th {
                border-color:transparent;
            }
            .clone tbody th {
                visibility:visible;
                /*color:red;*/
            }
            .clone .fixed-side {
                border:1px solid #000;
                /*background:#eee;*/
                visibility:visible;
            }
            .clone thead, .clone tfoot{background:transparent;}
            .hoverIcon{
                font-size: x-large;
            }
        </style>
    </head>
    <body>
        <div class="wrapper">
            <!-- Sidebar  -->
            <nav id="sidebar">
                <div class="sidebar-header">
                    <h3>MarkMeIn</h3>
                    <strong>MMI</strong>
                </div>
                <ul class="list-unstyled components">
                    <li class="active">
                        <a href="#classSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                            <i class="fas fa-home"></i>
                            Class Info
                        </a>
                        <ul class="collapse list-unstyled" id="classSubmenu">
                            <li>
                                <a class="insideAnchor" onclick="showContent('addClass');">Add Class</a>
                            </li>
                            <li>
                                <a class="insideAnchor" onclick="showContent('viewClass');">View Classes</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#studentSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                            <i class="fas fa-user"></i>
                            Student Info
                        </a>
                        <ul class="collapse list-unstyled" id="studentSubmenu">
                            <li>
                                <a class="insideAnchor" onclick="showContent('addStudent');">Add Student</a>
                            </li>
                            <li>
                                <a class="insideAnchor" onclick="showContent('viewStudent');">View/Edit Student</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#attendacneSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                            <i class="fas fa-copy"></i>
                            Attendance
                        </a>
                        <ul class="collapse list-unstyled" id="attendacneSubmenu">
                            <li>
                                <a class="insideAnchor" onclick="showContent('takeAttendance');">Take Attendance</a>
                            </li>
                            <li>
                                <a class="insideAnchor" onclick="showContent('viewAttendance');">View Attendance</a>
                            </li>
                            <!--                            <li>
                                                            <a class="insideAnchor">Update Attendance</a>
                                                        </li>-->
                        </ul>
                    </li>
                    <!--                    <li>
                                            <a onclick="showContent('contact');">
                                                <i class="fas fa-question"></i>
                                                Contact
                                            </a>
                                        </li>-->
                    <li>
                        <a onclick="showContent('about');">
                            <i class="fas fa-paper-plane"></i>
                            FAQ
                            <!--About-->
                        </a>
                    </li>
                </ul>
            </nav>

            <!-- Page Content  -->
            <div id="content">

                <nav class="navbar navbar-expand-lg navbar-light bg-light">
                    <div class="container-fluid">

                        <button type="button" id="sidebarCollapse" class="btn btn-info">
                            <i class="fas fa-align-left"></i>
                            <!--<span>Toggle Sidebar</span>-->
                        </button>
                        <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                            <i class="fas fa-align-justify"></i>
                        </button>

                        <div class="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul class="nav navbar-nav ml-auto">
                                <li class="nav-item active">
                                    <a class="nav-link" onclick="getUser();"><i class="fas fa-user"></i><span style="margin-left:5px;">My Profile</span></a>
                                </li>
                                <li class="nav-item active">
                                    <a class="nav-link" onclick="clearSession();"><i class="fas fa-sign-out-alt"></i><span style="margin-left:5px;">Log Out</span></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <!--<div id="dashContent">-->
                <div id="viewClassContent" class="contentDiv">
                    <h2>Current Classes</h2>
                    <br/><br/>
                    <div class="tblScrollDiv">
                        <table id="classSummary" border="1" width="75%">
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="viewAttendanceContent" class="contentDiv" hidden>
                    <h2>Attendance Summary</h2>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Select Class</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <select class="form-control" id="viewClassName" onchange="getTotalAttendance($(this));"></select>
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <div class="col-sm-12 col-xs-12 col-md-12 col-lg-12">
                            <div id="table-scroll" class="table-scroll">
                                <div class="table-wrap">
                                    <table id="viewAttendStuTbl" class="main-table" border="1" width="100%" style="text-align: center;">
                                        <thead>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="viewStudentContent" class="contentDiv" hidden>
                    <h2>Student Summary</h2>
                    <br/><br/>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Select Class</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <select class="form-control" id="stuClassName"></select>
                        </div>
                    </div>
                    <br/>
                    <div class="tblScrollDiv">
                        <table id="studentSummary" border="1" width="100%">
                            <thead style="text-align: center;">
                                <tr>
                                    <th>S.No.</th>
                                    <!--<th>Class Name</th>-->
                                    <th>Student Id</th>
                                    <th>Student Name</th>
                                    <th></th>
                                    <!--<th>Student LName</th>-->
                                    <!--<th>Student Email</th>-->
                                    <!--<th>Student Phone No.</th>-->
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="takeAttendanceContent" class="contentDiv" hidden>
                    <h2>Take Attendance</h2>
                    <br/><br/>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Select Class</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <select class="form-control" id="attendClassName" onchange="getClassDates($(this));"></select>
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Select Date</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <select class="form-control" id="attendClassDates" onchange="getAttendanceData($(this));"></select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12 col-xs-12 col-md-12 col-lg-12">
                            <div class="tblScrollDiv">
                                <table id="attendStudentTbl" border="1" width="100%" style="margin-top:2%;text-align: center;">
                                    <thead>
                                        <tr>
                                            <th>S.No.</th>
                                            <th>Student Name</th>
                                            <th>Present</th>
                                            <th>Absent</th>
                                            <th>Late</th>
                                            <th>Notes</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-12">
                            <center><button type="button" id="saveAttendance" class="btn btn-success btn-lg" style="margin-top:1%;">Save</button></center>
                        </div>
                    </div>
                </div>
                <div id="addStudentContent" class="contentDiv" hidden>
                    <h2>Add Student</h2>
                    <br/><br/>
                    <div class="row">
                        <!--                        <div class="col-sm-12 col-xs-12 col-md-6 col-lg-4">
                                                    <input type="radio" name="studentAdd" id="single" class="addStu" value="S" style="margin-left:2%;"><label style="margin-left:2%;">Single Entry</label>
                                                </div>-->
                        <div class="col-sm-12 col-xs-12 col-md-6 col-lg-4">
                            <input type="radio" name="studentAdd" id="multiple" class="addStu" value="M" style="margin-left:2%;"><label style="margin-left:2%;">Enter Students</label>
                            <!--<input type="radio" name="studentAdd" id="multiple" class="addStu" value="M" style="margin-left:2%;"><label style="margin-left:2%;">Multiple Entry</label>-->
                        </div>
                        <div class="col-sm-12 col-xs-12 col-md-6 col-lg-4">
                            <input type="radio" name="studentAdd" id="fileUpd" class="addStu" value="F" style="margin-left:2%;"><label style="margin-left:2%;">Upload File</label>
                        </div>
                    </div>
                    <center>
                        <div id="singleStu" class="stuContent" hidden>
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <label>Class Name</label>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <select class="form-control" id="studentClass" onchange="matchStudentMapping($(this), 'select', 'Single');"></select>
                                    <!--<input type="text" class="form-control" id="studentClass"/>-->
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <label>Student Id</label>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <input type="text" class="form-control" id="studentRoll"  onfocusout="matchStudentMapping($(this), 'input', 'Single');"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <label>Student First Name</label>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <input type="text" class="form-control" id="studentFname"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <label>Student Last Name</label>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                    <input type="text" class="form-control" id="studentLname"/>
                                </div>
                            </div>
                            <!--                            <div class="row">
                                                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                                                <label>Student Email</label>
                                                            </div>
                                                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                                                <input type="text" class="form-control" id="studentEmail" onfocusout="validateMail(document.getElementById('studentEmail'));"/>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                                                <label>Phone Number</label>
                                                            </div>
                                                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                                                <input type="text" class="form-control" id="studentContact" maxlength="10"/>
                                                            </div>
                                                        </div>-->
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 col-md-6 col-lg-12">
                                    <center><button type="button" id="saveSingleStudent" class="btn btn-success btn-lg" style="margin-top:1%;">Save</button></center>
                                </div>
                            </div>
                        </div>
                    </center>
                    <div id="multipleStu" class="stuContent" hidden>
                        <div class="row">
                            <div class="col-sm-12 col-xs-12 col-md-12 col-lg-12">
                                <table id="multipleStudentTbl" border="1" width="100%" style="margin-top:2%;text-align: center;">
                                    <thead>
                                        <tr>
                                            <th hidden>S.No.</th>
                                            <th>Class Name</th>
                                            <th>Student Id</th>
                                            <th>Student Last Name</th>
                                            <th>Student First Name</th>
                                            <!--<th>Student Email</th>-->
                                            <!--<th>Phone Number</th>-->
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12 col-xs-12 col-md-12 col-lg-12">
                                <button type="button" id="addStu" onclick="addMoreStudent();" class="btn btn-danger btn-lg" style="margin-top:1%;float:right;font-weight:bold;font-size:smaller;"><i class="fa fa-user-plus"></i></button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-12">
                                <center><button type="button" id="saveMutipleStudent" class="btn btn-success btn-lg" style="margin-top:1%;" hidden>Save</button></center>
                            </div>
                        </div>
                    </div>
                    <div id="fileUpdStu" class="stuContent" hidden>
                        <div class="row" style="margin-top:5%;">
                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                <label>Class Name</label>
                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                <select class="form-control" id="uploadClassName"></select>
                            </div>
                        </div>
                        <div class="row" style="margin-top:3%;">
                            <div class="col-sm-12 col-xs-12 col-md-6 col-lg-4">
                                <label>Upload File :</label>
                            </div>
                            <div class="col-sm-12 col-xs-12 col-md-6 col-lg-8">
                                <input type="file" id="studentFile" name="studentFile" style="display:none;"/>
                                <a class="btn btn-warning btn-lg" id="uploadDocument" style="cursor: pointer;">Upload</a>
                                <span id="studentDOCName"></span>
                            </div>
                        </div>
                        <div class="row" style="margin-top:3%;">
                            <div class="col-sm-12 col-xs-12 col-md-8 col-lg-6">
                                <a onclick="downloadTemplate();" style="text-decoration: underline;cursor:pointer;">Please download template first for excel or csv file</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--                <div id="contactContent" class="contentDiv" hidden>
                                    <h2>Contact</h2>
                                    <br/><br/>
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                            <p></p>
                                        </div>
                                    </div>
                                </div>-->
                <div id="aboutContent" class="contentDiv" hidden>
                    <h2>FAQs</h2>
                    <br/><br/>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="container">
                                <div class="panel-group" id="accordion">
                                    <div class="panel panel-default" style="border:1px grey solid;border-radius:4px;">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse1" style="margin-left:4px;">
                                                    1. How can I reset my password?</a>
                                            </h4>
                                        </div>
                                        <div id="collapse1" class="panel-collapse collapse in">
                                            <div class="panel-body">If you forgot your password, then go to 'Forgot Password' button on Login Screen and enter your Username. You will get a reset password link on the registered email address. Visit that link and change your password.  One other way is 'Reset Password' tab on 'My Profile' menu list.</div>
                                        </div>
                                    </div>
                                    <br/>
                                    <div class="panel panel-default" style="border:1px grey solid;border-radius:4px;">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse2" style="margin-left:4px;">
                                                    2. How can I add students into multiple classes at the same time?</a>
                                            </h4>
                                        </div>
                                        <div id="collapse2" class="panel-collapse collapse">
                                            <div class="panel-body">There are three options to add students to classes: Single entry and multiple entry and through file.<br/>
                                                Single entry and File options allows users to add students into one class only.<br/>
                                                Multiple entry feature allows users to add students into multiple classes at the same time by giving a class selection dropdown for each entry.</div>
                                        </div>
                                    </div>
                                    <br/>
                                    <div class="panel panel-default" style="border:1px grey solid;border-radius:4px;">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse3" style="margin-left:4px;">
                                                    3. What is the use of file format available for adding students?</a>
                                            </h4>
                                        </div>
                                        <div id="collapse3" class="panel-collapse collapse">
                                            <div class="panel-body">Users can download the file format available and fill in the student's information as per the template available in it. This is the way system accepts the details to be added.</div>
                                        </div>
                                    </div>
                                    <br/>
                                    <div class="panel panel-default" style="border:1px grey solid;border-radius:4px;">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse4" style="margin-left:4px;">
                                                    4. Where can I check the summary of each student present and absent details?</a>
                                            </h4>
                                        </div>
                                        <div id="collapse4" class="panel-collapse collapse">
                                            <div class="panel-body">'View Attendance' page is the best to look for the student's total present, absent and late summary for each class.</div>
                                        </div>
                                    </div>
                                    <br/>
                                    <div class="panel panel-default" style="border:1px grey solid;border-radius:4px;">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse5" style="margin-left:4px;">
                                                    5. Where can I add minutes for late comers?</a>
                                            </h4>
                                        </div>
                                        <div id="collapse5" class="panel-collapse collapse">
                                            <div class="panel-body">On 'Take Attendance' page we have provided a Notes feature beside 'Late' column in which users can specify number of minutes a student is late to the class. Any other required information can also be added and saved.</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="addClassContent" class="contentDiv" hidden>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Class Name</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <!--<input type="text" class="form-control" id="className" onfocusout="matchClassExistence($(this));"/>-->
                            <input type="text" class="form-control" id="className"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Class Level</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <select class="form-control" id="classLevel">
                                <!--                                <option>Graduate</option>
                                                                <option>Under Graduate</option>-->
                            </select>
                            <!--<input type="text" class="form-control" id="classLevel"/>-->
                        </div>
                    </div>
                    <!--                    <div class="row">
                                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                                <label>Class Crn</label>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                                                <input type="text" class="form-control" id="classCrn"/>
                                            </div>
                                        </div>-->
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Section</label>
                            <!--<label>Credit Hours</label>-->
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <input type="text" class="form-control" id="creditHours"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Class Term</label>
                        </div>
                        <div class="col-xs-6 col-sm-6 col-md-3 col-lg-2">
                            <select class="form-control" id="termType">
                            </select>
                        </div>
                        <div class="col-xs-6 col-sm-6 col-md-3 col-lg-2">
                            <select class="form-control" id="termYear"></select>
                            <!--<input type="text" class="form-control" id="termYear"/>-->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Start Date</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <input type="date" class="form-control" id="startDate" placeholder="dd-mm-yyyy"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>End Date</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <input type="date" class="form-control" id="endDate" placeholder="dd-mm-yyyy"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                            <label>Class Meetings</label>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-8">
                            <div class="input-group" style="margin-top:1%;">
                                <label class="checkbox-label"><input type="checkbox" id="1" class="meetClass" value="1">    Mon</label>
                                <label class="checkbox-label"><input type="checkbox" id="2" class="meetClass" value="2" style="margin-left:7px;">    Tues</label>
                                <label class="checkbox-label"><input type="checkbox" id="3" class="meetClass" value="3" style="margin-left:7px;">    Wed</label>
                                <label class="checkbox-label"><input type="checkbox" id="4" class="meetClass" value="4" style="margin-left:7px;">    Thurs</label>
                                <label class="checkbox-label"><input type="checkbox" id="5" class="meetClass" value="5" style="margin-left:7px;">    Fri</label>
                                <label class="checkbox-label"><input type="checkbox" id="6" class="meetClass" value="6" style="margin-left:7px;">    Sat</label>
                                <label class="checkbox-label"><input type="checkbox" id="0" class="meetClass" value="0" style="margin-left:7px;">    Sun</label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-8 col-lg-12">
                            <center><button type="button" id="generateClass" class="btn btn-success">Show Meeting Dates</button></center>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12 col-xs-12 col-md-12 col-lg-12">
                            <div class="tblScrollDiv">
                                <table id="classMeetTable" border="1" width="100%" style="margin-top:2%;text-align: center;">
                                    <thead>
                                        <tr>
                                            <th>S.No.</th>
                                            <th>Day</th>
                                            <th>Date</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-12">
                            <center><button type="button" id="saveClassMeeting" onclick="saveClassMeetings();" class="btn btn-success btn-lg"  style="margin-top:1%;" hidden>Create Class</button></center>
                        </div>
                    </div>
                </div>
            </div>
            <div id="docDiv" hidden></div>
        </div>

        <div class="modal" id="myModal" role="dialog" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <!--                     Modal Header        -->              
                    <div class="modal-header">
                        <h4 class="modal-title">Class Details</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <!-- Modal body -->
                    <div class="modal-body">
                        <div class="tblScrollDiv">
                            <table id="classDet" border="1" width="100%">
                                <thead style="text-align: center;">
                                    <tr>
                                        <th hidden>S.No.</th>
                                        <!--<th>Class Term</th>-->
                                        <!--<th>Class Id</th>-->
                                        <th>Class Name</th>
                                        <th>Class Day</th>
                                        <th>Class Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Modal footer -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>

                </div>
            </div>
        </div>

        <div class="modal" id="stuModel" role="dialog" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <!--                     Modal Header        -->              
                    <div class="modal-header">
                        <h4 class="modal-title">Student Details</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <!-- Modal body -->
                    <div class="modal-body">
                        <div class="tblScrollDiv">
                            <table width="100%">
                                <tbody>
                                    <tr>
                                        <td>Student Id</td>
                                        <td id="stuId"></td>
                                    </tr>
                                    <tr>
                                        <td>Student Last Name</td>
                                        <td><input id="SLName" class="form-control"  type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td>Student First Name</td>
                                        <td><input class="form-control" id="SFName" type="text"/></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Modal footer -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" onclick="updateStuDet();">Update</button>
                    </div>

                </div>
            </div>
        </div>
        <div class="modal" id="userModel" role="dialog" data-backdrop="static" data-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <!--                     Modal Header        -->              
                    <div class="modal-header">
                        <h4 class="modal-title">User Details</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>

                    <!-- Modal body -->
                    <div class="modal-body">
                        <div class="tblScrollDiv">
                            <table width="100%">
                                <tbody>
                                    <tr>
                                        <td>Name</td>
                                        <td id="uName"></td>
                                        <td hidden><input type="text" class="form-control" id="forgetUserName" disabled/></td>
                                    </tr>
                                    <tr>
                                        <td>Designation</td>
                                        <td><input class="form-control" id="uDesig" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td>Phone No</td>
                                        <td><input class="form-control" id="uPhone" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td>Email</td>
                                        <td><input class="form-control" id="uMail" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td>School Name</td>
                                        <td><input class="form-control" id="sName" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><a id="uReset" style="cursor: pointer;color:blue;font-weight:bold;text-decoration: underline;">Reset Password</a></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Modal footer -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" onclick="updateUserDet();">Update</button>
                    </div>

                </div>
            </div>
        </div>
        <!--<nav class="navbar navbar-expand-lg navbar-light bg-light">-->
        <!--<div class="container-fluid">-->
        <div class="row">
            <div class="col-sm-12 col-md-12 col-xs-12 col-lg-12">
                <p style="text-align: center;font-weight:bold;font-size:large;color:black;">MarkMeIn | Neha Gupta | </p>
            </div>
        </div>
        <!--</div>-->
        <!--</nav>-->
    </body>

</html>