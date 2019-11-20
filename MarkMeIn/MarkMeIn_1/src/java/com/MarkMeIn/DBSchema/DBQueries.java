/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.DBSchema;

/**
 *
 * @author hp
 */
public class DBQueries {

    public static String CHECKLOGIN = "select userId from MMI_USERINFO where userName=? and userPassword=?";

    public static String UPDATESESSION = "update MMI_USERINFO set sessionid=?,sessiontime=sysdate where userId=?";

    public static String GETLOGININFO = "select USERNAME,DESIGNATION,SESSIONID,EMAILID,CONTACT,SCHOOLNAME,USERTYPE from MMI_USERINFO where userId=?";

    public static String MATCHUSEREMAIL = "select userId,emailId from MMI_USERINFO where username=?";

    public static String MATCHUSER = "select userId from MMI_USERINFO where emailid=? or UPPER(username)=?";

    public static String UPDATEPASSWORD = "update MMI_USERINFO set userPassword=? where USERNAME=? and userId=?";

    public static String INSERTUSER = "insert into MMI_USERINFO(USERNAME,DESIGNATION,EMAILID,CONTACT,SCHOOLNAME,USERTYPE,USERPASSWORD) values(?,?,?,?,?,?,?)";

    public static String USERLOGOUT = "update MMI_USERINFO set sessionid=?,sessiontime=? where userid=?";

    public static String MATCHCLASSNAME = "select classname from MMI_CLASSINFO";

    public static String MATCHSTUDENTCLASS = "select * from MMI_STUDENTINFO where classid=? and studentid=?";

    public static String GETCLASSLIST = "select classid,classname,creditHours,startDate,endDate from MMI_CLASSINFO where userid=? order by to_date(startdate, 'YYYY-MM-DD') asc";

    public static String INSERTCLASSINFO = "insert into MMI_CLASSINFO(userid,classname,classlevel,creditHours,startDate,endDate,classMeets,classterm) values(?,?,?,?,?,?,?,?) RETURNING classid into ?";

    public static String INSERTCLASSDETAILS = "insert into MMI_CLASSDETAILS(userid,classid,seq,classday,classdate,dateid) values(?,?,?,?,?,?)";

    public static String INSERTSTUDENTINFO = "insert into MMI_STUDENTINFO(classname,userid,classid,seq,studentid,studentlname,studentfname) values(?,?,?,?,?,?,?)";
//    public static String INSERTSTUDENTINFO = "insert into MMI_STUDENTINFO(classname,userid,classid,seq,studentid,studentfname,studentlname,studentemail,studentcontact) values(?,?,?,?,?,?,?,?,?)";

    public static String GETCLASSINFO = "select A.classid as CLASSID,A.ClassName as CLASSNAME,B.classday as CLASSDAY,B.CLASSDATE as CLASSDATE,A.classterm as CLASSTERM from MMI_CLASSINFO A,MMI_CLASSDETAILS B where A.classid=B.classid and A.userid=? and A.classid=? order by to_date(B.CLASSDATE, 'MON DD,YYYY' )";

    public static String GETSTUDENTINFO = "select classname,studentid,studentfname,studentlname from MMI_STUDENTINFO where userid=? and classid=?";
//    public static String GETSTUDENTINFO = "select classname,studentid,studentfname,studentlname,studentemail,studentcontact from MMI_STUDENTINFO where userid=? order by classid";

    public static String GETCLASSMEETS = "select CLASSDATE from MMI_CLASSDETAILS where classid=? and userid=? order by to_date(CLASSDATE, 'MON DD,YYYY' )";

    public static String GETSTUCLASSLIST = "select studentid,studentfname,studentlname from MMI_STUDENTINFO where classid=? and userId=?";

    public static String INSERTATTENDANCE = "insert into MMI_ATTENDANCE(userid,classid,seq,classname,classmeetdate,studentname,studentid,presentOrabsent,latein,notes) values(?,?,?,?,?,?,?,?,?,?)";

    public static String CHECKATTENDEXIST = "select classmeetdate from MMI_ATTENDANCE where userid=? and classid=? and studentid=? and classmeetdate=?";

    public static String GETATTENDANCE = "select classmeetdate,PRESENTORABSENT,latein,notes from MMI_ATTENDANCE where classid=? and userid=? and studentname=? order by to_date(classmeetdate, 'MON DD,YYYY' )";

    public static String GETCLASSTERM = "select distinct classterm from MMI_CLASSINFO where userid=?";

    public static String REMOVECLASS = "delete from MMI_CLASSINFO where userid=? and classid=?";

    public static String REMOVECLASSDET = "delete from MMI_CLASSDETAILS where userid=? and classid=?";

    public static String REMOVECLASSDATE = "delete from MMI_CLASSDETAILS where userid=? and classid=? and CLASSDATE=?";

    public static String REMOVECLASSSTU = "delete from MMI_STUDENTINFO where userid=? and classid=?";

    public static String REMOVECLASSATD = "delete from MMI_ATTENDANCE where userid=? and classid=?";

    public static String REMOVECLASSMEETATD = "delete from MMI_ATTENDANCE where userid=? and classid=? and CLASSMEETDATE=?";

    public static String REMOVESTUDENT = "delete from MMI_STUDENTINFO where userid=? and studentid=?";

    public static String REMOVEATTENDANCE = "delete from MMI_ATTENDANCE where userid=? and studentid=?";

    public static String GETUSERDETAIL = "select USERNAME,DESIGNATION,EMAILID,CONTACT,SCHOOLNAME from MMI_USERINFO where userid=?";

    public static String UPDATEUSERDET = "update MMI_USERINFO set DESIGNATION=?,EMAILID=?,CONTACT=?,SCHOOLNAME=? where userid=?";

    public static String GETSUMOFSTUCLASS = "select count(*) as COUNT from MMI_ATTENDANCE where USERID=? and CLASSID=? and PRESENTORABSENT=? and CLASSMEETDATE=?";

    public static String GETSTUCOUNT = "select count(*) as COUNT from MMI_STUDENTINFO where USERID=? and CLASSID=?";

    public static String GETMEETCOUNT = "select count(*) as COUNT, CLASSDATE from MMI_CLASSDETAILS where USERID=? and CLASSID=? group by CLASSDATE";
    
    public static String CHECKATTENDANCERECORD = "select * from MMI_ATTENDANCE where userid=? and classid=? and CLASSMEETDATE=?";
    
   public static String GETATTENDANCEDATA = "select studentid,presentorabsent,latein,notes from mmi_attendance where userid=? and classid=? and classmeetdate=?";
}
