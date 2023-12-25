package com.smart.controller;

import com.smart.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/user/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=contacts.xls";

        response.setHeader(headerKey, headerValue);
        reportService.generateExcel(response);

    }
}
