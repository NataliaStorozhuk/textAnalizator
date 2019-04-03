package sample;

import sample.DTO.AllTokensClass;
import sample.DTO.BookProfile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticGetter {

    public AllTokensClass getBaseFrequencies(ArrayList<BookProfile> books) {

        AllTokensClass allTokensClass = new AllTokensClass();
        //формируем общий, сортируем, выкидываем повторы
        allTokensClass.arrayAfterSort = getAllTokensArray(books);

        //получаем tf - число каждой лексемы в книге
        for (BookProfile book : books) {
            getTf(book, allTokensClass.arrayAfterSort);
        }//долго

        //считаем общее число файлов, в которых встречается слово
        ArrayList<Integer> documentFrequency = getDf(books);

        //считаем idf
  //      allTokensClass.idf = getIdf(documentFrequency, books.size());
        if (books.size()!=1)
            allTokensClass.idf = getIdf(documentFrequency, books.size());
        else {
            for (int j=0; j<books.get(0).normTF.size(); j++){
                allTokensClass.idf.add(1.0);
            }
        }

        //получаем tf-idf
        for (BookProfile book : books) {
            getTfIdf(book, allTokensClass.idf);
        }

        //получаем w по модной формуле
        for (BookProfile book : books) {
            getW(book, allTokensClass.idf);
        }

        return allTokensClass;

    }


    /*Метод делает из списка разных книг отсортированный массив лексем без дубликатов*/
    public static List<String> getAllTokensArray(ArrayList<BookProfile> books) {
        ArrayList<String> allArray = new ArrayList<String>();
        for (BookProfile book : books) {
            allArray.addAll(book.getLexems());
        }

        List<String> arrayAfterSort = allArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(arrayAfterSort);

        return arrayAfterSort;
    }

    /*Получает количество повторений каждой лексемы в книге tf */
    public static void getTf(BookProfile book, List<String> arrayAfterSort) {
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

        double maxIdf = Collections.max(book.tf);

        ArrayList<Double> newTF = new ArrayList<Double>();
        for (int i = 0; i < arrayAfterSort.size(); i++) {

            BigDecimal bdDF = new BigDecimal(tf.get(i) );
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
    public static ArrayList<Integer> getDf(ArrayList<BookProfile> books) {
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

            double wi = (0.5 + book.normTF.get(i) * 0.5) * idf.get(i);
            w.add(wi);
        }
        book.setW(w);
    }
}
