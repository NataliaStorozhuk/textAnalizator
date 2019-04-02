package sample.FileConverter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import sample.DTO.BookProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelExporter {

    public static void createExcelFile(List<String> lexems, ArrayList<BookProfile> books, String bookName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet");
        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("words");
        cell.setCellStyle(style);

        for (int i = 0; i < books.size(); i++) {
            cell = row.createCell(i + 1, CellType.STRING);
            cell.setCellValue(books.get(i).getName());
            cell.setCellStyle(style);
        }

        // Data
        for (int i = 0; i < lexems.size(); i++) {

            rownum++;
            row = sheet.createRow(rownum);

            // EmpNo (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(lexems.get(i));

            for (int j = 0; j < books.size(); j++) {
                cell = row.createCell(j + 1, CellType.STRING);
                cell.setCellValue(books.get(j).getTf_idf().get(i));
                cell.setCellStyle(style);
            }
        }
        File file = new File("C:/Users/Natalia/Desktop/" + bookName + ".xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
    }

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
}
