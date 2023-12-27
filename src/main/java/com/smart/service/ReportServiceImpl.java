package com.smart.service;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;


    public int addCommonData(@AuthenticationPrincipal Principal principal){
        User user=userRepository.getUserByUserName(principal.getName());
        return user.getId();
    }

    @Override
    public void generateExcel(HttpServletResponse response) throws IOException {
//        List<Contact> contacts = this.contactRepository.findAll();

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        int userId=addCommonData(authentication);

        List<Contact> contacts1 = this.contactRepository.findContactByUser(userId);
        System.out.println("contacts  >"+contacts1);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Contact Information");
        HSSFRow row = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        HSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        font.setBoldweight((short) 700);
        headerStyle.setFont(font);

        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        bodyStyle.setAlignment(CellStyle.ALIGN_LEFT);
        bodyStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        bodyStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        bodyStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        bodyStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);

        font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        bodyStyle.setFont(font);

        Row header_name_rowno = sheet.createRow(1);
        int columnSeq = 0;
        int rowno=1;

        createNewHeaderCell(header_name_rowno, columnSeq++, "ID", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Name", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Nick Name", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Work", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Email", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Phone No.", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Image", headerStyle);
        createNewHeaderCell(header_name_rowno, columnSeq++, "Description", headerStyle);

//        sheet.addMergedRegion(new CellRangeAddress(rowno, rowno,columnSeq, columnSeq));
        /*createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "ID", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowno, rowno,columnSeq, columnSeq));
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Name", headerStyle);
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Nick Name", headerStyle);
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Work", headerStyle);
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Email", headerStyle);
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Phone No.", headerStyle);
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Image", headerStyle);
        createNewRowAndHeaderCell(sheet, rowno, columnSeq++, "Description", headerStyle);*/


        int rowIndex = 2;

        for (Contact contact : contacts1) {
            int cellno=0;
//            if (!(userId == contact.getUser().getId())) {continue;}
            Row rowvalue = sheet.createRow(rowIndex++);
            createNewCell(rowvalue, cellno++, "" + contact.getcId(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getName(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getSecondName(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getWork(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getEmail(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getPhone(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getImage(), sheet, bodyStyle);
            createNewCell(rowvalue, cellno++, "" + contact.getDescription(), sheet, bodyStyle);
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        //        workbook.close();
        outputStream.close();
    }


    public void createNewCell(Row row, int cellno, String cellValue, HSSFSheet sheet, CellStyle holidayStyle) {
        Cell cell = row.createCell(cellno);
        cell.setCellValue(cellValue);
        cell.setCellStyle(holidayStyle);
        sheet.setDefaultColumnWidth(20);
    }

    public void createBigNewCell(Row row, int cellno, String cellValue, XSSFSheet sheet, CellStyle holidayStyle) {
        Cell cell = row.createCell(cellno);
        cell.setCellValue(cellValue);
        cell.setCellStyle(holidayStyle);
        sheet.setDefaultColumnWidth(20);
    }

    public void createNewRowAndHeaderCell(HSSFSheet sheet, int rowno, int columnSeq, String cellValue, CellStyle headerStyle) {
        Row row = sheet.createRow(rowno);
        Cell cell = row.createCell(columnSeq);
        cell.setCellValue(cellValue);
        cell.setCellStyle(headerStyle);
    }

    public void createNewHeaderCell(Row row, int cellno, String cellValue, CellStyle headerStyle) {
        Cell cell = row.createCell(cellno);
        cell.setCellValue(cellValue);
        cell.setCellStyle(headerStyle);
        row.setRowStyle(headerStyle);
    }





}


