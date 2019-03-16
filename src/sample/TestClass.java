package sample;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestClass {

    Analyzer analyzer = new Analyzer();
    ArrayList<Book> books = new ArrayList<>();

    @Test
    public void getFiles() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/test");
        listFilesForFolder(folder);
       List<String> arrayAfterSort= analyzer.getResults(books);
        createExcelFile(arrayAfterSort, books);
    }

    public void ChooseFile() {

   /* final JFileChooser fileChooser = new JFileChooser();

    int returnValue = fileChooser.showOpenDialog(null);
        if(returnValue ==JFileChooser.APPROVE_OPTION)

    {
        File file = fileChooser.getSelectedFile();
        listFilesForFolder(file);

    }*/

    }

    public void listFilesForFolder(File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                getBookFromFile(fileEntry);
                System.out.println(fileEntry.getName());
            }
        }
            int p=0;
    }


    public void getBookFromFile(File file) {

        String s = analyzer.getDocFromFileSystem(file);
        ArrayList<String> wordsFromString = analyzer.getWordsFromString(s);

        ArrayList<String> wordsWithoutStop = analyzer.getWordsWithoutStop(wordsFromString);
        ArrayList<String> fileAfterPorter = analyzer.getWordsAfterPorter(wordsWithoutStop);

        Book book = new Book(file.getName(), fileAfterPorter);
        books.add(book);
    }


    public void createExcelFile(List<String> lexems, ArrayList<Book> books) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Employees sheet");
        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("words");
        cell.setCellStyle(style);

        for (int i=0; i<books.size(); i++) {
            cell = row.createCell(i+1, CellType.STRING);
            cell.setCellValue(books.get(i).getName());
            cell.setCellStyle(style);
        }

        // Data
        for (int i=0; i<lexems.size(); i++) {

            rownum++;
            row = sheet.createRow(rownum);

            // EmpNo (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(lexems.get(i));

            for (int j=0; j<books.size(); j++) {
                cell = row.createCell(j+1, CellType.STRING);
                cell.setCellValue(books.get(j).getTf_idf().get(i));
                cell.setCellStyle(style);
            }
        }
        File file = new File("C:/Users/Natalia/Desktop/books.xls");
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
