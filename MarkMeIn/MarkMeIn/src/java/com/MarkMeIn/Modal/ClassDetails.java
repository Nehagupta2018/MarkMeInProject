/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.Modal;

import java.util.List;

/**
 *
 * @author test
 */
public class ClassDetails {

    private String userId = "";
    private String className = "";
    private String classLevel = "";
    private String creditHours = "";
    private String startDate = "";
    private String endDate = "";
    private String classTerm = "";
    private String classMeetings = "";
    private List<ClassInfo> classDateObj;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(String classLevel) {
        this.classLevel = classLevel;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getClassMeetings() {
        return classMeetings;
    }

    public void setClassMeetings(String classMeetings) {
        this.classMeetings = classMeetings;
    }

    public List<ClassInfo> getClassDateObj() {
        return classDateObj;
    }

    public void setClassDateObj(List<ClassInfo> classDateObj) {
        this.classDateObj = classDateObj;
    }

    public String getClassTerm() {
        return classTerm;
    }

    public void setClassTerm(String classTerm) {
        this.classTerm = classTerm;
    }

}
