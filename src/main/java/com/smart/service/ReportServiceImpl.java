package com.smart.service;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.apache.poi.hssf.usermodel.*;
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


import org.apache.poi.hssf.util.*;
import org.apache.poi.util.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URL;
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

        //
        Image image2 = null;
        String percentcount = "|0|10|20|30|40|50|";
        String gradegraph = "|A1|A2|B1|B2|C1|C2|D|E1|E2|";
        String chartimagepath="";
        try {
            chartimagepath = "https://chart.googleapis.com/chart?cht=bvg&chxl=0:" + gradegraph + "|1:" + percentcount +"&chd=t:"+"20,10,15,17,45,5,8,48,9" +"&chs=900x250&chtt=&chco=151515,8c8c8c,A9A9A9&chxt=x,y&chbh=26,10,26&chm=N,000000,0,-1,10|N,000000,1,-1,10|N,000000,2,-1,10&chdl=Highest Marks|Average Marks|Marks Obtained&chxs=0,000000,9.5,0,l,000000|1,000000,9.5,0,lt,676767&chds=0,50";
            chartimagepath = googleChartURL(chartimagepath);

//            image2 = Image.getInstance(chartimagepath);
        } catch (Exception e) {
            System.err.println("error[" + e.getMessage());
        }

        //

        /*String imagee = "E:\\11th\\IMG_2431.JPG";
        InputStream my_banner_image = new FileInputStream(imagee);
        byte[] bytes = IOUtils.toByteArray(my_banner_image);
        int my_picture_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        my_banner_image.close();
        HSSFPatriarch drawing = sheet.createDrawingPatriarch();
        ClientAnchor my_anchor = new HSSFClientAnchor();
        my_anchor.setCol1(2);
        my_anchor.setRow1(1);
        HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
        my_picture.resize(0.02);*/

//         Replace with the actual URL of the image
//        String imageUrl = "https://resources.edunexttechnologies.com/html-team/common-images/cbse-logo-blue.png";

        // Fetch the image from the URL
        URL url = new URL(chartimagepath);

//        InputStream my_banner_image = new FileInputStream(imagee);
            byte[] bytes = IOUtils.toByteArray(url.openStream());
            int my_picture_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
//                fis.close();

            HSSFPatriarch drawing = sheet.createDrawingPatriarch();
            ClientAnchor my_anchor = new HSSFClientAnchor();
            my_anchor.setCol1(1);
            my_anchor.setRow1(20);
            HSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);
            my_picture.resize(0.4);


        Row header_name_rowno = sheet.createRow(1);
        int columnSeq = 0;
        int rowno=5;

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
//        row.setRowStyle(headerStyle);
    }

    public static String googleChartURL(String chart) {
        chart = chart.replaceAll(" ", "");
        chart = chart.replaceAll("\\|", "%7C");
        chart = chart.replaceAll(" ", "");
        return chart;
    }





}


