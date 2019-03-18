package sample;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sample.FileToBookConverter.*;
import static sample.StatisticGetter.*;

public class TestClass {

    Analyzer analyzer = new Analyzer();
    ArrayList<Book> books = new ArrayList<>();

    //просто тест получения файлов и потом в эксельку что нибудь записать
    @Test
    public void getFiles() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives");
        listFilesForFolder(folder);
        List<String> arrayAfterSort = getBaseFrequencies(books);
        ExcelExporter.createExcelFile(arrayAfterSort, books, "allWords");
    }

    //Получить анализ выбранных 352 слов
    @Test
    public void getResultsForDetectiveDictionary() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();

        String detectiveWordsString = usingBufferedReader("src/resources/detectiveDictionary.txt");
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

        getListBooksWithDetectiveWordsOnly(folder, detectiveWords);
        List<String> arrayAfterSort = getResultsWithDetectivesDictionary(books);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        books.add(createAverageBook(books));
        ExcelExporter.createExcelFile(arrayAfterSort, books, "another");
    }

    //Опыт 2 без удаления имен
    @Test
    public void getResultsForMaxDictionary() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();

        listFilesForFolder(folder);
        List<String> arrayAfterSort = getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<Book> booksMax = new ArrayList<>();
        Book maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = new ArrayList<>();
        for (int i = 0; i < maxBook.tf.size(); i++) {
            maxArrayList.add(arrayAfterSort.get(maxBook.tf.get(i)));
        }

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);


        ExcelExporter.createExcelFile(maxArrayList, booksMax, "detectives_utf8");
    }

    //Опыт 2, имена удалены
    @Test
    public void getResultsForMaxDictionaryWithoutNames() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/another_viborka_utf8");
        long start = System.currentTimeMillis();

        listFilesForFolderWithoutNames(folder);
        List<String> arrayAfterSort = getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<Book> booksMax = new ArrayList<>();
        Book maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = new ArrayList<>();
        for (int i = 0; i < maxBook.tf.size(); i++) {
            maxArrayList.add(arrayAfterSort.get(maxBook.tf.get(i)));
        }

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);


        ExcelExporter.createExcelFile(maxArrayList, booksMax, "another_viborka_utf8");
    }


    //из книги со соредними значениями показателя высчитываем и сортируем максимум countWords штук слов
    private Book createMaxBook(Book averageBook, Integer countWords) {
        Book book = new Book("maxBook");
        book.setTf(new ArrayList<>());
        book.setTf_idf(new ArrayList<>());
        for (int i = 0; i < countWords; i++) {
            book.tf.add(averageBook.tf_idf.indexOf(Collections.max(averageBook.tf_idf)));

            book.tf_idf.add(Collections.max(averageBook.tf_idf));
            averageBook.tf_idf.set(averageBook.tf_idf.indexOf(Collections.max(averageBook.tf_idf)), 0.0);
        }
        return book;
    }

    //из книг всех - всех высчитываем средние показатели
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


    //Ходим по папке, собираем файлы в объекты Book
    public void listFilesForFolder(File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                books.add(getBookFromFile(fileEntry));
            }
        }
    }

    //Ходим по папке, собираем файлы в объекты Book
    public void listFilesForFolderWithoutNames(File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                books.add(getBookFromFileWithoutNames(fileEntry));
            }
        }
    }

    /*Получаем список книг, в котором лексемы каждой книги содержат ТОЛЬКО слова из detectiveWords*/
    public void getListBooksWithDetectiveWordsOnly(File folder, ArrayList<String> detectiveWords) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                books.add(getBookWithDetectiveWordsOnly(fileEntry, detectiveWords));
                System.out.println(fileEntry.getName());
            }
        }
    }

    /*Берем книгу из файла, выкиыдваем лишние слова*/
    public Book getBookWithDetectiveWordsOnly(File file, ArrayList<String> detectiveWords) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> wordsFromString = getWordsFromString(s);

        wordsFromString.retainAll(detectiveWords);

        ArrayList<String> fileAfterPorter = getWordsAfterPorter(wordsFromString);

        Book book = new Book(file.getName(), fileAfterPorter
        );
        return book;
    }

    public List<String> getResultsWithDetectivesDictionary(ArrayList<Book> books) {

        //формируем общий, сортируем, выкидываем повторы
        String detectiveWordsString = usingBufferedReader("src/resources/detectiveDictionary.txt");
        ArrayList<String> arrayAfterSort = getWordsFromString(detectiveWordsString);

        for (int i = 0; i < books.size(); i++) {
            getTf(books.get(i), arrayAfterSort);
        }

        //считаем общее число файлов, в которых встречается слово
        ArrayList<Integer> documentFrequency = getDf(books);

        //считаем idf
        ArrayList<Double> idf = getIdf(documentFrequency, books.size());

        //получаем tf-idf
        for (int i = 0; i < books.size(); i++) {
            getTfIdf(books.get(i), idf);
        }

        return arrayAfterSort;
    }


}
