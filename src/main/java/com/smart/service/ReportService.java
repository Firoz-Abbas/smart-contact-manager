package com.smart.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public interface ReportService {
    public void generateExcel(HttpServletResponse response) throws IOException;
}
