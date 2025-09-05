package com.pransquare.nems.batch;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.pransquare.nems.entities.PayrollStagingEntity;
import com.pransquare.nems.exceptions.ExcelReadException;

@Component
public class ExcelHelper {
	

public  List<PayrollStagingEntity> readPayrollFromExcel(InputStream is, String fileId) {
    try {
        Workbook workbook = new XSSFWorkbook(is);
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet("Sheet1");
        Iterator<Row> rows = sheet.iterator();
        List<PayrollStagingEntity> payRolls = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            // skip header
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();
            PayrollStagingEntity payRoll = new PayrollStagingEntity();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();

//                switch (cellIdx) {
//                    case 0:
//                        payRoll.setEmployeeCode(currentCell.getStringCellValue());
//                        break;
                switch (cellIdx) {
                case 0:
                    if (currentCell.getCellType() == CellType.STRING) {
                        payRoll.setEmployeeCode(currentCell.getStringCellValue());
                    } else if (currentCell.getCellType() == CellType.NUMERIC) {
                        payRoll.setEmployeeCode(String.valueOf((long) currentCell.getNumericCellValue()));
                    } else if (currentCell.getCellType() == CellType.FORMULA) {
                        switch (currentCell.getCachedFormulaResultType()) {
                            case STRING:
                                payRoll.setEmployeeCode(currentCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                payRoll.setEmployeeCode(String.valueOf((long) currentCell.getNumericCellValue()));
                                break;
                        }
                    }
                    break;
            
                    case 1:
                        payRoll.setFullName(currentCell.getStringCellValue());
                        break;
                    case 2:
                        payRoll.setMonth(currentCell.getStringCellValue());
                        break;
                    case 3:
                        payRoll.setYear(String.valueOf(currentCell.getNumericCellValue()).substring(0, 4));
                        break;
                    case 4:
                        payRoll.setNoOfDays(currentCell.getNumericCellValue());
                        break;
                    case 5:
                        payRoll.setLop(currentCell.getNumericCellValue());
                        break;
                    case 6:
                        payRoll.setBasicSalary(currentCell.getNumericCellValue());
                        break;
                    case 7:
                        payRoll.setHra(currentCell.getNumericCellValue());
                        break;
                    case 8:
                        payRoll.setMedicalAllowance(currentCell.getNumericCellValue());
                        break;
                    case 9:
                        payRoll.setConveyance(currentCell.getNumericCellValue());
                        break;
                    case 10:
                        payRoll.setSpecialAllowance (currentCell.getNumericCellValue());
                        break;
                    case 11:
                    	payRoll.setVariablePayRelease(currentCell.getNumericCellValue());
                        break;
                    case 12:
                        payRoll.setOtherAllowance(currentCell.getNumericCellValue());
                        break;
                    case 13:
                        payRoll.setEmployeePf(currentCell.getNumericCellValue());
                        break;
                    case 14:
                        payRoll.setPt(currentCell.getNumericCellValue());
                        break;
                    case 15:
                        payRoll.setInsurance(currentCell.getNumericCellValue());
                        break;
                    case 16:
                        payRoll.setGrossSalary(currentCell.getNumericCellValue());
                        break;
                    case 17:
                        payRoll.setDeductions(currentCell.getNumericCellValue());
                        break;
                    case 18:
                        payRoll.setNetSalary(currentCell.getNumericCellValue());
                       
                        break;
                    default:
                    	  
                        break;
                }

                cellIdx++;
            }
            payRoll.setCreatedDate(LocalDateTime.now());
            payRoll.setCreatedBy("Sys");
            payRoll.setStatus("100");
            payRoll.setFileId(fileId);
            payRolls.add(payRoll);
        }

        workbook.close();
        return payRolls;
    } catch (IOException e) {
    	throw new ExcelReadException("Failed to parse Excel file: " + e.getMessage(), e);
    }

}
}
