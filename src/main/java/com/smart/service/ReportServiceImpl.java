package com.smart.service;

import com.smart.dao.ContactRepository;
import com.smart.entities.Contact;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Contact> contacts=this.contactRepository.findAll();
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet("Contact Information");
        HSSFRow row=sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Name");
        row.createCell(2).setCellValue("Nick Name");
        row.createCell(3).setCellValue("Work");
        row.createCell(4).setCellValue("Email");
        row.createCell(5).setCellValue("Phone No.");
        row.createCell(6).setCellValue("Image");
        row.createCell(7).setCellValue("Description");

        int rowIndex=1;

        for (Contact contact:contacts){
            HSSFRow datarow = sheet.createRow(rowIndex);
            datarow.createCell(0).setCellValue(contact.getcId());
            datarow.createCell(1).setCellValue(contact.getName());
            datarow.createCell(2).setCellValue(contact.getSecondName());
            datarow.createCell(3).setCellValue(contact.getWork());
            datarow.createCell(4).setCellValue(contact.getEmail());
            datarow.createCell(5).setCellValue(contact.getPhone());
            datarow.createCell(6).setCellValue(contact.getImage());
            datarow.createCell(7).setCellValue(contact.getDescription());
            rowIndex++;

        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
