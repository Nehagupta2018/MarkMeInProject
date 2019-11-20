/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.controller;

import com.MarkMeIn.DBSchema.DBQueries;
import com.MarkMeIn.DBSchema.DBUtility;
import com.MarkMeIn.Handler.SendForgotEmail;
import com.MarkMeIn.Modal.ClassDetails;
import com.MarkMeIn.Modal.ClassInfo;
import com.MarkMeIn.Modal.UserInfo;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.internal.OracleTypes;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author hp
 */
@WebServlet(name = "MMIController", urlPatterns = {"/MMIController"})
public class MMIController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        HttpSession session = null;
        try {
            String callFlag = request.getParameter("callType");
            con = DBUtility.getConnection();
            Gson gson = new Gson();
            if (callFlag.equals("Login")) {
                String userName = request.getParameter("userName");
                String userPassword = request.getParameter("userPassword");
                Map<Object, Object> dataMap = new LinkedHashMap<Object, Object>();
                System.out.println("userName---" + userName + "---userPassword---" + userPassword);
                pStmt = con.prepareStatement(DBQueries.CHECKLOGIN);
                pStmt.setString(1, userName);
                pStmt.setString(2, userPassword);
                rSet = pStmt.executeQuery();
                if (rSet.next()) {
                    session = request.getSession(true);
                    String sessionId = session.getId();
                    System.out.println("sessionId---" + sessionId);
                    int userId = rSet.getInt(1);
                    pStmt = con.prepareStatement(DBQueries.UPDATESESSION);
                    pStmt.setString(1, sessionId);
                    pStmt.setInt(2, userId);
                    pStmt.executeUpdate();
//                    pStmt.close();

                    pStmt = con.prepareStatement(DBQueries.GETLOGININFO);
                    pStmt.setInt(1, userId);
                    rSet = pStmt.executeQuery();
                    if (rSet.next()) {
                        ResultSetMetaData rsmd = rSet.getMetaData();
                        int cols = rsmd.getColumnCount();
                        for (int i = 1; i <= cols; i++) {
                            String colName = rsmd.getColumnName(i);
                            System.out.println(colName + "  value is   " + rSet.getString(i));
                            dataMap.put(colName, rSet.getString(i));
                        }
                        rsmd = null;
                    }

                    dataMap.put("UserId", userId);
                    dataMap.put("Login", "Success");
                } else {
                    dataMap.put("Login", "Failure");
                }
                pStmt.close();
                rSet.close();
                System.out.println("return data---" + gson.toJson(dataMap));
                out.write(gson.toJson(dataMap));
            } else if (callFlag.equals("getUser")) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                Map<String, String> dataMap = new LinkedHashMap<String, String>();
                pStmt = con.prepareStatement(DBQueries.GETUSERDETAIL);
                pStmt.setInt(1, userId);
                rSet = pStmt.executeQuery();
                if (rSet.next()) {
                    ResultSetMetaData rsmd = rSet.getMetaData();
                    int cols = rsmd.getColumnCount();
                    for (int i = 1; i <= cols; i++) {
                        String colName = rsmd.getColumnName(i);
                        System.out.println(colName + "  value is   " + rSet.getString(i));
                        dataMap.put(colName, rSet.getString(i));
                    }
                    rsmd = null;
                }
                out.write(gson.toJson(dataMap));
            } else if (callFlag.equals("getClassInfo")) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int classId = Integer.parseInt(request.getParameter("classId"));
                System.out.println("userId---" + userId);
                List<Object> dataList = new ArrayList<Object>();
