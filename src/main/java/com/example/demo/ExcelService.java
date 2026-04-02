package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    @Autowired
    private StudentRepo repo;

    @Cacheable("studentExcel")
    public byte[] exportExcel() throws Exception {

        List<Student> students = repo.findAll();
        System.out.println("Generating Excel from DB only");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Data");

        // Header Row
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Phone Number");
        header.createCell(4).setCellValue("Role");
        header.createCell(5).setCellValue("Verified");
        header.createCell(6).setCellValue("OTP Expiry");

        int rowNum = 1;

        for (Student student : students) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(student.getId());

            row.createCell(1).setCellValue(
                student.getName() != null ? student.getName() : ""
            );

            row.createCell(2).setCellValue(
                student.getEmail() != null ? student.getEmail() : ""
            );

            row.createCell(3).setCellValue(
                student.getPhoneNumber() != null ? student.getPhoneNumber() : ""
            );

            row.createCell(4).setCellValue(
                student.getRole() != null ? student.getRole() : ""
            );

            row.createCell(5).setCellValue(
                student.getIsVerified() != null ? student.getIsVerified() : false
            );

            row.createCell(6).setCellValue(
                student.getOtpExpiry() != null ? student.getOtpExpiry().toString() : ""
            );
        }

        // Auto-size columns
        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}