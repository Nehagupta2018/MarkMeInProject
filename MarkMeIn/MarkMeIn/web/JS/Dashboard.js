var userId = "";
var userCount = 1;
var classList = [];
var studentList = [];
$(document).ready(function () {
    jQuery(".main-table").clone(true).appendTo('#table-scroll').addClass('clone');
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
    $("input[name='studentAdd']").on('change', function () {
        $(".stuContent").hide();
        count = 1;
        var id = $(this).attr("id");
        $("#multipleStudentTbl tbody").html("");
        $("#saveMutipleStudent").hide();
        $("input").val("");
        $("select").html("");
        $("#" + id + "Stu").removeAttr("hidden");
        $("#" + id + "Stu").attr("style", "display:block;");
        if (id === "single") {
            getClassList("Single", "0");
        } else if (id === "fileUpd") {
            getClassList("Upload", "0");
        }

    });
    var loginData = JSON.parse(localStorage.getItem("loginData"));
    userId = loginData.UserId;
    getClassData(userId);
    $("#generateClass").click(function () {
        debugger;
        $("#classMeetTable tbody").html("");
        classList = [];
        $(".meetClass").each(function () {
            if ($(this).prop("checked")) {
                classList.push($(this).attr("id"));
            }
        });
        if (classList.length === 0) {
            $("#classMeetTable").hide();
            alert("Please select class meetings first.");
        } else {
            if ($("#startDate").val() === "" || $("#endDate").val() === "") {
                $("#classMeetTable").hide();
                alert("Please select Start and End date both.");
            } else {
                $("#saveClassMeeting").removeAttr("hidden");
                $("#classMeetTable").show();
                var startD = new Date($("#startDate").val());
                var startDay = startD.getDay();
                var endD = new Date($("#endDate").val());
                var endDay = endD.getDay();
                if (startD >= endD) {
                    alert("Start Date should be before end date.");
                } else {
                    var start = moment($("#startDate").val());
                    var end = moment($("#endDate").val());
                    var day = 0;
                    var result = {};
                    for (var i = 0; i < classList.length; i++) {
                        var dateList = [];
                        day = parseInt(classList[i]);
                        var current = start.clone();
                        if (startDay <= day) {
                            current.day(startDay + (day - startDay));
                            dateList.push(current.clone());
                        }
                        while (current.day(7 + day).isBefore(end)) {
                            dateList.push(current.clone());
                        }
                        if (endDay === day) {
                            dateList.push(end.clone());
                        }
                        result[day] = dateList.map(m => m.format('LL'));
                    }

//                    console.log(result);
                    setTables(result);
                }
            }
        }
    });
    $("#uReset").click(function () {
        sendPasswordMail();
        alert("Password reset link has been sent on your mail.");
        localStorage.setItem("loginData", "");
        window.location = "../MarkMeIn/Login.jsp";
    });
    $("#stuClassName").on("change", function () {
        if ($(this).val() !== "") {
            getStudentData(userId, $(this).find("option:selected").attr("classid"));
        }
    });

    $("#uploadDocument").click(function ()
    {
        $("#studentFile").click();
        $("#studentFile").unbind("change");
        $("#studentFile").on("change", function ()
        {
            var form_data = new FormData();
            var fileAttr = new Object();
            var fileObj = $("#studentFile").prop("files")[0];
            var filePath = $("#studentFile").val();
            fileAttr.Name = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length);
            fileAttr.Extension = filePath.substring(filePath.lastIndexOf("."), filePath.length);
            fileAttr.NameOnly = fileAttr.Name.substring(0, fileAttr.Name.lastIndexOf("."));
            var dt = new Date();
            var ext = fileAttr.Extension;
            if (ext === ".xlsx" || ext === ".csv") {
                var fileName = "StudentRecord_" + dt.getMonth() + "_" + dt.getMinutes() + ext;
                $("#studentDOCName").text(fileName);
                form_data.append("fileName", fileName);
                form_data.append("classId", $("#uploadClassName").find("option:selected").attr("classid"));
                form_data.append("className", $("#uploadClassName").find("option:selected").text());
                form_data.append("userId", userId);
                form_data.append("extension", ext);
                form_data.append("file", $("#studentFile").prop("files")[0]);
                $.ajax({
                    url: "../MarkMeIn/DocUpload",
                    dataType: 'script',
                    cache: false,
                    contentType: false,
                    processData: false,
                    type: 'post',
                    data: form_data,
                    success: function (response) {
//                        alert(response);
                        if (response === "SUCCESS") {
                            alert("Records saved successfully.");
                        } else if (response === "EMPTY") {
                            alert("File is empty. Please upload correct file.");
                            $("#studentDOCName").html("");
                        } else {
                            alert("Please upload correct file.");
                            $("#studentDOCName").html("");
                        }
                    },
                    error: function () {
                        alert("Some Error while Uploading File.");
                        $("#studentDOCName").html("");
                    }
                });
            } else {
                alert("Please upload excel or csv file.");
                $("#studentDOCName").html("");
            }
        });
    });
    $("#saveSingleStudent").click(function () {
        var studentData = {};
        studentList = [];
        studentData["seq"] = 1;
        studentData["userId"] = userId;
        var mandFlag = true;
        studentData["type"] = "Single";
        studentData["className"] = $("#studentClass").find("option:selected").text();
        studentData["classId"] = $("#studentClass").find("option:selected").attr("classid");
        $("div#singleStu").find("input[type='text']").each(function () {
            if ($(this).val().trim() !== "") {
                studentData[$(this).attr("id")] = $(this).val();
            } else {
                mandFlag = false;
                alert("Please fill all details.");
                return false;
            }
        });
        studentList.push(studentData);
        console.log(JSON.stringify(studentList));
        if (mandFlag) {
            saveStudentInfo(studentList);
        }
    });
    $("#saveMutipleStudent").click(function () {
        var studentData = {};
        studentList = [];
        var mandFlag = true;
        var arr = ["studentRoll", "studentLname", "studentFname"];
//        var arr = ["studentRoll", "studentFname", "studentLname", "studentEmail", "studentContact"];
        $("#multipleStudentTbl tbody").find("tr").each(function () {
            studentData = {};
            var tdCount = 0;
            studentData["type"] = "Multiple";
            studentData["userId"] = userId;
            studentData["seq"] = $(this).find("td:eq(0)").text();
            studentData["className"] = $(this).find("select").find("option:selected").text();
            studentData["classId"] = $(this).find("select").find("option:selected").attr("classid");
            $(this).find("td:gt(1)").each(function () {
                if ($(this).find("input").val().trim() !== "") {
                    studentData[arr[tdCount]] = $(this).find("input").val();
                    tdCount++;
                } else {
                    alert("Please fill all details.");
                    mandFlag = false;
                    return false;
                }
            });
            if (!mandFlag) {
                return false;
            }
            studentList.push(studentData);
        });
        console.log(JSON.stringify(studentList));
        if (mandFlag) {
            saveStudentInfo(studentList);
        }
    });
    $("#saveAttendance").click(function () {
        var attendanceList = [];
        var mandFlag = true;
        $("#attendStudentTbl tbody tr").each(function () {
            if ($(this).find("select").val() === "") {
                mandFlag = false;
                return false;
            }
        });
        if (mandFlag) {
            $("#attendStudentTbl tbody tr").each(function () {
                var attendanceData = {};
                attendanceData["userId"] = userId;
                attendanceData["classId"] = $("#attendClassName").find("option:selected").attr("classid");
                attendanceData["className"] = $("#attendClassName").find("option:selected").text();
                attendanceData["studentId"] = $(this).attr("studentId");
                attendanceData["studentName"] = $(this).find("td:eq(1)").text();
                attendanceData["sequence"] = $(this).find("td:eq(0)").text();
                attendanceData["presentAbsent"] = $(this).find("input[type='radio']:checked").val();
                attendanceData["lateEntry"] = $(this).find("input[type='checkbox']").prop("checked");
                attendanceData["classMeetDay"] = $("#attendClassDates").val();
                attendanceData["notes"] = $(this).find("td:eq(5)").find("textarea").val();
                attendanceList.push(attendanceData);
            });
//            alert(JSON.stringify(attendanceList));
            if (JSON.stringify(attendanceList) !== "[]") {
                $.ajax({
                    url: "../MarkMeIn/MMIController",
                    async: false,
                    data: "callType=insertAttendance&dataList=" + JSON.stringify(attendanceList),
                    success: function (data) {
                        console.log(data);
                        if (data !== "") {
                            alert("Data Saved succesfully.");
                            $("#attendStudentTbl tbody").html("");
                        }
                    },
                    error: function (error) {
                        alert("Ajax error---" + error);
                    }
                });
            }
        } else {
            alert("Please mark attendance for all students.");
        }
    });
});
function saveStudentInfo(dataList) {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=insertStudentInfo&dataList=" + JSON.stringify(dataList),
        success: function (data) {
            console.log(data);
            if (data !== "") {
                alert("Data Saved succesfully.");
                $("input").val("");
                $("#multipleStudentTbl tbody").html("");
                $("#saveMutipleStudent").hide();
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function getClassList(flag, count) {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=getClassList&userId=" + userId,
        success: function (data) {
            console.log(data);
            var id = "";
            if (flag === "Single") {
                id = "studentClass";
            } else if (flag === "Multiple") {
                id = "studentClass" + count;
            } else if (flag === "Upload") {
                id = "uploadClassName";
            } else if (flag === "attendance") {
                id = "attendClassName";
            } else if (flag === "viewAttend") {
                id = "viewClassName";
            } else if (flag === "viewStu") {
                id = "stuClassName";
            }
            if (data !== "[]") {
                console.log(data);
                var responseData = JSON.parse(data);
                $("#" + id).html("");
                $("#" + id).append("<option value=''>Select</option>");
                for (var i = 0; i < responseData.length; i++) {
//                    $.each(responseData[i], function (key, value) {
                    $("#" + id).append("<option classid='" + responseData[i].CLASSID + "'>" + responseData[i].CLASSNAME + "</option>");
//                        $("#" + id).append("<option classid='" + key + "'>" + value + "</option>");
//                    });
                }
            } else {
                alert("No class exists. Please add class first.");
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function getTotalAttendance(element) {
    $("#viewAttendStuTbl tbody").html("");
    $("#viewAttendStuTbl thead").html("");
    var classId = element.find("option:selected").attr("classid");
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=getTotalAttendance&classId=" + classId + "&userId=" + userId,
        success: function (data) {
            debugger;
            console.log(data);
            if (data !== "{}") {
                var responseData = JSON.parse(data);
                var totalDays = "";
                $.each(responseData, function (key, value) {
                    if (key === "classMeetList") {
                        var headHtml = "<tr><th class='fixed-side'>S.No.</th><th class='fixed-side'>Name</th><th class='fixed-side'>P</th><th class='fixed-side'>Ab</th><th class='fixed-side'>Lt</th>";
                        for (var i = 0; i < value.length; i++) {
                            headHtml = headHtml + "<th>" + value[i] + "</th>";
                        }
                        totalDays = value.length;
                        headHtml = headHtml + "</tr>";
                        $("#viewAttendStuTbl thead").append(headHtml);
                    } else if (key === "classStuList") {
                        var count = 1;
                        $.each(value, function (innerKey, innerValue) {
                            debugger;
                            var presentDays = 0;
                            var absentDays = 0;
                            var lateInDays = 0;
                            var bodyRowHtml = "<tr><th class='fixed-side'>" + count + "</th><th class='fixed-side'>" + innerKey + "</th><td id='present_" + count + "' class='fixed-side'></td><td id='absent_" + count + "' class='fixed-side'></td><td id='late_" + count + "' class='fixed-side'></td>";
                            var i = 0;
                            for (var j = 0; j < totalDays; j++) {
//                                debugger;
                                var classText = $("#viewAttendStuTbl thead tr").find("th:eq(" + (parseInt(j) + 5) + ")").text();
                                classText = classText.substring(0, (classText.length / 2));
                                if (i < innerValue.length && innerValue[i].classMeet === classText) {

                                    if (innerValue[i].presenAbsent === "P") {
                                        if (innerValue[i].lateIn === "Yes") {
                                            bodyRowHtml = bodyRowHtml + "<td>" + innerValue[i].presenAbsent + "/Late &nbsp;<span title='" + innerValue[i].notes + "' onclick='showNotes($(this));' style='font-weight:bold;color:green;'>&ndash;</span></td>";
                                            lateInDays++;
                                        } else {
                                            bodyRowHtml = bodyRowHtml + "<td>" + innerValue[i].presenAbsent + " &nbsp;<span title='" + innerValue[i].notes + "' style='font-weight:bold;color:green;'>&ndash;</span></td>";
                                        }
                                        presentDays++;
                                    } else if (innerValue[i].presenAbsent === "Ab") {
                                        bodyRowHtml = bodyRowHtml + "<td>" + innerValue[i].presenAbsent + "</td>";
                                        absentDays++;
                                    }
                                    i = i + 1;
                                } else {
                                    bodyRowHtml = bodyRowHtml + "<td> &nbsp; </td>";
                                }
                            }
                            bodyRowHtml = bodyRowHtml + "</tr>";
                            $("#viewAttendStuTbl tbody").append(bodyRowHtml);
                            debugger;
                            $("#viewAttendStuTbl tbody tr").find("td[id='present_" + count + "']").html(presentDays);
                            $("#viewAttendStuTbl tbody tr").find("td[id='absent_" + count + "']").html(absentDays);
                            $("#viewAttendStuTbl tbody tr").find("td[id='late_" + count + "']").html(lateInDays);
                            count = count + 1;
//                            bodyRowHtml = bodyRowHtml + "<td>" + presentDays + "/" + totalDays + "</td></tr>";
                        });
                    }
                });
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function showNotes(element) {
    alert(element.attr("title"));
}

function getClassDates(element) {
    var classId = element.find("option:selected").attr("classid");
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=getClassMeets&classId=" + classId + "&userId=" + userId,
        success: function (data) {
            debugger;
            console.log(data);
            if (data !== "{}") {
                var responseData = JSON.parse(data);
                $("#attendClassDates").html("");
                let firstOption = "";
                let lastOption = "";

                for (var i = 0; i < responseData.length; i++) {
                    let pastText = "";
                    var startD = new Date(responseData[i].class);
                    var endD = new Date();
//                    $("#attendClassDates").append("<option>" + responseData[i] + "</option>");
                    if (startD <= endD) {
//                        console.log("Found");
                        pastText = "&nbsp;&nbsp;&nbsp;<span><i class='fas fa-info-circle' title='Date is in Past' onclick='dateBeforeAlert();' style='font-weight:bold;color:red;'></i></span>";
                    }
                    if (responseData[i].record === "N") {
                        firstOption = firstOption + "<option style='color:red;'>" + responseData[i].class + pastText + "</option>";
                    } else {
                        lastOption = lastOption + "<option>" + responseData[i].class + pastText + "</option>";
                    }
                }
                $("#attendClassDates").append(firstOption);
                $("#attendClassDates").append(lastOption);
                getClassStudentList();
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}
function dateBeforeAlert() {
    alert("Date is in Past.");
}

function getClassTerms() {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=getTermList&userId=" + userId,
        success: function (data) {
            console.log(data);
            if (data !== "[]") {
                var responseData = JSON.parse(data);
                for (var i = 0; i < responseData.length; i++) {
                }
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function getAttendanceData(element) {
    var classId = $("#attendClassName").find("option:selected").attr("classid");
    var date = element.val();
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=getAttendanceData&classId=" + classId + "&userId=" + userId + "&classDate=" + date,
        success: function (data) {
            debugger;
            console.log(data);
            if (data !== "[]" && data !== "") {
                var responseData = JSON.parse(data);
               for(var i=0;i<responseData.length;i++){
                   var trObj= $("#attendStudentTbl tbody").find("tr[studentId='"+responseData[i].studentID+"']");
                   if(responseData[i].pAb === "P"){
                        trObj.find("td:eq(2)").find("input[type=radio]").prop("checked",true);
                        trObj.find("td:eq(3)").find("input[type=radio]").prop("checked",false);
                        trObj.find("td:eq(5)").find("textarea").val(responseData[i].notes);
                   }else if(responseData[i].pAb === "Ab"){
                        trObj.find("td:eq(3)").find("input[type=radio]").prop("checked",true);
                        trObj.find("td:eq(2)").find("input[type=radio]").prop("checked",false);
                         trObj.find("td:eq(4)").find("input[type=checkbox]").prop("disabled",true);
                         trObj.find("td:eq(5)").find("textarea").prop("disabled",true);
                   }
                    if(responseData[i].latein === "true"){
                       trObj.find("td:eq(4)").find("input[type=checkbox]").prop("checked",true);
                   }else{
                       trObj.find("td:eq(4)").find("input[type=checkbox]").prop("checked",false);
                   }
               }
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function getClassStudentList() {
    var classId = $("#attendClassName").find("option:selected").attr("classid");
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=getClassStuList&classId=" + classId + "&userId=" + userId,
        success: function (data) {
            debugger;
            console.log(data);
            if (data !== "[]" && data !== "") {
                var responseData = JSON.parse(data);
                $("#attendStudentTbl tbody").html("");
                for (var i = 0; i < responseData.length; i++) {
                    $.each(responseData[i], function (key, value) {
                        var trHtml = "<tr studentId='" + key + "'><td>" + (parseInt(i) + 1) + "</td><td>" + value + "</td>"
                                + "<td><input type='radio' value='P' name='attendSelect" + (parseInt(i) + 1) + "'></td>"
                                + "<td><input type='radio' value='Ab' name='attendSelect" + (parseInt(i) + 1) + "'></td>"
                                + "<td><input type='checkbox' id='lateAttend" + (parseInt(i) + 1) + "'/></td>"
                                + "<td><textarea class='form-control' id='notes" + (parseInt(i) + 1) + "'></textarea></td>"
                                + "</tr>";
                        $("#attendStudentTbl tbody").append(trHtml);
                    });
                }
                $("input[name*='attendSelect']").on('change', function (event) {
                    if ($(this).val() === "Ab") {
                        $(this).closest("tr").find("td:eq(4)").find("input[type=checkbox]").prop("checked", false);
                        $(this).closest("tr").find("td:eq(4)").find("input[type=checkbox]").attr("disabled", "disabled");
                        $(this).closest("tr").find("td:eq(5)").find("textarea").val("");
                        $(this).closest("tr").find("td:eq(5)").find("textarea").attr("disabled", "disabled");
                    } else {
                        $(this).closest("tr").find("td:eq(4)").find("input[type=checkbox]").removeAttr("disabled");
                        $(this).closest("tr").find("td:eq(5)").find("textarea").removeAttr("disabled");
                    }
                });
            }

        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function saveClassMeetings() {
    if ($("#className").val().trim() !== "" && $("#classLevel").val().trim() !== "" && $("#creditHours").val().trim() !== "") {
        var classDates = [];
        $("#classMeetTable tbody").find("tr").each(function () {
            var data = {};
            data["dateId"] = $(this).attr("id");
            data["dateDay"] = $(this).find("td:eq(1)").text();
            data["dateStr"] = $(this).find("td:eq(2)").text();
            data["seq"] = $(this).find("td:eq(0)").text();
            classDates.push(data);
        });
        var dataMap = {
            userId: userId,
            className: $("#className").val(),
            classLevel: $("#classLevel").val(),
            creditHours: $("#creditHours").val(),
            startDate: $("#startDate").val(),
            endDate: $("#endDate").val(),
            classTerm: $("#termType").val() + "," + $("#termYear").val(),
            classMeetings: JSON.stringify(classList),
            classDateObj: classDates
        }
        matchClassExistence($("#className"));
        console.log(JSON.stringify(dataMap));
        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: "callType=insertClassInfo&dataMap=" + JSON.stringify(dataMap),
            success: function (data) {
                console.log(data);
                if (data !== "") {
                    alert("Class created succesfully.");
                    $("input").val("");
                    $("#classMeetTable tbody").html("");
                    $("#saveClassMeeting").hide();
                    $(".meetClass").prop("checked", "");
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

function matchClassExistence(element) {
    console.log(element.val());
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: "callType=checkClassExist&className=" + element.val(),
        success: function (data) {
            console.log(data);
            if (data === "Class Exist") {
                alert("Class name already exists. Please try another name.");
                element.val("");
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function matchStudentMapping(element, flag, entryType) {
    var studentId = "";
    var classId = "";
    if (element.val().trim() !== "") {
        if (flag === "input") {
            studentId = element.val().trim();
            if (entryType === "Single") {
                classId = $("#studentClass").find("option:selected").attr("classid");
            } else {
                classId = element.closest("tr").find("select").find("option:selected").attr("classid");
            }

        } else if (flag === "select") {
            classId = element.find("option:selected").attr("classid");
            if (entryType === "Single") {
                studentId = $("#studentRoll").val().trim();
            } else {
                studentId = element.closest("tr").find("td:eq(2)").find("input").val().trim();
            }
        }
        console.log(studentId + "---" + classId);
        if (studentId !== "" && (classId !== "" || typeof classId !== "undefined")) {
            $.ajax({
                url: "../MarkMeIn/MMIController",
                async: false,
                data: "callType=matchStudentMap&studentId=" + studentId + "&classId=" + classId,
                success: function (data) {
                    console.log(data);
                    if (data === "Match Found") {
                        alert("Student is already added to this particular class. Please change class or studentid.");
                        $("input[id*=studentClass]").val("");
                        if (flag === "input") {
                            element.val("");
                        }
                    }
                },
                error: function (error) {
                    alert("Ajax error---" + error);
                }
            });
        } else {
            alert("Please select classId & student Id both.");
        }
    }
}

function downloadTemplate() {
    var frame = "<iframe width='1200px' height='600px' src='../MarkMeIn/DownloadTemplate' id='iframe1'/>";
    $("#docDiv").html(frame);
    $("#iframe1").hide();
}

function addMoreStudent() {
    var html = "<tr>"
            + "<td hidden>" + count + "</td>"
            + "<td> <select style='width:100%;' class='form-control' id='studentClass" + count + "'  onchange='matchStudentMapping($(this), \"select\", \"Multiple\");'></select></td>"
//            + "<td><input type='text' class='form-control' style='width:100%;' id='studentClass" + count + "'/></td>"
            + "<td><input type='text' class='form-control' style='width:100%;' id='studentRoll" + count + "'  onfocusout='matchStudentMapping($(this), \"input\", \"Multiple\");'/></td>"
            + "<td><input type='text' class='form-control' style='width:100%;' id='studentLname" + count + "'/></td>"
            + "<td><input type='text' class='form-control' style='width:100%;' id='studentFname" + count + "'/></td>"
//            + "<td><input type='text' class='form-control' style='width:100%;' onfocusout='validateMail(document.getElementById(\"studentEmail" + count + "\"));' id='studentEmail" + count + "'/></td>"
//            + "<td><input type='text' class='form-control' maxlength='10' style='width:100%;' id='studentContact" + count + "'/></td>"
            + "</tr>";
    $("#multipleStudentTbl tbody").append(html);
    getClassList("Multiple", count);
    count++;
    $("#saveMutipleStudent").removeAttr("hidden");
    $("#saveMutipleStudent").attr("style", "display:block;");
}

function getClassData(userId) {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: {
            userId: userId,
            callType: "getClassList"
        },
        success: function (data) {
            debugger;
            console.log(data);
            if (data !== "[]") {
                var responseData = JSON.parse(data);
                var count = 0;
                var dataHTML = "";
                for (var i = 0; i < responseData.length; i++) {
                    debugger;
                    dataHTML = dataHTML + "<tr><td style='width:100%;font-weight:bold;cursor:pointer;text-align:center;' classid='" + responseData[i].CLASSID + "'>" + responseData[i].CLASSNAME + "&nbsp;&nbsp;&nbsp; - &nbsp;&nbsp;&nbsp;" + responseData[i].CREDITHOURS +
                            "<br/>" + responseData[i].STARTDATE + "&nbsp;&nbsp;&nbsp; : &nbsp;&nbsp;&nbsp;" + responseData[i].ENDDATE +
                            "<br/>Number of Students  : &nbsp;&nbsp;&nbsp;" + responseData[i].stuCount +
                            "<br/>Attend Rate : &nbsp;&nbsp;&nbsp;" + responseData[i].presentRate + "%" +
                            "<hr><p><i class='fas fa-list-alt hoverIcon' style='color:black;' title='View Attendance' onclick='viewClassAttend($(this));'></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + "<i class='fas fa-user-plus hoverIcon' style='color:black;' title='Add Student' onclick='addStudentClass($(this));'></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + "<i class='fas fa-user-edit hoverIcon' style='color:black;' title='View/Edit Student' onclick='viewStudentClass($(this));'></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + "<i class='fas fa-clipboard hoverIcon' style='color:black;' title='Take Attendance' onclick='takeClassAttend($(this));'></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + "<i class='fas fa-edit hoverIcon' style='color:black;' title='Edit Class' onclick='getClassDetails($(this),\"Edit\");'></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                            + "<i class='fa fa-trash hoverIcon' style='color:red;' title='Delete' onclick='removeClassEle($(this));'></i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p></td></tr>";
                }
                $("#classSummary tbody").append(dataHTML);
            } else {
                alert("Sorry you don't have any class existing.");
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function viewClassAttend(element) {
    var classId = element.closest("td").attr("classid");
    $("#attendacneSubmenu").find("li:eq(1)").find("a").trigger("click");
    $("#viewClassName").find("option[classid='" + classId + "']").attr("selected", "selected");
    getTotalAttendance($("#viewClassName"));
}

function takeClassAttend(element) {
    var classId = element.closest("td").attr("classid");
    $("#attendacneSubmenu").find("li:eq(0)").find("a").trigger("click");
    $("#attendClassName").find("option[classid='" + classId + "']").attr("selected", "selected");
    getClassDates($("#attendClassName"));
}

function addStudentClass(element) {
    var classId = element.closest("td").attr("classid");
    $("#studentSubmenu").find("li:eq(0)").find("a").trigger("click");
//    $("#attendClassName").find("option[classid='" + classId + "']").attr("selected", "selected");
//    getClassDates($("#attendClassName"));
}

function viewStudentClass(element) {
    var classId = element.closest("td").attr("classid");
    $("#studentSubmenu").find("li:eq(1)").find("a").trigger("click");
    $("#stuClassName").find("option[classid='" + classId + "']").attr("selected", "selected");
    $("#stuClassName").trigger("change");
//    getClassDates($("#stuClassName"));
}

function getClassDetails(element, flag) {
    $("#classDet tbody").html("");
    var classId = element.closest("td").attr("classid");
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: {
            userId: userId,
            classId: classId,
            callType: "getClassInfo"
        },
        success: function (data) {
            console.log(data);
            if (data !== "[]") {
                var responseData = JSON.parse(data);
                var count = 1;
                if (flag === "Edit") {
                    $("#classDet thead tr").append("<td></td>");
                }
                for (var i = 0; i < responseData.length; i++) {
//                    var term = responseData[i].CALSSTERM;
//                    if (typeof term === "undefined") {
//                        term = "";
//                    }
                    var dataHTML = "<tr>"
                            + "<td hidden>" + count + "</td>"
//                            + "<td>" + term + "</td>"
//                            + "<td>" + responseData[i].CLASSID + "</td>"
                            + "<td>" + responseData[i].CLASSNAME + "</td>"
                            + "<td>" + responseData[i].CLASSDAY + "</td>"
                            + "<td>" + responseData[i].CLASSDATE + "</td>";
                    if (flag === "Edit") {
                        dataHTML = dataHTML + "<td><i class='fa fa-trash' style='color:red;' title='Delete' onclick='removeClass(\"classDet\",$(this));'></i></td>";
                    }
                    dataHTML = dataHTML + +"</tr>";
                    $("#classDet tbody").append(dataHTML);
                    count++;
                }
                $("#myModal").attr('classId', classId);
                $("#myModal").modal('show');
            } else {
                alert("Sorry you don't have any class existing.");
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function getUser() {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: {
            userId: userId,
            callType: "getUser"
        },
        success: function (data) {
            console.log(data);
            if (data != "{}") {
                var responseData = JSON.parse(data);
                $("#uName").text(responseData.USERNAME);
                $("#forgetUserName").val(responseData.USERNAME);
                $("#uDesig").val(responseData.DESIGNATION);
                $("#uPhone").val(responseData.CONTACT);
                $("#sName").val(responseData.SCHOOLNAME);
                $("#uMail").val(responseData.EMAILID);
                $("#userModel").modal('show');
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function removeClassEle(element) {
    if (confirm("Are you sure you want to remove this class?")) {
        debugger;
        var classId = element.closest("td").attr("classid");
//    alert(classId);
        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: {
                userId: userId,
                classId: classId,
                callType: "removeClass"
            },
            success: function (data) {
                console.log(data);
                $("#classSummary tbody").html("");
                getClassData(userId);
            },
            error: function (error) {
                alert("Ajax error---" + error);
            }
        });
    }
}

function getStudentData(userId, classId) {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: {
            userId: userId,
            classId: classId,
            callType: "getStudentInfo"
        },
        success: function (data) {
            console.log(data);
            $("#studentSummary tbody").html("");
            if (data !== "[]") {

                var responseData = JSON.parse(data);
                var count = 1;
                for (var i = 0; i < responseData.length; i++) {
                    var dataHTML = "<tr>"
                            + "<td>" + count + "</td>"
//                            + "<td>" + responseData[i].CLASSNAME + "</td>"
                            + "<td>" + responseData[i].STUDENTID + "</td>"
                            + "<td>" + responseData[i].STUDENTLNAME + " " + responseData[i].STUDENTFNAME + "</td>"
                            + "<td><i class='fas fa-edit' title='Edit' onclick='editStuDet($(this));'></i>&nbsp;&nbsp;&nbsp;<i style='color:red;' class='fa fa-trash' title='Delete' onclick='removeClass(\"studentSummary\",$(this));'></i></td>"
//                            + "<td>" + responseData[i].STUDENTEMAIL + "</td>"
//                            + "<td>" + responseData[i].STUDENTCONTACT + "</td>"
                            + "</tr>";
                    $("#studentSummary tbody").append(dataHTML);
                    count++;
                }
            } else {
                alert("No current class exist.");
            }
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function editStuDet(element) {
    debugger;
    var index = element.closest("tr").index();
    var id = element.closest("tr").find("td:eq(1)").text();
    var name = element.closest("tr").find("td:eq(2)").text();
    $("#stuId").text(id);
    $("#SFName").val(name.split(" ")[1]);
    $("#SLName").val(name.split(" ")[0]);
    $("#stuModel").attr("rowIndex", index);
    $("#stuModel").modal('show');
}

function updateStuDet() {
    var rowIndex = $("#stuModel").attr("rowIndex");
    if ($("#SLName").val().trim() !== "" && $("#SFName").val().trim() !== "") {
        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: {
                userId: userId,
                stuId: $("#stuId").text(),
                stuLName: $("#SLName").val(),
                stuFName: $("#SFName").val(),
                callType: "updateStu"
            },
            success: function (data) {
                console.log(data);
                alert("Record updated successfully.");
                $("#stuModel").modal('hide');
                $("#studentSummary tbody").find("tr:eq(" + rowIndex + ")").find("td:eq(2)").text($("#SLName").val() + " " + $("#SFName").val());
            },
            error: function (error) {
                alert("Ajax error---" + error);
            }
        });
    } else {
        alert("Names can not be empty.");
    }
}

function setTables(result) {
    $("#classMeetTable").show();
    var count = 1;
    var tempStr = "";
    $.each(result, function (key, value) {
        var day = parseInt(key);
        day = day === 0 ? 'Sunday' : day === 1 ? 'Monday' : day === 2 ? 'Tuesday' : day === 3 ? 'Wednesday' : day === 4 ? 'Thursday' : day === 5 ? 'Friday' : 'Saturday';
        for (var j = 0; j < value.length; j++) {
            console.log(value[j]);
            if (tempStr !== value[j]) {
                var dynamicHtml = "<tr id=" + key + "><td>" + count + "</td><td>" + day + "</td><td>" + value[j] + "</td><td><a onclick='removeClass(\"classMeetTable\",$(this));'><i class='fa fa-trash' style='color:red;'></i></a></td></tr>";
                $("#classMeetTable tbody").append(dynamicHtml);
                tempStr = value[j];
                count++;
            }
        }
    });
}

function removeClass(tblName, element) {
    var count = 1;
    if (tblName === "classDet") {
        var classMeet = element.closest("tr").find("td:eq(3)").text();
        var classId = $("#myModal").attr('classId');
        if (confirm("Do you want to remove this class meeting?")) {
            element.closest("tr").remove();
            $("#" + tblName + " tbody tr").each(function () {
                $(this).find("td:eq(0)").text(count);
                count++;
            });
            $.ajax({
                url: "../MarkMeIn/MMIController",
                async: false,
                data: {
                    userId: userId,
                    classMeet: classMeet,
                    classId: classId,
                    callType: "removeClassDet"
                },
                success: function (data) {
                    console.log(data);
                },
                error: function (error) {
                    alert("Ajax error---" + error);
                }
            });
        }
    } else if (tblName === "studentSummary") {
        debugger;
        var stuId = element.closest("tr").find("td:eq(1)").text();
        if (confirm("Do you want to remove this student record?")) {
            debugger;
            element.closest("tr").remove();
            $("#" + tblName + " tbody tr").each(function () {
                $(this).find("td:eq(0)").text(count);
                count++;
            });
            $.ajax({
                url: "../MarkMeIn/MMIController",
                async: false,
                data: {
                    userId: userId,
                    stuId: stuId,
                    callType: "removeStudent"
                },
                success: function (data) {
                    console.log(data);
                },
                error: function (error) {
                    alert("Ajax error---" + error);
                }
            });
        }
    }

}

function showContent(flag) {
    debugger;
    $(".contentDiv").hide();
    $("#" + flag + "Content").removeAttr("hidden");
    $("#" + flag + "Content").attr("style", "display:block;");
    $("input").val("");
    $("select").html("");
    if (flag === "viewClass") {
        $("#classSummary tbody").html("");
        getClassData(userId);
    } else if (flag === "viewStudent") {
        $("#studentSummary tbody").html("");
        getClassList("viewStu", "0");
    } else if (flag === "viewAttendance") {
        getClassList("viewAttend", "0");
    } else if (flag === "takeAttendance") {
        getClassList("attendance", "0");
    } else if (flag === "addStudent") {
        getClassList("Upload", "0");
    } else if (flag === "addClass") {
        $("#termYear").html("");
        $("#termType").html("");
        var date = new Date();
        for (var i = 0; i < 10; i++) {
            let year = date.getFullYear();
            $("#termYear").append("<option>" + year + "</option>");
            date.setFullYear(year + 1);
        }
        $("#classLevel").append("<option>Graduate</option><option>Undergraduate</option>");
        $("#termType").append("<option>Summer</option><option>Spring</option><option>Fall</option>");

        $(function () {
            $('#startDate').prop('min', function () {
                return new Date().toJSON().split('T')[0];
            });
        });
    }
}

function clearSession() {
    $.ajax({
        url: "../MarkMeIn/MMIController",
        async: false,
        data: {
            userId: userId,
            callType: "logOut"
        },
        success: function (data) {
            console.log(data);
            alert("Log Out Success.");
            localStorage.setItem("loginData", "");
            window.location = "../MarkMeIn/Login.jsp";
        },
        error: function (error) {
            alert("Ajax error---" + error);
        }
    });
}

function updateUserDet() {
    if ($("#uDesig").val().trim() !== "" && $("#uPhone").val().trim() !== "" && $("#uMail").val().trim() !== "" && $("#sName").val().trim() !== "") {
        $.ajax({
            url: "../MarkMeIn/MMIController",
            async: false,
            data: {
                userId: userId,
                desig: $("#uDesig").val(),
                phone: $("#uPhone").val(),
                mail: $("#uMail").val(),
                sName: $("#sName").val(),
                callType: "updateUser"
            },
            success: function (data) {
                console.log(data);
                alert("User updated successfully.");
                $("#userModel").modal('hide');
            },
            error: function (error) {
                alert("Ajax error---" + error);
            }
        });
    } else {
        alert("Please fill all details.");
    }
}