//                pStmt = con.prepareStatement(DBQueries.GETCLASSLIST);
                pStmt = con.prepareStatement(DBQueries.GETCLASSINFO);
                pStmt.setInt(1, userId);
                pStmt.setInt(2, classId);
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    Map<Object, Object> classMap = new LinkedHashMap<Object, Object>();
                    ResultSetMetaData meta = rSet.getMetaData();
                    for (int j = 1; j <= meta.getColumnCount(); j++) {
                        if (meta.getColumnName(j).equalsIgnoreCase("CLASSID")) {
                            classMap.put(meta.getColumnName(j), rSet.getInt(j));
                        } else {
                            classMap.put(meta.getColumnName(j), rSet.getString(j));
                        }
                    }
                    dataList.add(classMap);
                }
                out.write(new Gson().toJson(dataList));

            } else if (callFlag.equals("getStudentInfo")) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int classId = Integer.parseInt(request.getParameter("classId"));
                System.out.println("userId---" + userId);
                List<Object> dataList = new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETSTUDENTINFO);
                pStmt.setInt(1, userId);
                pStmt.setInt(2, classId);
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    Map<Object, Object> classMap = new LinkedHashMap<Object, Object>();
                    ResultSetMetaData meta = rSet.getMetaData();
                    for (int j = 1; j <= meta.getColumnCount(); j++) {
                        classMap.put(meta.getColumnName(j), rSet.getString(j));
                    }
                    dataList.add(classMap);
                }
                out.write(new Gson().toJson(dataList));

            } else if (callFlag.equals("insertStudentInfo")) {
                String dataList = request.getParameter("dataList");
                JSONArray jArray = new JSONArray(dataList);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = jArray.getJSONObject(i);
                    pStmt = con.prepareStatement(DBQueries.INSERTSTUDENTINFO);
                    pStmt.setString(1, String.valueOf(jObj.get("className")));
                    pStmt.setInt(2, Integer.parseInt(String.valueOf(jObj.get("userId"))));
                    pStmt.setInt(3, Integer.parseInt(String.valueOf(jObj.get("classId"))));
                    pStmt.setInt(4, Integer.parseInt(String.valueOf(jObj.get("seq"))));
                    pStmt.setString(5, String.valueOf(jObj.get("studentRoll")));
                    pStmt.setString(6, String.valueOf(jObj.get("studentLname")));
                    pStmt.setString(7, String.valueOf(jObj.get("studentFname")));
//                    pStmt.setString(8, String.valueOf(jObj.get("studentEmail")));
//                    pStmt.setString(9, String.valueOf(jObj.get("studentContact")));
                    pStmt.executeUpdate();
                    pStmt.close();
                }
                System.out.println("dataList---" + dataList);
                out.write("Success");
            } else if (callFlag.equals("checkClassExist")) {
                String className = request.getParameter("className");
                String flag = "true";
                pStmt = con.prepareStatement(DBQueries.MATCHCLASSNAME);
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    if (className.equalsIgnoreCase(rSet.getString(1))) {
                        flag = "false";
                    }
                }
                if (flag.equals("false")) {
                    out.write("Class Exist");
                } else {
                    out.write("OK");
                }
                pStmt.close();
                rSet.close();
            } else if (callFlag.equals("getClassList")) {
                String userId = request.getParameter("userId");
                List<Object> dataList = new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETCLASSLIST);
                pStmt.setInt(1, Integer.parseInt(userId));
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    Map<Object, Object> classMap = new LinkedHashMap<Object, Object>();
                    ResultSetMetaData meta = rSet.getMetaData();
                    for (int j = 1; j <= meta.getColumnCount(); j++) {
                        if (meta.getColumnName(j).equalsIgnoreCase("CLASSID")) {
                            classMap.put(meta.getColumnName(j), rSet.getInt(j));
                        } else {
                            classMap.put(meta.getColumnName(j), rSet.getString(j));
                        }
                    }
