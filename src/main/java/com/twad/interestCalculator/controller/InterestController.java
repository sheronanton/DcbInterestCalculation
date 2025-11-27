package com.twad.interestCalculator.controller;

// Java IO
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Apache POI for Excel
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.twad.interestCalculator.InterestResponse;
import com.twad.interestCalculator.entity.DemandEntry;
import com.twad.interestCalculator.service.InterestCalculatorService;
import com.twad.interestCalculator.service.InterestCalculatorService.ResultEntry;




@CrossOrigin(origins = "http://localhost:5173") // Allow your React dev server
@RestController
@RequestMapping("/api/excel")
public class InterestController {


    
	@PostMapping("/upload")
	public ResponseEntity<InterestResponse> uploadAndCalculateInterest(
	    @RequestParam("file") MultipartFile file,
	    @RequestParam("mode") String mode // <--- receives toggle value from frontend
	)  {
        List<DemandEntry> demands = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            Row headerRow = rows.next(); // Assuming headers are Sl, Year, Month, Demand
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));

            while (rows.hasNext()) {
                Row row = rows.next();
                int slNo = (int) row.getCell(0).getNumericCellValue();
                int year = (int) row.getCell(1).getNumericCellValue();
                int month = (int) row.getCell(2).getNumericCellValue();
                int demand = (int) row.getCell(3).getNumericCellValue();

                demands.add(new DemandEntry(slNo, year, month, demand));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


        InterestCalculatorService service = new InterestCalculatorService();
        InterestResponse response = service.calculateInterestUrban(demands, mode);
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody InterestResponse interestResponse) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Interest Calculation");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Month", "Year", "Opening Balance", "Demand", "Overdue Amount", "Closing Balance", "Interest"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Add data rows
            int rowNum = 1;
            for (ResultEntry entry : interestResponse.results) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.month);
                row.createCell(1).setCellValue(entry.year);
                row.createCell(2).setCellValue(entry.openingBalance);
                row.createCell(3).setCellValue(entry.demand);
                row.createCell(4).setCellValue(entry.overdueAmount);
                row.createCell(5).setCellValue(entry.closingBalance);
                row.createCell(6).setCellValue(entry.interest);
            }

            // Add totals row
            Row totalsRow = sheet.createRow(rowNum + 1);
            totalsRow.createCell(0).setCellValue("TOTAL");
            totalsRow.createCell(5).setCellValue(interestResponse.totalClosingBalance);
            totalsRow.createCell(6).setCellValue(interestResponse.totalInterest);

            // Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();

            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Content-Disposition", "attachment; filename=interest_calculation.xlsx");
            headers2.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok()
                    .headers(headers2)
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
