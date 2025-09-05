//package ai.sigmasoft.emsportal.batch
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//
//import ai.sigmasoft.emsportal.entities.PayrollStagingEntity;
//import ai.sigmasoft.emsportal.services.PayrollService;
//
//public static List<PayRoll> excelToPayRoll(InputStream is) {
//    try {
//        Workbook workbook = new XSSFWorkbook(is);
//        Sheet sheet = workbook.getSheet(SHEET);
//        Iterator<Row> rows = sheet.iterator();
//        List<PayrollStagingEntity> payRolls = new ArrayList<>();
//
//        int rowNumber = 0;
//        while (rows.hasNext()) {
//            Row currentRow = rows.next();
//
//            // skip header
//            if (rowNumber == 0) {
//                rowNumber++;
//                continue;
//            }
//
//            Iterator<Cell> cellsInRow = currentRow.iterator();
//            PayRoll payRoll = new PayRoll();
//
//            int cellIdx = 0;
//            while (cellsInRow.hasNext()) {
//                Cell currentCell = cellsInRow.next();
//
//                switch (cellIdx) {
//                    case 0:
//                        payRoll.setEmployeeCode(currentCell.getStringCellValue());
//                        break;
//                    case 1:
//                        payRoll.setFullName(currentCell.getStringCellValue());
//                        break;
//                    case 2:
//                        payRoll.setMonthYear(currentCell.getStringCellValue());
//                        break;
//                    case 3:
//                        payRoll.setWorkingDays(currentCell.getNumericCellValue());
//                        break;
//                    case 4:
//                        payRoll.setLop(currentCell.getNumericCellValue());
//                        break;
//                    case 5:
//                        payRoll.setBasicSalary(currentCell.getNumericCellValue());
//                        break;
//                    case 6:
//                        payRoll.setHra(currentCell.getNumericCellValue());
//                        break;
//                    case 7:
//                        payRoll.setMedicalAllowance(currentCell.getNumericCellValue());
//                        break;
//                    case 8:
//                        payRoll.setSpecialAllowance(currentCell.getNumericCellValue());
//                        break;
//                    case 9:
//                        payRoll.setOtherAllowance(currentCell.getNumericCellValue());
//                        break;
//                    case 10:
//                        payRoll.setEmployerPf(currentCell.getNumericCellValue());
//                        break;
//                    case 11:
//                        payRoll.setEmployeePf(currentCell.getNumericCellValue());
//                        break;
//                    case 12:
//                        payRoll.setPt(currentCell.getNumericCellValue());
//                        break;
//                    case 13:
//                        payRoll.setInsurance(currentCell.getNumericCellValue());
//                        break;
//                    case 14:
//                        payRoll.setVariablePayReserve(currentCell.getNumericCellValue());
//                        break;
//                    case 15:
//                        payRoll.setVariablePayRelease(currentCell.getNumericCellValue());
//                        break;
//                    case 16:
//                        payRoll.setCreatedDate(LocalDateTime.now());
//                        break;
//                    case 17:
//                        payRoll.setCreatedBy("Sys");
//                        break;
//                    case 18:
//                        payRoll.setModifiedDate(LocalDateTime.now());
//                        break;
//                    case 19:
//                        payRoll.setModifiedBy("Sys");
//                        break;
//                    case 20:
//                        payRoll.setStatus(currentCell.getStringCellValue());
//                        break;
//                    case 21:
//                        payRoll.setGrossSalary(currentCell.getNumericCellValue());
//                        break;
//                    case 22:
//                        payRoll.setNetSalary(currentCell.getNumericCellValue());
//                        break;
//                    default:
//                        break;
//                }
//
//                cellIdx++;
//            }
//
//            payRolls.add(payRoll);
//        }
//
//        workbook.close();
//        return payRolls;
//    } catch (IOException e) {
//        throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
//    }
//}



