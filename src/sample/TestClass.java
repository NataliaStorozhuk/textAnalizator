package sample;

import org.testng.annotations.Test;
import sample.DTO.AllTokensClass;
import sample.DTO.BookProfile;
import sample.FileConverter.ExcelExporter;
import sample.FileConverter.ObjectToJsonConverter;

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

public class TestClass {

    Analyzer analyzer = new Analyzer();
    StatisticGetter statisticGetter = new StatisticGetter();
    ArrayList<BookProfile> books = new ArrayList<>();

    //просто тест получения файлов и потом в эксельку что нибудь записать
    @Test
    public void getFiles() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/test");
        books = listFilesForFolder(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);
        ExcelExporter.createExcelFile(testViborka.arrayAfterSort, books, "allWords");
    }


    //Пробуем записать данные в json ТЕСТОВЫЕ
    @Test
    public void getBaseFrequencesInJson() {
        final File folder = new File("C:/Users/Natalia/Desktop/test");
        long start = System.currentTimeMillis();

        books = listFilesForFolder(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        testViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson("C:/Users/Natalia/Desktop/testJsonFile", testViborka);
        AllTokensClass test = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/testJsonFile", AllTokensClass.class);
    }

    //Пробуем записать данные в json РАБОЧИЕ
    @Test
    public void getBaseFrequencesInJsonDETECTIVE() {
        final File folder = new File("C:/Users/admin/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();
        //50 книг
        books = listFilesForFolder(folder);
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;           //9604 распаралелено, 17786 если по очереди
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books); //это очень долго бежит!
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);
        books.add(createAverageBook(books));
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);
        testViborka.w = books.get(books.size() - 1).getW();
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);
        ObjectToJsonConverter.fromObjectToJson("C:/Users/admin/Desktop/test.json", testViborka);
        AllTokensClass test = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/admin/test.json", AllTokensClass.class);
        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);
    }

    //Получить анализ выбранных 352 слов
    @Test
    public void getResultsForDetectiveDictionary() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();

        String detectiveWordsString = usingBufferedReader("src/resources/detectiveDictionary_afterMethod.txt");
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

        getListBooksWithDetectiveWordsOnly(folder, detectiveWords);
        List<String> arrayAfterSort = getResultsWithDetectivesDictionary(books);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        books.add(createAverageBook(books));
        ExcelExporter.createExcelFile(arrayAfterSort, books, "another");
    }

    //Поиск наиболее часто употребимых слов 300 шт
    //Опыт 2 без удаления имен
    @Test
    public void getResultsForMaxDictionary() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/test");
        //    final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();

        books = listFilesForFolder(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<BookProfile> booksMax = new ArrayList<>();
        BookProfile maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = getMaxUseWords(testViborka, maxBook);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);


        ExcelExporter.createExcelFile(maxArrayList, booksMax, "detectives_utf8_max300");
    }

    private ArrayList<String> getMaxUseWords(AllTokensClass testViborka, BookProfile maxBook) {
        ArrayList<String> maxArrayList = new ArrayList<>();
        for (int i = 0; i < maxBook.tf.size(); i++) {
            maxArrayList.add(testViborka.arrayAfterSort.get(maxBook.tf.get(i)));
        }
        return maxArrayList;
    }

    //Опыт 2, имена удалены
    @Test
    public void getResultsForMaxDictionaryWithoutNames() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/another_viborka_utf8");
        long start = System.currentTimeMillis();

        books = listFilesForFolderWithoutNames(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<BookProfile> booksMax = new ArrayList<>();
        BookProfile maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = getMaxUseWords(testViborka, maxBook);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        ExcelExporter.createExcelFile(maxArrayList, booksMax, "another_viborka_utf8");
    }

    //Считаем скалярное произведение, тестим на 6 книгах ДО модных нововведений
    @Test
    public void getSimiliarityWithoutMagic() {

        //тестовые 3
        AllTokensClass studyViborka = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/testJsonFileDetective.json", AllTokensClass.class);
        //         AllTokensClass studyViborka = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/test.json", AllTokensClass.class);

        //тестовая следующая
        //      File file = new File("C:/Users/Natalia/Desktop/text4.txt");
        //     final File folder = new File("C:/Users/Natalia/Desktop/test_testBooks");
        final File folder = new File("C:/Users/Natalia/Desktop/test_6Books");
        for (final File fileEntry : folder.listFiles()) {
            analyzer.getFileCos(fileEntry.getPath(), studyViborka);
        }

    }


    //Получить книгу после применения нового метода с именами
    @Test
    public void getRealBookWithoutNames() {
        File file = new File("C:/Users/Natalia/Desktop/test_detectives/Агата Кристи 10 негритят.txt");
        BookProfile testBook1 = getBookFromFileWithoutNames(file);
    }

    //Анализ без имен!
    @Test
    public void getSimiliarityWithMagic() {

       /* final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        //final File folder = new File("C:/Users/Natalia/Desktop/test");

        books = listFilesForFolderWithoutNames(folder);
        AllTokensClass baseViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        baseViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson("C:/Users/Natalia/Desktop/testJsonFile", baseViborka);*/
        AllTokensClass studyViborka = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/testJsonFileWithoutNames", AllTokensClass.class);

        ymnojIDFforDetectiveWords(studyViborka, 2);

        //     final File folderTestBooks = new File("C:/Users/Natalia/Desktop/test_testBooks");
        final File folderTestBooks = new File("C:/Users/Natalia/Desktop/test_6Books");
        for (final File fileEntry : folderTestBooks.listFiles()) {
            analyzer.getFileCos(fileEntry.getPath(), studyViborka);
        }

    }

    private void ymnojIDFforDetectiveWords(AllTokensClass studyViborka, Integer cof) {
        String detectiveWordsString = usingBufferedReader("src/resources/detectiveDictionary_afterMethod.txt");
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

//Это умножение idf на 5
        for (int i = 0; i < studyViborka.arrayAfterSort.size(); i++) {
            for (int j = 0; j < detectiveWords.size(); j++) {
                if (studyViborka.arrayAfterSort.get(i).equals(detectiveWords.get(j))) {
                    studyViborka.w.set(i, studyViborka.w.get(i) / cof);
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
        for (int i = 0; i < countWords; i++) {
            book.tf.add(averageBook.tf_idf.indexOf(Collections.max(averageBook.tf_idf)));

            book.tf_idf.add(Collections.max(averageBook.tf_idf));
            averageBook.tf_idf.set(averageBook.tf_idf.indexOf(Collections.max(averageBook.tf_idf)), 0.0);
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
    public ArrayList<BookProfile> listFilesForFolder(File folder) {

    /*    for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
          //      listFilesForFolder(fileEntry);
            } else {
                books.add(getBookFromFile(fileEntry));
            }
        }*/
        ArrayList<BookProfile> books = new ArrayList<>();
        CompletableFuture[] futures = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    books.add(getBookFromFile(file));
                    return null;
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        return books;
    }

    //Ходим по папке, собираем файлы в объекты BookProfile, обработка без имен
    public ArrayList<BookProfile> listFilesForFolderWithoutNames(File folder) {

        ArrayList<BookProfile> books = new ArrayList<>();
        CompletableFuture[] futures = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    books.add(getBookFromFileWithoutNames(file));
                    return null;
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        return books;
    }

    /*Получаем список книг, в котором лексемы каждой книги содержат ТОЛЬКО слова из detectiveWords*/
    //тут надо разобраться, сама не поняла, что сделала
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
    public BookProfile getBookWithDetectiveWordsOnly(File file, ArrayList<String> detectiveWords) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> wordsFromString = getWordsFromString(s);

        wordsFromString.retainAll(detectiveWords);

        ArrayList<String> fileAfterPorter = getWordsAfterPorter(wordsFromString);

        BookProfile book = new BookProfile(file.getName(), fileAfterPorter
        );
        return book;
    }

    /*Получаем анализ слов, которые в файле с "детективными" словами*/
    public List<String> getResultsWithDetectivesDictionary(ArrayList<BookProfile> books) {

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
        for (BookProfile book : books) {
            getTfIdf(book, idf);
        }

        return arrayAfterSort;
    }


}