//                    String presentRate = "0.00";
                    List<String> presentRate = getPresentRate(Integer.parseInt(userId), rSet.getInt("CLASSID"));
                    classMap.put("presentRate", presentRate.get(0));
                    classMap.put("stuCount", presentRate.get(1));
                    dataList.add(classMap);
                }
                System.out.println(new Gson().toJson(dataList));
                out.write(new Gson().toJson(dataList));
            } else if (callFlag.equals("getClassMeets")) {
                String userId = request.getParameter("userId");
                String classId = request.getParameter("classId");
                List<Object> dataList = new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETCLASSMEETS);
                pStmt.setInt(1, Integer.parseInt(classId));
                pStmt.setInt(2, Integer.parseInt(userId));
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    Map<String, String> dataMap = new LinkedHashMap<String, String>();
                    String classMeetDay = rSet.getString(1);
                    PreparedStatement pStmt1 = con.prepareStatement(DBQueries.CHECKATTENDANCERECORD);
                    pStmt1.setInt(1, Integer.parseInt(userId));
                    pStmt1.setInt(2, Integer.parseInt(classId));
                    pStmt1.setString(3, classMeetDay);
                    ResultSet rSet1 = pStmt1.executeQuery();
                    String recordFound = "N";
                    if (rSet1.next()) {
                        recordFound = "Y";
                    }
                    dataMap.put("class", classMeetDay);
                    dataMap.put("record", recordFound);
                    dataList.add(dataMap);
                }
                out.write(new Gson().toJson(dataList));
            } else if (callFlag.equals("removeClass")) {
                String userId = request.getParameter("userId");
                String classId = request.getParameter("classId");

                pStmt = con.prepareStatement(DBQueries.REMOVECLASS);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.executeUpdate();
                pStmt.close();
                pStmt = con.prepareStatement(DBQueries.REMOVECLASSDET);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.executeUpdate();
                pStmt.close();
                pStmt = con.prepareStatement(DBQueries.REMOVECLASSSTU);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.executeUpdate();
                pStmt.close();
                pStmt = con.prepareStatement(DBQueries.REMOVECLASSATD);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.executeUpdate();
                pStmt.close();
                out.write("Success");
            } else if (callFlag.equals("removeStudent")) {
                String userId = request.getParameter("userId");
                String stuId = request.getParameter("stuId");

                pStmt = con.prepareStatement(DBQueries.REMOVESTUDENT);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setString(2, stuId);
                pStmt.executeUpdate();
                pStmt.close();
                pStmt = con.prepareStatement(DBQueries.REMOVEATTENDANCE);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setString(2, stuId);
                pStmt.executeUpdate();
                pStmt.close();

                out.write("Success");
            } else if (callFlag.equals("updateUser")) {
                String userId = request.getParameter("userId");
                String desig = request.getParameter("desig");
                String phone = request.getParameter("phone");
                String mail = request.getParameter("mail");
                String sName = request.getParameter("sName");

                pStmt = con.prepareStatement(DBQueries.UPDATEUSERDET);
                pStmt.setString(1, desig);
                pStmt.setString(2, mail);
                pStmt.setString(3, phone);
                pStmt.setString(4, sName);
                pStmt.setInt(5, Integer.parseInt(userId));
                pStmt.executeUpdate();
                pStmt.close();
                out.write("Success");
            } else if (callFlag.equals("removeClassDet")) {
                String userId = request.getParameter("userId");
                String classId = request.getParameter("classId");
                String classMeet = request.getParameter("classMeet");

                pStmt = con.prepareStatement(DBQueries.REMOVECLASSDATE);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.setString(3, classMeet);
                pStmt.executeUpdate();
                pStmt.close();
                pStmt = con.prepareStatement(DBQueries.REMOVECLASSMEETATD);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.setString(3, classMeet);
                pStmt.executeUpdate();
                pStmt.close();

                out.write("Success");
            } else if (callFlag.equals("getTermList")) {
                String userId = request.getParameter("userId");
                List<Object> dataList = new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETCLASSTERM);
                pStmt.setInt(1, Integer.parseInt(userId));
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    dataList.add(rSet.getString(1));
                }
                rSet.close();
                pStmt.close();
                out.write(new Gson().toJson(dataList));
            } else if (callFlag.equals("getClassStuList")) {
                String userId = request.getParameter("userId");
                String classId = request.getParameter("classId");
                List<Object> dataList = new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETSTUCLASSLIST);
                pStmt.setInt(1, Integer.parseInt(classId));
                pStmt.setInt(2, Integer.parseInt(userId));
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    Map<Object, Object> classMap = new LinkedHashMap<Object, Object>();
                    classMap.put(rSet.getString(1), rSet.getString(2) + " " + rSet.getString(3));
                    dataList.add(classMap);
                }
                rSet.close();
                pStmt.close();
                out.write(new Gson().toJson(dataList));
            } else if (callFlag.equals("matchStudentMap")) {
                String studentId = request.getParameter("studentId");
                String classId = request.getParameter("classId");
                pStmt = con.prepareStatement(DBQueries.MATCHSTUDENTCLASS);
                pStmt.setInt(1, Integer.parseInt(classId));
                pStmt.setString(2, studentId);
                rSet = pStmt.executeQuery();
                if (rSet.next()) {
                    out.write("Match Found");
                }
                rSet.close();
                pStmt.close();
            } else if (callFlag.equals("getAttendanceData")) {
                String userId = request.getParameter("userId");
                String classId = request.getParameter("classId");
                String classDate = request.getParameter("classDate");
                List<Object> dataList=new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETATTENDANCEDATA);
                pStmt.setInt(1, Integer.parseInt(userId));
                pStmt.setInt(2, Integer.parseInt(classId));
                pStmt.setString(3, classDate);
                rSet = pStmt.executeQuery();
                while(rSet.next()){
                    Map<String,String> dataMap= new LinkedHashMap<String,String>();
                    dataMap.put("studentID",rSet.getString(1));
                    dataMap.put("pAb",rSet.getString(2));
                    dataMap.put("latein",rSet.getString(3));
                    dataMap.put("notes",rSet.getString(4));
                    dataList.add(dataMap);
                }
                rSet.close();
                pStmt.close();
                
                System.out.println("new Gson().toJson(dataList)---"+new Gson().toJson(dataList));
                out.write(new Gson().toJson(dataList));
            } else if (callFlag.equals("insertClassInfo")) {
                String dataMap = request.getParameter("dataMap");
                ClassDetails classObj = gson.fromJson(dataMap, ClassDetails.class);
                System.out.println("classObj---" + gson.toJson(classObj));
                int classId = 0;

                OraclePreparedStatement OpStmt = (OraclePreparedStatement) con.prepareStatement(DBQueries.INSERTCLASSINFO);
                OpStmt.setInt(1, Integer.parseInt(classObj.getUserId()));
                OpStmt.setString(2, classObj.getClassName());
                OpStmt.setString(3, classObj.getClassLevel());
                OpStmt.setString(4, classObj.getCreditHours());
                OpStmt.setString(5, classObj.getStartDate());
                OpStmt.setString(6, classObj.getEndDate());
                OpStmt.setString(7, classObj.getClassMeetings());
                OpStmt.setString(8, classObj.getClassTerm());
                OpStmt.registerReturnParameter(9, OracleTypes.INTEGER);
                OpStmt.executeUpdate();
                rSet = OpStmt.getReturnResultSet();
                if (rSet.next()) {
                    classId = rSet.getInt(1);
                }
                rSet.close();
                OpStmt.close();

                List<ClassInfo> classInfoList = classObj.getClassDateObj();
                for (int i = 0; i < classInfoList.size(); i++) {
                    pStmt = con.prepareStatement(DBQueries.INSERTCLASSDETAILS);
                    pStmt.setInt(1, Integer.parseInt(classObj.getUserId()));
                    pStmt.setInt(2, classId);
                    pStmt.setInt(3, Integer.parseInt(classInfoList.get(i).getSeq()));
                    pStmt.setString(4, classInfoList.get(i).getDateDay());
                    pStmt.setString(5, classInfoList.get(i).getDateStr());
                    pStmt.setString(6, classInfoList.get(i).getDateId());
                    pStmt.executeUpdate();
                    pStmt.close();
                }
                out.write("Success");

            } else if (callFlag.equals("updatePassword")) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String userName = request.getParameter("userName");
                String password = request.getParameter("password");

                pStmt = con.prepareStatement(DBQueries.UPDATEPASSWORD);
                pStmt.setString(1, password);
                pStmt.setString(2, userName);
                pStmt.setInt(3, userId);
                int result = pStmt.executeUpdate();
                pStmt.close();
                if (result == 1) {
                    out.write("Success");
                } else {
                    out.write("Failure");
                }
            } else if (callFlag.equals("logOut")) {
                String userId = request.getParameter("userId");

                pStmt = con.prepareStatement(DBQueries.USERLOGOUT);
                pStmt.setString(1, "");
                pStmt.setString(2, "");
                pStmt.setInt(3, Integer.parseInt(userId));
                pStmt.executeUpdate();
                pStmt.close();
                out.write("Success");
            } else if (callFlag.equals("sendPasswordMail")) {
                String userName = request.getParameter("userName");
                int userId = 0;
                String userMail = "";
                pStmt = con.prepareStatement(DBQueries.MATCHUSEREMAIL);
                pStmt.setString(1, userName);
                rSet = pStmt.executeQuery();
                if (rSet.next()) {
                    userId = rSet.getInt(1);
                    userMail = rSet.getString(2);
                    new SendForgotEmail().sendMailPassword(userName, userId, userMail);
                    out.write("Success");
                } else {
                    out.write("UserName and Email mismatch.");
                }
                rSet.close();
                pStmt.close();
            } else if (callFlag.equals("insertUser")) {
                String insertData = request.getParameter("data");
                UserInfo userData = gson.fromJson(insertData, UserInfo.class);
                System.out.println("insertData---"+insertData);
                pStmt = con.prepareStatement(DBQueries.MATCHUSER);
                pStmt.setString(1, userData.geteMail());
                pStmt.setString(2, userData.getUserName().toUpperCase());
                rSet = pStmt.executeQuery();
                if (rSet.next()) {
                    System.out.println("Email matched");
                    out.write("User Exists!");
                } else {
                    System.out.println("Not Found");
                    pStmt = con.prepareStatement(DBQueries.INSERTUSER);
                    pStmt.setString(1, userData.getUserName());
                    pStmt.setString(2, userData.getDesignation());
                    pStmt.setString(3, userData.geteMail());
                    pStmt.setString(4, userData.getContact());
                    pStmt.setString(5, userData.getSchoolName());
                    pStmt.setString(6, userData.getUserType());
                    pStmt.setString(7, userData.getPassword());
                    pStmt.executeUpdate();
                    pStmt.close();
                    out.write("Success");
                }
                rSet.close();
                pStmt.close();
            } else if (callFlag.equals("insertAttendance")) {
                String insertData = request.getParameter("dataList");
                JSONArray jArray = new JSONArray(insertData);
                for (int i = 0; i < jArray.length(); i++) {
                    boolean flag = true;
                    JSONObject jObj = jArray.getJSONObject(i);

                    pStmt = con.prepareStatement(DBQueries.CHECKATTENDEXIST);
                    pStmt.setInt(1, Integer.parseInt(String.valueOf(jObj.get("userId"))));
                    pStmt.setInt(2, Integer.parseInt(String.valueOf(jObj.get("classId"))));
                    pStmt.setString(3, String.valueOf(jObj.get("studentId")));
                    pStmt.setString(4, String.valueOf(jObj.get("classMeetDay")));
                    rSet = pStmt.executeQuery();
                    if (rSet.next()) {
                        flag = false;
                    }
                    pStmt.close();
                    rSet.close();

                    if (flag) {
                        pStmt = con.prepareStatement(DBQueries.INSERTATTENDANCE);
                        pStmt.setInt(1, Integer.parseInt(String.valueOf(jObj.get("userId"))));
                        pStmt.setInt(2, Integer.parseInt(String.valueOf(jObj.get("classId"))));
                        pStmt.setString(3, String.valueOf(jObj.get("sequence")));
                        pStmt.setString(4, String.valueOf(jObj.get("className")));
                        pStmt.setString(5, String.valueOf(jObj.get("classMeetDay")));
                        pStmt.setString(6, String.valueOf(jObj.get("studentName")));
                        pStmt.setString(7, String.valueOf(jObj.get("studentId")));
                        pStmt.setString(8, String.valueOf(jObj.get("presentAbsent")));
                        pStmt.setString(9, String.valueOf(jObj.get("lateEntry")));
                        pStmt.setString(10, String.valueOf(jObj.get("notes")));

                        pStmt.executeUpdate();
                        pStmt.close();
                    }
                }
                System.out.println("insertData---" + insertData);
                out.write("Success");
            } else if (callFlag.equals("getTotalAttendance")) {
                String classId = request.getParameter("classId");
                String userId = request.getParameter("userId");
                Map<Object, Object> finalMap = new LinkedHashMap<Object, Object>();

                List<Object> classMeetList = new ArrayList<Object>();
                pStmt = con.prepareStatement(DBQueries.GETCLASSMEETS);
                pStmt.setInt(1, Integer.parseInt(classId));
                pStmt.setInt(2, Integer.parseInt(userId));
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    classMeetList.add(rSet.getString(1));
                }
                rSet.close();
                pStmt.close();

                finalMap.put("classMeetList", classMeetList);

                Map<Object, Object> classStuMap = new LinkedHashMap<Object, Object>();
                pStmt = con.prepareStatement(DBQueries.GETSTUCLASSLIST);
