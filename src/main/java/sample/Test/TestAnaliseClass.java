package sample.Test;

import org.testng.annotations.Test;
import sample.Analyzer;
import sample.DTO.BookProfile;
import sample.DTO.GenreProfile;
import sample.FileConverter.ExcelExporter;
import sample.FileConverter.FileToBookConverter;
import sample.FileConverter.ObjectToJsonConverter;
import sample.StatisticGetter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static sample.FileConverter.FileToBookConverter.*;
import static sample.StatisticGetter.*;

public class TestAnaliseClass {

    private Analyzer analyzer = new Analyzer();
    private StatisticGetter statisticGetter = new StatisticGetter();
    private FileToBookConverter fileToBookConverter = new FileToBookConverter();
    private ArrayList<BookProfile> books = new ArrayList<>();
    private String desktopPath = System.getProperty("user.home") + "\\" + "Desktop" + "\\";

    //просто тест получения файлов и потом в эксельку что нибудь записать
    @Test
    public void getFiles() throws IOException {
        final File folder = new File(desktopPath + "test");
        books = listFilesFromFolder(folder);
        GenreProfile testViborka = statisticGetter.getBaseFrequencies(books);
        //  ExcelExporter.createExcelFile(testViborka.lexemesList, books, "allWordsMagicW");
    }


    //Пробуем записать данные в json ТЕСТОВЫЕ
    @Test
    public void getBaseFrequencesInJson() {
        final File folder = new File(desktopPath + "detectives_utf8");

        books = listFilesFromFolder(folder);
        GenreProfile testViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        testViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson(desktopPath + "testJsonFile", testViborka);
        GenreProfile test = (GenreProfile) ObjectToJsonConverter.fromJsonToObject(desktopPath + "testJsonFileTf*IDF", GenreProfile.class);
    }

