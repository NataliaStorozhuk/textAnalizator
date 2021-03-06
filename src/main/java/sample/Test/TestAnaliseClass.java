package sample.Test;

import org.testng.annotations.Test;
import sample.Analizator.Analyzer;
import sample.Analizator.StatisticGetter;
import sample.DTO.BookProfile;
import sample.DTO.GenreProfile;
import sample.FileConverter.ObjectToJsonConverter;
import sample.FileConverter.TxtToBookConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sample.Analizator.Analyzer.getAverageW;
import static sample.Analizator.StatisticGetter.*;
import static sample.FileConverter.TxtToBookConverter.*;

public class TestAnaliseClass {

    private Analyzer analyzer = new Analyzer();
    private StatisticGetter statisticGetter = new StatisticGetter();
    private TxtToBookConverter fileToBookConverter = new TxtToBookConverter();
    private ArrayList<BookProfile> books = new ArrayList<>();
    private String desktopPath = System.getProperty("user.home") + "\\" + "Desktop" + "\\";

    //просто тест получения файлов и потом в эксельку что нибудь записать
    @Test
    public void getFiles() throws IOException {
        final File folder = new File(desktopPath + "test");
        books = listBooksFromFolder(folder);
        GenreProfile testViborka = getBaseFrequencies(books);
        ExcelExporter.createExcelFile(testViborka.getLexemasArray(), books, "allWordsMagicW");
    }


    //Пробуем записать данные в json ТЕСТОВЫЕ
    @Test
    public void getBaseFrequencesInJson() {
        final File folder = new File(desktopPath + "detectives_utf8");

        books = listBooksFromFolder(folder);
        GenreProfile testViborka = statisticGetter.getBaseFrequencies(books);
        books.add(createAverageBook(books));
        testViborka.setW(books.get(books.size() - 1).getW());

        ObjectToJsonConverter.fromObjectToJson(desktopPath + "testJsonFile", testViborka);
        // GenreProfile test = (GenreProfile) ObjectToJsonConverter.fromJsonToObject(desktopPath + "testJsonFileTf*IDF", GenreProfile.class);
    }

    //Пробуем записать данные в json РАБОЧИЕ
    @Test
    public void getBaseFrequencesInJsonDETECTIVE() {
        final File folder = new File(desktopPath + "detectives_utf8");
        long start = System.currentTimeMillis();
        //50 книг
        books = listBooksFromFolder(folder);
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;           //9604 распаралелено, 17786 если по очереди
        System.out.println("Время работы в милисекундах: " + timeConsumedMillis);

        GenreProfile testViborka = statisticGetter.getBaseFrequencies(books); //это очень долго бежит
        //берется среднее по книгам всеем
        books.add(createAverageBook(books));
//туда устанавливается w
        testViborka.setW(books.get(books.size() - 1).getW());

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
            maxArrayList.add(testViborka.genreLexemas.get(maxBook.tf.get(i)).getLexema());
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

        books = listBooksFromFolder(folder);
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
    public void getSimilarityWithoutMagic() {
        GenreProfile studyViborka = (GenreProfile) ObjectToJsonConverter.fromJsonToObject
                (desktopPath + "detectivesTFIDF.json", GenreProfile.class);
        final File folder = new File(desktopPath + "test_6Books");
        for (final File fileEntry : folder.listFiles()) {
            analyzer.getFileCos(fileEntry.getPath(), studyViborka);
        }
    }

    //Считаем скалярное произведение, тестим на 6 книгах после обновления значений весов
    @Test
    public void getSimilarityWithMagic() {
        GenreProfile studyViborka = (GenreProfile) ObjectToJsonConverter.fromJsonToObject(
                desktopPath + "detectivesTFIDF.json", GenreProfile.class);
        dimentionIDFforDetectiveWords(studyViborka, 1000);
        final File folderTestBooks = new File(desktopPath + "test_6Books");
        for (final File fileEntry : folderTestBooks.listFiles()) {
            analyzer.getFileCos(fileEntry.getPath(), studyViborka);
        }
    }

    private void dimentionIDFforDetectiveWords(GenreProfile studyViborka, Integer cof) {
        String detectiveWordsString = usingBufferedReader(this.getClass().getResource("/detectiveDictionary_afterMethod.txt").getPath());
        ArrayList<String> detectiveWords = getWordsFromString(detectiveWordsString);

//Это умножение idf на 5
        for (int i = 0; i < studyViborka.genreLexemas.size(); i++) {
            for (int j = 0; j < detectiveWords.size(); j++) {
                if (studyViborka.genreLexemas.get(i).equals(detectiveWords.get(j))) {
                    studyViborka.getWArray().set(i, studyViborka.getWArray().get(i) / cof);
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
        ArrayList<Double> w = getAverageW(books);
        averageBook.setW(w);
        return averageBook;
    }


    /*Получаем список книг, в котором лексемы каждой книги содержат ТОЛЬКО слова из detectiveWords*/
    //тут надо разобраться, сама не поняла, что сделала
    public void getListBooksWithDetectiveWordsOnly(File folder, ArrayList<String> detectiveWords) {

        //чуть побыстрее работает без перебора папок
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listBooksFromFolder(fileEntry);
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

        for (BookProfile book1 : books) {
            getTf(book1, arrayAfterSort);
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
        String stopWordsString = usingBufferedReader("C:\\Users\\Natalia\\Desktop\\analyzer\\stop_words.txt");
        System.out.println(stopWordsString);
    }
}
