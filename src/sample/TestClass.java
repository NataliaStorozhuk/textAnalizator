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
import java.util.Collections;
import java.util.List;

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
        listFilesForFolder(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);
        ExcelExporter.createExcelFile(testViborka.arrayAfterSort, books, "allWords");
    }


    //Пробуем записать данные в json ТЕСТОВЫЕ
    @Test
    public void getBaseFrequencesInJson() {
        final File folder = new File("C:/Users/Natalia/Desktop/test");
        long start = System.currentTimeMillis();

        listFilesForFolder(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        testViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson("C:/Users/Natalia/Desktop/testJsonFile", testViborka);
        AllTokensClass test = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/testJsonFile", AllTokensClass.class);
    }

    //Пробуем записать данные в json РАБОЧИЕ
    @Test
    public void getBaseFrequencesInJsonDETECTIVE() {
        final File folder = new File("C:/Users/Natalia/Desktop/test");
       // final File folder = new File("C:/Users/Natalia/Desktop/test");
        long start = System.currentTimeMillis();
            //14 книг
        listFilesForFolder(folder); //1.22-1,29
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        testViborka.w = books.get(books.size() - 1).getW();

        ObjectToJsonConverter.fromObjectToJson("C:/Users/Natalia/Desktop/test.json", testViborka);
        AllTokensClass test = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/test.json", AllTokensClass.class);
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
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

    //Опыт 2 без удаления имен
    @Test
    public void getResultsForMaxDictionary() throws IOException {
        final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        long start = System.currentTimeMillis();

        listFilesForFolder(folder);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<BookProfile> booksMax = new ArrayList<>();
        BookProfile maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = new ArrayList<>();
        for (int i = 0; i < maxBook.tf.size(); i++) {
            maxArrayList.add(testViborka.arrayAfterSort.get(maxBook.tf.get(i)));
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
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(books);

        books.add(createAverageBook(books));

        ArrayList<BookProfile> booksMax = new ArrayList<>();
        BookProfile maxBook = createMaxBook(createAverageBook(books), 300);
        booksMax.add(maxBook);

        ArrayList<String> maxArrayList = new ArrayList<>();
        for (int i = 0; i < maxBook.tf.size(); i++) {
            maxArrayList.add(testViborka.arrayAfterSort.get(maxBook.tf.get(i)));
        }

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
   //     AllTokensClass studyViborka = (AllTokensClass) ObjectToJsonConverter.fromJsonToObject("C:/Users/Natalia/Desktop/test.json", AllTokensClass.class);

        //тестовая следующая
        //      File file = new File("C:/Users/Natalia/Desktop/text4.txt");
     //   File file = new File("C:/Users/Natalia/Desktop/text4.txt");

        File file = new File("C:/Users/Natalia/Desktop/test_detectives/Агата Кристи 10 негритят.txt");
        BookProfile testBook1 = getBookFromFile(file);

        ArrayList<BookProfile> testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook1);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka.w = testBook1.getW();

        Double skalar = analyzer.getSkalar(studyViborka, testViborka);
        Double cos = analyzer.getCos(studyViborka, testViborka, skalar);

        //тестовая 2
       file = new File("C:/Users/Natalia/Desktop/test_detectives/Дарья Калинина Возвращение блудного бумеранга.txt");
   //     file = new File("C:/Users/Natalia/Desktop/text5.txt");
        BookProfile testBook2 = getBookFromFile(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook2);
        AllTokensClass testViborka2 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka2.w = testBook2.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka2);
        cos = analyzer.getCos(studyViborka, testViborka, skalar);

        //тестовая 3
        file = new File("C:/Users/Natalia/Desktop/test_detectives/Борис Акунин Турецкий Гамбит.txt");
        BookProfile testBook3 = getBookFromFile(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook3);
        AllTokensClass testViborka3 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka3.w = testBook3.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka3);
        cos = analyzer.getCos(studyViborka, testViborka, skalar);

        //тестовая 4
        file = new File("C:/Users/Natalia/Desktop/test_another/Брукс Карен. Озеро в лунном свете - royallib.ru.txt");
        BookProfile testBook4 = getBookFromFile(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook4);
        AllTokensClass testViborka4 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka4.w = testBook4.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka4);
        cos = analyzer.getCos(studyViborka, testViborka, skalar);

        //тестовая 5
        file = new File("C:/Users/Natalia/Desktop/test_another/Толкиен Джон. Дети Хурина - royallib.ru.txt.txt");
        BookProfile testBook5 = getBookFromFile(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook5);
        AllTokensClass testViborka5 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka5.w = testBook5.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka5);
        cos = analyzer.getCos(studyViborka, testViborka, skalar);

        //тестовая 6
        file = new File("C:/Users/Natalia/Desktop/test_another/Шоу Бернард. Тележка с яблоками - royallib.ru.txt");
        BookProfile testBook6 = getBookFromFile(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook6);
        AllTokensClass testViborka6 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka6.w = testBook6.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka6);
        cos = analyzer.getCos(studyViborka, testViborka, skalar);

    }

    @Test
    public void getRealBookWithoutNames(){
        File file = new File("C:/Users/Natalia/Desktop/test_detectives/Агата Кристи 10 негритят.txt");
        BookProfile testBook1 = getBookFromFileWithoutNames(file);
    }

    @Test
    public void getSimiliarityWithMagic() {

        //тестовые 3
        final File folder = new File("C:/Users/Natalia/Desktop/detectives_utf8");
        listFilesForFolderWithoutNames(folder);
        AllTokensClass studyViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        studyViborka.w = books.get(books.size() - 1).getW();

        String detectiveWordsString = usingBufferedReader("src/resources/detectiveDictionary_afterMethod.txt");
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

//Это умножение idf на 5
        for (int i = 0; i < studyViborka.arrayAfterSort.size(); i++) {
            for (int j = 0; j < detectiveWords.size(); j++) {
                if (studyViborka.arrayAfterSort.get(i).equals(detectiveWords.get(j))) {
                    studyViborka.idf.set(i, studyViborka.idf.get(i) * 5);
                    break;
                }
            }
        }

        //тестовая следующая
        File file = new File("C:/Users/Natalia/Desktop/test_detectives/Агата Кристи 10 негритят.txt");
        BookProfile testBook1 = getBookFromFileWithoutNames(file);

        ArrayList<BookProfile> testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook1);
        AllTokensClass testViborka = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka.w = testBook1.getW();

        Double skalar = analyzer.getSkalar(studyViborka, testViborka);


        //тестовая 2
        file = new File("C:/Users/Natalia/Desktop/test_detectives/Дарья Калинина Возвращение блудного бумеранга.txt");
        BookProfile testBook2 = getBookFromFileWithoutNames(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook2);
        AllTokensClass testViborka2 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka2.w = testBook2.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka);

        //тестовая 3
        file = new File("C:/Users/Natalia/Desktop/test_detectives/Борис Акунин Турецкий Гамбит.txt");
        BookProfile testBook3 = getBookFromFileWithoutNames(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook3);
        AllTokensClass testViborka3 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka3.w = testBook3.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka);

        //тестовая 4
        file = new File("C:/Users/Natalia/Desktop/test_another/Брукс Карен. Озеро в лунном свете - royallib.ru.txt");
        BookProfile testBook4 = getBookFromFileWithoutNames(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook4);
        AllTokensClass testViborka4 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka4.w = testBook4.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka);

        //тестовая 5
        file = new File("C:/Users/Natalia/Desktop/test_another/Толкиен Джон. Дети Хурина - royallib.ru.txt.txt");
        BookProfile testBook5 = getBookFromFileWithoutNames(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook5);
        AllTokensClass testViborka5 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka5.w = testBook5.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka);

        //тестовая 6
        file = new File("C:/Users/Natalia/Desktop/test_another/Шоу Бернард. Тележка с яблоками - royallib.ru.txt");
        BookProfile testBook6 = getBookFromFileWithoutNames(file);

        testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook6);
        AllTokensClass testViborka6 = statisticGetter.getBaseFrequencies(testBookOnly);
        testViborka6.w = testBook6.getW();

        skalar = analyzer.getSkalar(studyViborka, testViborka);

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
    public void listFilesForFolder(File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
          //      listFilesForFolder(fileEntry);
            } else {
                books.add(getBookFromFile(fileEntry));
            }
        }
    }

    //Ходим по папке, собираем файлы в объекты BookProfile
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
    public BookProfile getBookWithDetectiveWordsOnly(File file, ArrayList<String> detectiveWords) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> wordsFromString = getWordsFromString(s);

        wordsFromString.retainAll(detectiveWords);

        ArrayList<String> fileAfterPorter = getWordsAfterPorter(wordsFromString);

        BookProfile book = new BookProfile(file.getName(), fileAfterPorter
        );
        return book;
    }

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
        for (int i = 0; i < books.size(); i++) {
            getTfIdf(books.get(i), idf);
        }

        return arrayAfterSort;
    }


}
