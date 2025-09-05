//package ai.sigmasoft.emsportal.batch;
//
//import java.io.InputStream;
//import java.time.LocalDateTime;
//import java.util.Iterator;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.context.annotation.Bean;
//
//import ai.sigmasoft.emsportal.entities.PayrollStagingEntity;
//
//public class ExcelItemReader implements ItemReader<PayrollStagingEntity> {
//
//    private final Iterator<Row> rowIterator;
//
//    public ExcelItemReader(InputStream inputStream) throws Exception {
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//        this.rowIterator = sheet.iterator();
//        if (rowIterator.hasNext()) {
//            rowIterator.next(); // Skip header row
//        }
//    }
//
//	@Override
//	public PayrollStagingEntity read() {
//		if (rowIterator != null && rowIterator.hasNext()) {
//			Row row = rowIterator.next();
//			PayrollStagingEntity entity = new PayrollStagingEntity();
//			entity.setEmployeeCode(row.getCell(0).getStringCellValue());
//			entity.setFullName(row.getCell(1).getStringCellValue());
//			entity.setMonthYear(row.getCell(2).getStringCellValue());
//			entity.setWorkingDays(row.getCell(3).getNumericCellValue());
//			entity.setLop(row.getCell(4).getNumericCellValue());
//			entity.setBasicSalary(row.getCell(5).getNumericCellValue());
//			entity.setHra(row.getCell(6).getNumericCellValue());
//			entity.setMedicalAllowance(row.getCell(7).getNumericCellValue());
//			entity.setSpecialAllowance(row.getCell(8).getNumericCellValue());
//			entity.setOtherAllowance(row.getCell(9).getNumericCellValue());
//			entity.setEmployeePf(row.getCell(10).getNumericCellValue());
//			entity.setEmployerPf(row.getCell(11).getNumericCellValue());
//			entity.setPt(row.getCell(12).getNumericCellValue());
//			entity.setInsurance(row.getCell(13).getNumericCellValue());
//			entity.setVariablePayReserve(row.getCell(14).getNumericCellValue());
//			entity.setVariablePayRelease(row.getCell(15).getNumericCellValue());
//			entity.setCreatedDate(LocalDateTime.now());
//			entity.setCreatedBy("System");
//			entity.setStatus("100");
//			// Map other columns
//			entity.setGrossSalary(row.getCell(16).getNumericCellValue());
//
//			return entity;
//		} else {
//			return null; // No more rows
//		}
//	}
//}



