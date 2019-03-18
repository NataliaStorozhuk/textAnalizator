package sample;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticGetter {

    public static List<String> getBaseFrequencies(ArrayList<Book> books) {

        //формируем общий, сортируем, выкидываем повторы
        List<String> arrayAfterSort = getAllTokensArray(books);

        //получаем tf - число каждой лексемы в книге
        for (Book book : books) {
            getTf(book, arrayAfterSort);
        }

        //считаем общее число файлов, в которых встречается слово
        ArrayList<Integer> documentFrequency = getDf(books);

        //считаем idf
        ArrayList<Double> idf = getIdf(documentFrequency, books.size());

        //получаем tf-idf
        for (Book book : books) {
            getTfIdf(book, idf);
        }

        return arrayAfterSort;

    }


    /*Метод делает из списка разных книг отсортированный массив лексем без дубликатов*/
    public static List<String> getAllTokensArray(ArrayList<Book> books) {
        ArrayList<String> allArray = new ArrayList<String>();
        for (Book book : books) {
            allArray.addAll(book.getLexems());
        }

        List<String> arrayAfterSort = allArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(arrayAfterSort);

        return arrayAfterSort;
    }

    /*Получает количество повторений каждой лексемы в книге tf */
    public static void getTf(Book book, List<String> arrayAfterSort) {
        ArrayList<Integer> tf = new ArrayList<Integer>();

        for (int i = 0; i < arrayAfterSort.size(); i++) {
            String temp = arrayAfterSort.get(i);
            int numberOfElephants = (int) book.getLexems()
                    .stream()
                    .filter(p -> p.equals(temp))
                    .count();

            tf.add(i, numberOfElephants);
        }

        book.setTf(tf);
    }

    //Получили список, который содержит количества файлов, в которых повторяются лексемы
    public static ArrayList<Integer> getDf(ArrayList<Book> books) {
        ArrayList<Integer> documentFrequency = new ArrayList<>();
        for (int i = 0; i < books.get(0).getTf().size(); i++) {
            int temp = 0;
            for (int j = 0; j < books.size(); j++) {
                if ((books.get(j).getTf().get(i)) != 0)
                    temp++;
            }
            documentFrequency.add(i, temp);
        }
        return documentFrequency;
    }

    //Пoлучили idf - логарифм (общее число доков/на число доков с каждой лексемой)
    public static ArrayList<Double> getIdf(ArrayList<Integer> documentFrequency, Integer booksSize) {
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
            System.out.println(idf.get(i));
        }
        return idf;
    }

    //Получили число TfIdf для каждого документа
    public static void getTfIdf(Book book, ArrayList<Double> idf) {
        ArrayList<Double> tfIdf = new ArrayList<Double>();

        for (int i = 0; i < idf.size(); i++) {
            double getCount = book.getTf().get(i).doubleValue();
            double temp = idf.get(i) * getCount;
            tfIdf.add(i, temp);
        }
        book.setTf_idf(tfIdf);
    }
}