    //Пробуем записать данные в json РАБОЧИЕ
    @Test
    public void getBaseFrequencesInJsonDETECTIVE() {
        final File folder = new File(desktopPath + "detectives_utf8");
        long start = System.currentTimeMillis();
        //50 книг
        books = listFilesFromFolder(folder);
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;           //9604 распаралелено, 17786 если по очереди
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        GenreProfile testViborka = statisticGetter.getBaseFrequencies(books); //это очень долго бежит
        books.add(createAverageBook(books));

        testViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson(desktopPath + "detectivesTFIDF.json", testViborka);
        GenreProfile test = (GenreProfile) ObjectToJsonConverter.fromJsonToObject(desktopPath + "detectivesTFIDF.json", GenreProfile.class);
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);
    }


    @Test
    public void getWDictionaryDetective() throws IOException {
        getResultsForDetectiveDictionary("detectives_utf8");
    }

    @Test
    public void getWDictionaryAnother() throws IOException {
        getResultsForDetectiveDictionary("another_viborka_utf8");
    }

    @Test
    public void getWDictionaryTest() throws IOException {
        getResultsForDetectiveDictionary("test");
    }

    //Получить анализ выбранных ранее "ключевых" слов
    @Test
    public void getResultsForDetectiveDictionary(String folderName) throws IOException {
        final File folder = new File(desktopPath + folderName);
        long start = System.currentTimeMillis();

        String detectiveWordsString = usingBufferedReader("/detectiveDictionary_afterMethod.txt");
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

        getListBooksWithDetectiveWordsOnly(folder, detectiveWords);
        List<String> arrayAfterSort = getResultsWithDetectivesDictionary(books);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        books.add(createAverageBook(books));
        ExcelExporter.createExcelFile(arrayAfterSort, books, folderName + "getWDictionary");
    }


    private ArrayList<String> getMaxUseWords(GenreProfile testViborka, BookProfile maxBook) {
        ArrayList<String> maxArrayList = new ArrayList<>();
        for (int i = 0; i < maxBook.tf.size(); i++) {
            maxArrayList.add(testViborka.lexemesList.get(maxBook.tf.get(i)));
        }
        return maxArrayList;
    }

    @Test
    public void getMaxDetective() throws IOException {
        getResultsForMaxDictionary("detectives_utf8");
    }

    @Test
    public void getMaxAnother() throws IOException {
        getResultsForMaxDictionary("another_utf8");
    }

    @Test
    public void getMaxTest() throws IOException {
        getResultsForMaxDictionary("test");
    }

    //Поиск наиболее часто употребимых слов 300 шт

    public void getResultsForMaxDictionary(String folderName) throws IOException {

        final File folder = new File(desktopPath + folderName);
        long start = System.currentTimeMillis();

        books = listFilesFromFolder(folder);
        GenreProfile testViborka = statisticGetter.getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<BookProfile> booksMax = new ArrayList<>();
        BookProfile maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = getMaxUseWords(testViborka, maxBook);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        ExcelExporter.createExcelFile(maxArrayList, booksMax, folderName + "getWCountWords300WithoutSF_1");
    }

    //Считаем скалярное произведение, тестим на 6 книгах ДО модных нововведений
    @Test
    public void getSimiliarityWithoutMagic() {

        //тестовые 3
        GenreProfile studyViborka = (GenreProfile) ObjectToJsonConverter.fromJsonToObject(desktopPath + "detectivesTFIDF.json", GenreProfile.class);
        //         GenreProfile studyViborka = (GenreProfile) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/test.json", GenreProfile.class);

        //тестовая следующая
        //      File file = new File("C:/Users/Natalia/Desktop/text4.txt");
        //     final File folder = new File("C:/Users/Natalia/Desktop/test_testBooks");
        final File folder = new File(desktopPath + "test_6Books");
        for (final File fileEntry : folder.listFiles()) {
            analyzer.getFileCos(fileEntry.getPath(), studyViborka);
        }

    }


    @Test
    public void getSimiliarityWithMagic() {

       /* final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        //final File folder = new File("C:/Users/Natalia/Desktop/test");

        books = listFilesFromFolder(folder);
        GenreProfile baseViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        baseViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson("C:/Users/Natalia/Desktop/testJsonFile", baseViborka);*/
        GenreProfile studyViborka = (GenreProfile) ObjectToJsonConverter.fromJsonToObject(desktopPath + "detectivesTFIDF.json", GenreProfile.class);

        ymnojIDFforDetectiveWords(studyViborka, 1000);

        //     final File folderTestBooks = new File("C:/Users/Natalia/Desktop/test_testBooks");
        final File folderTestBooks = new File(desktopPath + "test_6Books");
        for (final File fileEntry : folderTestBooks.listFiles()) {
            analyzer.getFileCos(fileEntry.getPath(), studyViborka);
        }

    }

    private void ymnojIDFforDetectiveWords(GenreProfile studyViborka, Integer cof) {
        String detectiveWordsString = usingBufferedReader(this.getClass().getResource("/detectiveDictionary_afterMethod.txt").getPath());
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

//Это умножение idf на 5
        for (int i = 0; i < studyViborka.lexemesList.size(); i++) {
            for (int j = 0; j < detectiveWords.size(); j++) {
                if (studyViborka.lexemesList.get(i).equals(detectiveWords.get(j))) {
                    studyViborka.w.set(i, studyViborka.w.get(i) / cof);
                    // studyViborka.w.set(i, 0.0);
                    break;
                }
            }
        }
    }

    //из книги со соредними значениями показателя высчитываем и сортируем максимум countWords штук слов
    private BookProfile createMaxBook(BookProfile averageBook, Integer countWords) {
        BookProfile book = new BookProfile("maxBook");
        book.setTf(new ArrayList<>());
        book.setTf_idf(new ArrayList<>());
        book.setNormTF(new ArrayList<>());
        book.setW(new ArrayList<>());
        for (int i = 0; i < countWords; i++) {
            //пишем индекс максимального
            book.tf.add(averageBook.w.indexOf(Collections.max(averageBook.w)));
            //пишем сам максимальный
            book.w.add(Collections.max(averageBook.w));
            //обнуляем у этой книги максимальный
            averageBook.w.set(averageBook.w.indexOf(Collections.max(averageBook.w)), 0.0);
        }
        return book;
    }

    //из книг всех - всех высчитываем средние показатели
    private BookProfile createAverageBook(ArrayList<BookProfile> books) {
        BookProfile averageBook = new BookProfile("averageBook");
        ArrayList<Double> w = new ArrayList<Double>();
        for (int j = 0; j < books.get(0).getW().size(); j++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < books.size(); i++) {
                BigDecimal getW_ = new BigDecimal(books.get(i).getW().get(j));
                sum = sum.add(getW_);
            }

            BigDecimal zero = new BigDecimal(0);
            BigDecimal n = new BigDecimal(books.size());
            if (!sum.equals(zero)) {
                zero = sum.divide(n, 5, BigDecimal.ROUND_HALF_EVEN);
            }
            w.add(zero.doubleValue());

        }
        averageBook.setW(w);
        return averageBook;
    }

    //Ходим по папке, собираем файлы в объекты BookProfile
    public ArrayList<BookProfile> listFilesFromFolder(File folder) {

        ArrayList<BookProfile> books = new ArrayList<>();
      /*  for (final File fileEntry : folder.listFiles()) {

            System.out.println(fileEntry.getName());
            books.add(fileToBookConverter.getBookFromFile(fileEntry));

        }*/
        CompletableFuture[] futures = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    books.add(fileToBookConverter.getBookFromFile(file));
                    return null;
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        return books;
    }

    /*Получаем список книг, в котором лексемы каждой книги содержат ТОЛЬКО слова из detectiveWords*/
    //тут надо разобраться, сама не поняла, что сделала
    public void getListBooksWithDetectiveWordsOnly(File folder, ArrayList<String> detectiveWords) {

        //чуть побыстрее работает без перебора папок
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesFromFolder(fileEntry);
            } else {
                books.add(getBookWithDetectiveWordsOnly(fileEntry, detectiveWords));
                System.out.println(fileEntry.getName());
            }
        }
    }

    /*Берем книгу из файла, выкиыдваем лишние слова*/
    public BookProfile getBookWithDetectiveWordsOnly(File file, ArrayList<String> detectiveWords) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> wordsFromString = getWordsFromString(s);

        wordsFromString.retainAll(detectiveWords);

        ArrayList<String> fileAfterPorter = getWordsAfterPorter(wordsFromString);

        BookProfile book = new BookProfile(file.getName(), fileAfterPorter);
        return book;
    }

    /*Получаем анализ слов, которые в файле с "детективными" словами*/
    public List<String> getResultsWithDetectivesDictionary(ArrayList<BookProfile> books) {

        //формируем общий, сортируем, выкидываем повторы
        String detectiveWordsString = usingBufferedReader("/detectiveDictionary_afterMethod.txt");
        ArrayList<String> arrayAfterSort = getWordsFromString(detectiveWordsString);

        for (int i = 0; i < books.size(); i++) {
            getTf(books.get(i), arrayAfterSort);
        }

        //считаем общее число файлов, в которых встречается слово
        ArrayList<Double> documentFrequency = getDf(books);

        //считаем idf
        ArrayList<Double> idf = getIdf(documentFrequency, books.size());

        //получаем tf-idf
        for (BookProfile book : books) {
            getTfIdf(book, idf);
        }

        return arrayAfterSort;
    }


    @Test
    public void testStopWords() {
        String stopWordsString = usingBufferedReader(FileToBookConverter.class.getResource("/stop_words.txt").getPath());
    }
}