//                pStmt = con.prepareStatement(DBQueries.GETSTUCLASSLIST + " order by CLASSDATE");
                pStmt.setInt(1, Integer.parseInt(classId));
                pStmt.setInt(2, Integer.parseInt(userId));
                rSet = pStmt.executeQuery();
                while (rSet.next()) {
                    List<Object> studentList = new ArrayList<Object>();
                    String stuName = rSet.getString(2) + " " + rSet.getString(3);
                    PreparedStatement pstmt = con.prepareStatement(DBQueries.GETATTENDANCE);
                    pstmt.setInt(1, Integer.parseInt(classId));
                    pstmt.setInt(2, Integer.parseInt(userId));
                    pstmt.setString(3, stuName);
                    ResultSet rset = pstmt.executeQuery();
                    while (rset.next()) {
                        Map<String, String> innerMap = new LinkedHashMap<String, String>();
                        innerMap.put("classMeet", rset.getString(1));
                        innerMap.put("presenAbsent", rset.getString(2));
                        String late = rset.getString(3);
                        if (rset.wasNull()) {
                            late = "";
                        } else {
                            late = "Yes";
                        }
                        innerMap.put("lateIn", late);
                        System.out.println("rset.getString(4)--"+rset.getString(4));
                        innerMap.put("notes", rset.getString(4));
                        studentList.add(innerMap);
                    }
                    rset.close();
                    pstmt.close();
                    classStuMap.put(stuName, studentList);
                }
                rSet.close();
                pStmt.close();

                finalMap.put("classStuList", classStuMap);
                System.out.println(new Gson().toJson(finalMap));
                out.write(new Gson().toJson(finalMap));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getPresentRate(int userId, int classId) throws SQLException {
        double pRate = 0.0;
        int attendCount = 0;
        int stuCount = 0;
        int meetCount = 0;
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        List<String> dataList = new ArrayList<String>();
        DecimalFormat decFormat = new DecimalFormat("##.00");
        try {
            con = DBUtility.getConnection();
            pStmt = con.prepareStatement(DBQueries.GETSTUCOUNT);
            pStmt.setInt(1, userId);
            pStmt.setInt(2, classId);
            rSet = pStmt.executeQuery();
            if (rSet.next()) {
                stuCount = rSet.getInt("COUNT");
            }
            rSet.close();
            pStmt.close();
            System.out.println(stuCount);

            int count = 0;
            double stuRatio = 0.0;
            pStmt = con.prepareStatement(DBQueries.GETMEETCOUNT);
            pStmt.setInt(1, userId);
            pStmt.setInt(2, classId);
            rSet = pStmt.executeQuery();
            while (rSet.next()) {
                attendCount = 0;
                meetCount = rSet.getInt("COUNT");
                count = count + meetCount;
                String classMeetDay = rSet.getString("CLASSDATE");
                PreparedStatement pStmt1 = con.prepareStatement(DBQueries.GETSUMOFSTUCLASS);
                pStmt1.setInt(1, userId);
                pStmt1.setInt(2, classId);
                pStmt1.setString(3, "P");
                pStmt1.setString(4, classMeetDay);
                ResultSet rSet1 = pStmt1.executeQuery();
                if (rSet1.next()) {
                    attendCount = rSet1.getInt("COUNT");
                }
                rSet1.close();
                pStmt1.close();
                stuRatio = stuRatio + ((double) attendCount / stuCount);
            }
            rSet.close();
            pStmt.close();

//            System.out.println(attendCount + "----" + stuCount + "---" + meetCount);
            pRate = (((double) stuRatio / meetCount) * 100);
            System.out.println(pRate);
            dataList.add(decFormat.format(pRate));
            dataList.add(String.valueOf(stuCount));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            con.close();
        }
        return dataList;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
