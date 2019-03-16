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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestClass {

    Analyzer analyzer = new Analyzer();
    ArrayList<Book> books = new ArrayList<>();

    @Test
    public void getFiles() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives");
        listFilesForFolder(folder);
        List<String> arrayAfterSort = analyzer.getResults(books);
        createExcelFile(arrayAfterSort, books, "allWords");
    }

    @Test
    public void getResultsForDetectiveDictionary() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();
        List<String> detectiveDictionary = listFilesForDetectiveFolder(folder);
        List<String> arrayAfterSort = analyzer.getResultswithDetectivesDictionary(books);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        books.add(createAverageBook(books));
        createExcelFile(arrayAfterSort, books, "detectives");
    }

    private Book createAverageBook(ArrayList<Book> books) {
        Book averageBook = new Book("averageBook");
        ArrayList<Double> tf_idf = new ArrayList<Double>();
        for (int j = 0; j < books.get(0).getTf_idf().size(); j++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < books.size(); i++) {
                BigDecimal tf_idf_ = new BigDecimal(books.get(i).getTf_idf().get(j));
                sum = sum.add(tf_idf_);
            }

            BigDecimal zero = new BigDecimal(0);
            BigDecimal n = new BigDecimal(books.size());
            if (!sum.equals(zero)) {
                zero = sum.divide(n, 5, BigDecimal.ROUND_HALF_EVEN);
            }
            tf_idf.add(zero.doubleValue());

        }
        averageBook.setTf_idf(tf_idf);
        return averageBook;
    }


    public List<String> listFilesForDetectiveFolder(File folder) {
        List<String> dictionaryDetective = null;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                dictionaryDetective = getBookFromDetectiveBook(fileEntry);
                System.out.println(fileEntry.getName());
            }
        }
        return dictionaryDetective;
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
        int p = 0;
    }


    public void getBookFromFile(File file) {

        String s = analyzer.getDocFromFileSystem(file);
        ArrayList<String> wordsFromString = analyzer.getWordsFromString(s);

        ArrayList<String> wordsWithoutStop = analyzer.getWordsWithoutStop(wordsFromString);
        ArrayList<String> fileAfterPorter = analyzer.getWordsAfterPorter(wordsWithoutStop);

        Book book = new Book(file.getName(), fileAfterPorter);
        books.add(book);
    }

    public List<String> getBookFromDetectiveBook(File file) {

        String s = analyzer.getDocFromFileSystem(file);
        ArrayList<String> wordsFromString = analyzer.getWordsFromString(s);

        String detectiveWordsString = analyzer.usingBufferedReader("src/resources/detectiveDictionary.txt");
        ArrayList<String> detectiveWords = analyzer.getWordsFromString(detectiveWordsString);
        wordsFromString.retainAll(detectiveWords);

        // ArrayList<String> wordsWithoutStop = analyzer.getWordsWithoutStop(wordsFromString);
        ArrayList<String> fileAfterPorter = analyzer.getWordsAfterPorter(wordsFromString);

        Book book = new Book(file.getName(), fileAfterPorter);
        books.add(book);
        return detectiveWords;
    }


    public void createExcelFile(List<String> lexems, ArrayList<Book> books, String bookName) throws IOException {
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
