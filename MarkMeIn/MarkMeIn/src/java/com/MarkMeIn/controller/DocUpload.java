/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.controller;

import au.com.bytecode.opencsv.CSVReader;
import com.MarkMeIn.DBSchema.DBQueries;
import com.MarkMeIn.DBSchema.DBUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author test
 */
@WebServlet(name = "DocUpload", urlPatterns = {"/DocUpload"})
public class DocUpload extends HttpServlet {

    String fileName = "";
    String extension = "";
    String className = "";
    int userId = 0;
    int classId = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");
                List<FileItem> items;
                try {
                    items = upload.parseRequest(request);
                    Iterator<FileItem> it = items.iterator();
                    while (it.hasNext()) {
                        FileItem item = it.next();
                        if (item.isFormField()) {
                            if (item.getFieldName().equalsIgnoreCase("fileName")) {
                                fileName = item.getString();
                            } else if (item.getFieldName().equalsIgnoreCase("extension")) {
                                extension = item.getString();
                            } else if (item.getFieldName().equalsIgnoreCase("classId")) {
                                classId = Integer.parseInt(item.getString());
                            } else if (item.getFieldName().equalsIgnoreCase("className")) {
                                className = item.getString();
                            } else if (item.getFieldName().equalsIgnoreCase("userId")) {
                                userId = Integer.parseInt(item.getString());
                            }
                        } else {
                            String path = System.getProperty("user.dir") + File.separator + "Process" + File.separator + "MMI" + File.separator;
                            System.out.println(path);
                            File storeFile = new File(path);
                            if (!storeFile.exists()) {
                                storeFile.mkdirs();
                            }
                            item.write(new File(path + fileName));
                            String status = "";
                            if (extension.contains("csv")) {
                                status = insertThroughCSV(new File(path + fileName));
                            } else {
                                status = insertThroughExcel(new File(path + fileName));
                            }
                            new File(path + fileName).delete();
                            out.write(status);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String insertThroughCSV(File uploadFile) throws SQLException {
        String status = "";
        Connection con = DBUtility.getConnection();
        PreparedStatement pStmt = null;
        try {
            FileReader filereader = new FileReader(uploadFile);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            int rowCount = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                int count = 1;
                pStmt = con.prepareStatement(DBQueries.INSERTSTUDENTINFO);
                pStmt.setString(1, className);
                pStmt.setInt(2, userId);
                pStmt.setInt(3, classId);
                pStmt.setInt(4, count);
                if (nextRecord.length == 3) {
//                if (nextRecord.length == 6) {
                    int i = 4;
                    if (rowCount > 0) {
                        for (String cell : nextRecord) {
                            if (cell.equals("")) {
                                status = "FAIL";
                                return status;
                            } else {
                                pStmt.setString(i, cell);
                            }
                            i++;
                        }
                        pStmt.executeUpdate();
                        count++;
                    }
                } else {
                    status = "FAIL";
                    return status;
                }
                pStmt.close();
                rowCount++;
            }
            status = "SUCCESS";
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            con.close();
        }
        return status;
    }

    public String insertThroughExcel(File uploadFile) throws SQLException {
        String status = "";
        String fileStatus = "";
        Connection con = DBUtility.getConnection();
        PreparedStatement pStmt = null;
        try {
            String[] fileHeaders = new String[]{"Student Id", "Last Name", "First Name"};
//            String[] fileHeaders = new String[]{"S.No.", "Student Id", "First Name", "Last Name", "Email", "Contact"};

            FileInputStream file = new FileInputStream(uploadFile);
            // Create Workbook instance holding reference to .xlsx file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            // Get first/desired sheet from the workbook 
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() <= 1) {
                status = "EMPTY";
                return status;
            } else {
                Row row = sheet.getRow(0);
                for (int i = 0; i < fileHeaders.length; i++) {
                    String cellVal = row.getCell(i).getStringCellValue();
                    if (!fileHeaders[i].equals(cellVal)) {
                        fileStatus = "Incorrect";
                        status = "FAILURE";
                        return status;
                    } else {
                        fileStatus = "Correct";
                    }
                }

                if (fileStatus.equals("Correct")) {
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row row1 = rowIterator.next();
                        System.out.println(row1.getRowNum());
                        if (row1.getRowNum() > 0) {
                            int count = 1;
                            pStmt = con.prepareStatement(DBQueries.INSERTSTUDENTINFO);
                            pStmt.setString(1, className);
                            pStmt.setInt(2, userId);
                            pStmt.setInt(3, classId);
                            pStmt.setInt(4, count);
// For each row, iterate through all the columns 
                            int i = 5;
                            if (row1.getPhysicalNumberOfCells() == fileHeaders.length) {
                                Iterator<Cell> cellIterator = row1.cellIterator();
                                while (cellIterator.hasNext()) {
                                    Cell cell = cellIterator.next();
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    System.out.println(i + "---" + cell.getStringCellValue());
                                    pStmt.setString(i, cell.getStringCellValue());
                                    i++;
                                }
                            }
                            pStmt.executeUpdate();
                            count++;
                            status = "SUCCESS";
                            pStmt.close();
                        }
//                        } else {
//                            status = "FAILURE";
//                            return status;
//                        }
                    }
                }
            }
//        }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            con.close();
        }
        return status;
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
