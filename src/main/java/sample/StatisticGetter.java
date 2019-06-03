package sample;

import sample.DTO.BookProfile;
import sample.DTO.GenreLexema;
import sample.DTO.GenreProfile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static sample.Analyzer.getAverageW;

public class StatisticGetter {

    public static GenreProfile getBaseFrequencies(ArrayList<BookProfile> books) {

        System.out.println("Разбор по времени метода getBaseFrequencies");
        long start = System.currentTimeMillis();

        //формируем общий, сортируем, выкидываем повторы
        GenreProfile genreProfile = getAllTokensArray(books);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("getAllTokensArray" + timeConsumedMillis);
        start = System.currentTimeMillis();

        //получаем tf - число каждой лексемы в книге
  /*       for (BookProfile book : books) {
            getTf(book, genreProfile.lexemesList);
        }//долго
*/

        CompletableFuture[] futures = Arrays.stream(books.toArray())
                .map(book -> CompletableFuture.supplyAsync(() -> {
                    getTf((BookProfile) book, genreProfile.getLexemasArray());
                    return null;
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();

        finish = System.currentTimeMillis();
        timeConsumedMillis = finish - start;           //
        System.out.println("getTf" + timeConsumedMillis);
        start = System.currentTimeMillis();

        //считаем общее число файлов, в которых встречается слово
        ArrayList<Double> documentFrequency = getDf(books);
        genreProfile.setDf(documentFrequency);

        //считаем idf
        if (books.size() != 1)
            genreProfile.setIdf(getIdf(documentFrequency, books.size()));
        else {
            for (int j = 0; j < books.get(0).normTF.size(); j++) {
                genreProfile.genreLexemas.get(j).setIdf(1.0);
            }
        }


        //получаем tf-idf
        for (BookProfile book : books) {
            getTfIdf(book, genreProfile.getIdfArray());
        }


        //получаем w по модной формуле
        for (BookProfile book : books) {
            getW(book, genreProfile.getIdfArray());
            //         getW(book, genreProfile.df);
        }

        genreProfile.setW(getAverageW(books));

        System.out.println("Конец");
        return genreProfile;

    }


    /*Метод делает из списка разных книг отсортированный массив лексем без дубликатов*/
    public static GenreProfile getAllTokensArray(ArrayList<BookProfile> books) {
        GenreProfile genreProfile = new GenreProfile(true);
        ArrayList<String> allArray = new ArrayList<String>();
        for (BookProfile book : books) {
            allArray.addAll(book.getLexems());
        }
        List<String> arrayAfterSort = allArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(arrayAfterSort);


        for (int i = 0; i < arrayAfterSort.size(); i++) {
            GenreLexema genreLexema = new GenreLexema(arrayAfterSort.get(i));
            genreProfile.genreLexemas.add(genreLexema);
        }

        return genreProfile;
    }

    /*Получает количество повторений каждой лексемы в книге tf */
    public static void getTf(BookProfile book, List<String> arrayAfterSort) {
        ArrayList<Integer> tf = new ArrayList<Integer>();

        for (int i = 0; i < arrayAfterSort.size(); i++) {
            String temp = arrayAfterSort.get(i);
            int numberOfBooks = (int) book.getLexems()
                    .stream()
                    .filter(p -> p.equals(temp))
                    .count();

            tf.add(i, numberOfBooks);
        }


        book.setTf(tf);

        double maxIdf = Collections.max(book.tf);

        ArrayList<Double> newTF = new ArrayList<Double>();
        for (int i = 0; i < arrayAfterSort.size(); i++) {

            BigDecimal bdDF = new BigDecimal(tf.get(i));
            BigDecimal bdN = new BigDecimal(maxIdf);
            BigDecimal arg = new BigDecimal(0);
            if (!bdDF.equals(arg)) {
                arg = bdDF.divide(bdN, 5, BigDecimal.ROUND_HALF_EVEN);
            }

            newTF.add(i, arg.doubleValue());
        }
        book.normTF = newTF;
    }

    //Получили список, который содержит количества файлов, в которых повторяются лексемы
    public static ArrayList<Double> getDf(ArrayList<BookProfile> books) {
        ArrayList<Double> documentFrequency = new ArrayList<>();
        for (int i = 0; i < books.get(0).getTf().size(); i++) {
            double temp = 0;
            for (int j = 0; j < books.size(); j++) {
                if ((books.get(j).getTf().get(i)) != 0)
                    temp++;
            }
            documentFrequency.add(i, temp);
        }
        return documentFrequency;
    }

    //Пoлучили idf - логарифм (общее число доков/на число доков с каждой лексемой)
    public static ArrayList<Double> getIdf(ArrayList<Double> documentFrequency, Integer booksSize) {
        ArrayList<Double> idf = new ArrayList<>();
        for (int i = 0; i < documentFrequency.size(); i++) {

            //       http://pr0java.blogspot.com/2015/05/biginteger-bigdecimal_70.html
            BigDecimal bdDF = new BigDecimal(documentFrequency.get(i));
            BigDecimal bdN = new BigDecimal(booksSize);
            BigDecimal arg = new BigDecimal(0);
            double log = 0;
            if (!bdDF.equals(arg)) {
                arg = bdN.divide(bdDF, 5, BigDecimal.ROUND_HALF_EVEN);
                log = Math.log10(arg.doubleValue());
            }

            idf.add(i, log);
            //System.out.println(idf.get(i));
        }
        return idf;
    }

    //Получили число TfIdf для каждого документа
    public static void getTfIdf(BookProfile book, List<Double> idf) {
        ArrayList<Double> tfIdf = new ArrayList<Double>();

        for (int i = 0; i < idf.size(); i++) {
            double getCount = book.normTF.get(i);
            double temp = idf.get(i) * getCount;
            tfIdf.add(i, temp);
        }
        book.setTf_idf(tfIdf);
    }

    //Получили число W для каждого документа
    public static void getW(BookProfile book, List<Double> idf) {
        ArrayList<Double> w = new ArrayList<Double>();

        for (int i = 0; i < book.normTF.size(); i++) {

            //   double wi = (0.5 + book.normTF.get(i) * 0.5) * idf.get(i);
            //         double wi = (0.5 + book.normTF.get(i) * 0.5) * idf.get(i)/50;
            double wi = (book.normTF.get(i)) * idf.get(i);
            w.add(wi);
        }
        book.setW(w);
    }

    //Получили число W для каждого документа
    public static void alternativeW(BookProfile book, List<Double> idf) {
        ArrayList<Double> w = new ArrayList<Double>();

        for (int i = 0; i < book.normTF.size(); i++) {

            double wi = (0.5 + book.normTF.get(i) * 0.5) * idf.get(i);
            w.add(wi);
        }
        book.setW(w);
    }
}
