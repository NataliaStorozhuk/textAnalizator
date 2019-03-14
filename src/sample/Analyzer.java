package sample;


import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class Analyzer {
    int n = 3;

    private List<String> getAllTokensArray(ArrayList<String> d1Array, ArrayList<String> d2Array, ArrayList<String> qArray) {
        ArrayList<String> allArray = new ArrayList<String>();
        allArray.addAll(d1Array);
        allArray.addAll(d2Array);
        allArray.addAll(qArray);

        List<String> arrayAfterSort = allArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(arrayAfterSort);

        System.out.print(arrayAfterSort);
        System.out.print(arrayAfterSort.size());
        return arrayAfterSort;
    }

    private List<String> getAllTokensTwoDocs(ArrayList<String> d1Array, ArrayList<String> d2Array) {
        ArrayList<String> allArray = new ArrayList<String>();
        allArray.addAll(d1Array);
        allArray.addAll(d2Array);

        List<String> arrayAfterSort = allArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(arrayAfterSort);

        System.out.print(arrayAfterSort);
        System.out.print(arrayAfterSort.size());
        return arrayAfterSort;
    }


    private ArrayList<Double> getTfIdfQuery(List<String> arrayAfterSort, ArrayList<Integer> tdD1, ArrayList<Integer> tdD2, ArrayList<Integer> tdD3, ArrayList<Double> idf, ArrayList<String> QArray) {
        List<String> QArraySort = QArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(QArraySort);
        ArrayList<Integer> tdQ = getCountEachTokensInFile(QArray, QArraySort);
        ArrayList<Double> tfMaxTf = getTfMaxTf(tdQ);

        ArrayList<Integer> countQDocs = getQCountCocs(arrayAfterSort, tdD1, tdD2, tdD3, QArray, QArraySort);

        return getTfIdfQ(arrayAfterSort, idf, QArray, tfMaxTf, countQDocs);
    }

    private ArrayList<Double> getTfIdfQ(List<String> arrayAfterSort, ArrayList<Double> idf, ArrayList<String> QArray, ArrayList<Double> tfMaxTf, ArrayList<Integer> countQDocs) {
        ArrayList<Double> tfIdfQ = new ArrayList<>();

        for (int i = 0; i < countQDocs.size(); i++) {
            double temp=0.0;
            if (countQDocs.get(i) != 0) {

                for (int j = 0; j < arrayAfterSort.size(); j++) {
                    if (arrayAfterSort.get(j).equals(QArray.get(i))) {
                          temp=countQDocs.get(i).doubleValue() * tfMaxTf.get(i).doubleValue()*idf.get(j);
                        break;
                    }
                }

            }
            tfIdfQ.add(temp);
        }
        return tfIdfQ;
    }

    private ArrayList<Integer> getQCountCocs(List<String> arrayAfterSort, ArrayList<Integer> tdD1, ArrayList<Integer> tdD2, ArrayList<Integer> tdD3, ArrayList<String> QArray, List<String> QArraySort) {
        ArrayList<Integer> countQDocs = new ArrayList<>();
        for (int i = 0; i < QArraySort.size(); i++) {
            int temp = 0;
            for (int j = 0; j < arrayAfterSort.size(); j++) {
                if (arrayAfterSort.get(j).equals(QArray.get(i))) {

                    if (tdD1.get(j) != 0) temp++;
                    if (tdD2.get(j) != 0) temp++;
                    if (tdD3.get(j) != 0) temp++;
                    break;
                }
            }
            countQDocs.add(temp);
        }
        return countQDocs;
    }

    private ArrayList<Double> getTfMaxTf(ArrayList<Integer> tdQ) {
        ArrayList<Double> tfMaxTf = new ArrayList<>();
        Integer max = Collections.max(tdQ);
        for (int i = 0; i < tdQ.size(); i++) {
            tfMaxTf.add(tdQ.get(i).doubleValue() / max.doubleValue());
            System.out.println(tfMaxTf.get(i));
        }
        return tfMaxTf;
    }

     private ArrayList<CompareResults> getArraysNew(ArrayList<String> fileAfterPorter1, ArrayList<String> fileAfterPorter2,
                                                   ArrayList<String> fileAfterPorter3, ArrayList<String> qAfterPorter) {


        //формируем общий, сортируем, выкидываем повторы
        List<String> arrayAfterSort = getAllTokensArray(fileAfterPorter1, fileAfterPorter2, fileAfterPorter3);

        //считаем число каждого слова в файле
        ArrayList<Integer> tdD1 = getCountEachTokensInFile(fileAfterPorter1, arrayAfterSort);
        ArrayList<Integer> tdD2 = getCountEachTokensInFile(fileAfterPorter2, arrayAfterSort);
        ArrayList<Integer> tdD3 = getCountEachTokensInFile(fileAfterPorter3, arrayAfterSort);


        //считаем общее число файлов, в которых встречается слово
        ArrayList<Integer> documentFrequency = getCountFilesWithEveryWord(arrayAfterSort, tdD1, tdD2, tdD3);

        //считаем idf
        ArrayList<Double> idf = getIdf(documentFrequency);

        //получаем tf-idf
        ArrayList<Double> tfIdfD1 = getTfIdf(tdD1, idf);
        ArrayList<Double> tfIdfD2 = getTfIdf(tdD2, idf);
        ArrayList<Double> tfIdfD3 = getTfIdf(tdD3, idf);

             //Работаем с исходником
        //     ArrayList<Integer> tdQ = getCountEachTokensInFile(qAfterPorter, qAfterPorter);
        ArrayList<Double> tfIdfQ = getTfIdfQuery(arrayAfterSort, tdD1, tdD2, tdD3, idf, qAfterPorter);

        List<String> qAfterSort = qAfterPorter.stream().distinct().collect(Collectors.toList());
        Collections.sort(qAfterSort);

        CompareResults compareResults1=
                getCompareResults(fileAfterPorter1, qAfterPorter, arrayAfterSort, tfIdfD1, tfIdfQ, qAfterSort);

        //получаем результаты
    //    CompareResults compareResults1 = getCompareResults(tfIdfD1, tfIdfQ, tfIdfD1In2Skale, tfIdfQIn2Skale);
        CompareResults compareResults2 =
                getCompareResults(fileAfterPorter2, qAfterPorter, arrayAfterSort, tfIdfD2, tfIdfQ, qAfterSort);
              CompareResults compareResults3 =
                      getCompareResults(fileAfterPorter3, qAfterPorter, arrayAfterSort, tfIdfD2, tfIdfQ, qAfterSort);


         ArrayList<CompareResults> compareResults = new ArrayList<>();
         compareResults.add(compareResults1);
        compareResults.add(compareResults2);
        compareResults.add(compareResults3);
        return compareResults;
    }

    private CompareResults getCompareResults(ArrayList<String> fileAfterPorter1, ArrayList<String> qAfterPorter, List<String> arrayAfterSort, ArrayList<Double> tfIdfD1, ArrayList<Double> tfIdfQ, List<String> qAfterSort) {
        long start = System.currentTimeMillis();

        //формируем общий из 2x, сортируем, выкидываем повторы
        List<String> arrayTokensForTwo = getAllTokensTwoDocs(qAfterPorter, fileAfterPorter1);
        ArrayList<Double> tfIdfForDoc1  = getTfIgfForPairDoc(arrayAfterSort, tfIdfD1, arrayTokensForTwo);
        ArrayList<Double> tfIdfForDoc2  = getTfIgfForPairDoc(qAfterSort, tfIdfQ, arrayTokensForTwo);

        //получаем tf-idf в квадрате
        ArrayList<Double> tfIdfD1In2Skale = getTfIdfSkale(tfIdfForDoc1);
        ArrayList<Double> tfIdfQIn2Skale = getTfIdfSkale(tfIdfForDoc2);


        //получаем скалярное произведение
        Double getSkalar = getSkalar(tfIdfForDoc1, tfIdfForDoc2);

        //получаем эвклидово
        Double getEvklid = getEvklid(tfIdfForDoc1, tfIdfForDoc2);

        //получаем манхеттенское

        Double getManhetten = getManhetten(tfIdfForDoc1, tfIdfForDoc2);

        //получаем косинусное
        Double getCos = getCos(tfIdfD1In2Skale, tfIdfQIn2Skale, getSkalar);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;


        CompareResults compareResults = new CompareResults();
        compareResults.skalar = getSkalar;
        compareResults.cos = getCos;
        compareResults.evkl = getEvklid;
        compareResults.manh = getManhetten;
        compareResults.durability = timeConsumedMillis;
        return compareResults;
    }


    private ArrayList<Double> getTfIgfForPairDoc(List<String> arrayAfterSort, ArrayList<Double> tfIdfD1,
                                                 List<String> arrayTokensForTwo) {
        ArrayList<Double> tfIdfForDoc1 = new ArrayList<>();

        for (int i = 0; i < arrayTokensForTwo.size(); i++) {
            double temp = 0.0;
            for (int j = 0; j < arrayAfterSort.size(); j++) {
                if (arrayAfterSort.get(j).equals(arrayTokensForTwo.get(i))) {
                    temp= tfIdfD1.get(j);
                    break;
                }
            }
            tfIdfForDoc1.add(temp);
        }

        return tfIdfForDoc1;
    }

    private CompareResults getCompareResults(ArrayList<Double> tfIdfD1,
                                             ArrayList<Double> tfIdfD2,
                                             ArrayList<Double> tfIdfD1In2Skale,
                                             ArrayList<Double> tfIdfD2In2Skale) {

        long start = System.currentTimeMillis();

        //получаем скалярное произведение
        Double getSkalar = getSkalar(tfIdfD1, tfIdfD2);

        //получаем эвклидово
        Double getEvklid = getEvklid(tfIdfD1, tfIdfD2);

        //получаем манхеттенское

        Double getManhetten = getManhetten(tfIdfD1, tfIdfD2);

        //получаем косинусное
        Double getCos = getCos(tfIdfD1In2Skale, tfIdfD2In2Skale, getSkalar);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;


        CompareResults compareResults = new CompareResults();
        compareResults.skalar = getSkalar;
        compareResults.cos = getCos;
        compareResults.evkl = getEvklid;
        compareResults.manh = getManhetten;
        compareResults.durability = timeConsumedMillis;
        return compareResults;

    }

    private Double getCos(ArrayList<Double> tfIdfD1In2Skale, ArrayList<Double> tfIdfD2In2Skale, Double getSkalar) {
        Double getCos = 0.0;
        Double getChisl = Math.sqrt(getSkalar);
        Double getZnam = 0.0;
        Double getZnam1 = 0.0;
        Double getZnam1Sum = 0.0;
        Double getZnam2 = 0.0;
        Double getZnam2Sum = 0.0;

        for (int i = 0; i < tfIdfD1In2Skale.size(); i++) {
            getZnam1Sum += tfIdfD1In2Skale.get(i);
            getZnam2Sum += tfIdfD2In2Skale.get(i);
        }
        getZnam1 = Math.sqrt(getZnam1Sum);
        getZnam2 = Math.sqrt(getZnam2Sum);

        getZnam = getZnam1 * getZnam2;
        getCos = getChisl / getZnam;

        System.out.println("getCos=" + getCos);
        return getCos;
    }

    private Double getManhetten(ArrayList<Double> tfIdfD1, ArrayList<Double> tfIdfD2) {
        Double getManhetten = 0.0;

        for (int i = 0; i < tfIdfD1.size(); i++) {
            getManhetten += Math.abs((tfIdfD1.get(i) - tfIdfD2.get(i)));
        }
        System.out.println("getManhetten=" + getManhetten);
        return getManhetten;
    }

    private Double getEvklid(ArrayList<Double> tfIdfD1, ArrayList<Double> tfIdfD2) {
        Double getEvklid = 0.0;
        Double getSum = 0.0;

        for (int i = 0; i < tfIdfD1.size(); i++) {
            getSum += Math.pow((tfIdfD1.get(i) - tfIdfD2.get(i)), 2);
        }
        getEvklid = Math.sqrt(getSum);

        System.out.println("getSum=" + getSum);
        System.out.println("getEvklid=" + getEvklid);
        return getEvklid;
    }

    private Double getSkalar(ArrayList<Double> tfIdfD1, ArrayList<Double> tfIdfD2) {
        Double getSkalar = 0.0;
        Integer сountPairs = 0;

        for (int i = 0; i < tfIdfD1.size(); i++) {
            getSkalar += tfIdfD1.get(i) * tfIdfD2.get(i);
            if (tfIdfD1.get(i) != 0 && tfIdfD2.get(i) != 0)
                сountPairs++;
        }

        System.out.println("skalar=" + getSkalar);
        System.out.println("pair=" + сountPairs);
        return getSkalar;
    }

    private ArrayList<Double> getIdf(ArrayList<Integer> documentFrequency) {
        ArrayList<Double> idf = new ArrayList<>();
        for (int i = 0; i < documentFrequency.size(); i++) {

            //       http://pr0java.blogspot.com/2015/05/biginteger-bigdecimal_70.html
          /*  BigDecimal baseCountDocs = new BigDecimal(documentFrequency.get(i));
            BigDecimal nbN = new BigDecimal(n);
            BigDecimal b = baseCountDocs.divide(nbN, 5, BigDecimal.ROUND_HALF_EVEN);
*/
            //log a (b) = log c (b) / log c (a)

            BigDecimal baseCountDocs = new BigDecimal(documentFrequency.get(i));
            BigDecimal bdN = new BigDecimal(n);
            BigDecimal arg = bdN.divide(baseCountDocs, 5, BigDecimal.ROUND_HALF_EVEN);
            double log = Math.log(arg.doubleValue());
            //BigDecimalUtility bigDecimalUtility = new BigDecimalUtility();

         /*   double log1 = Math.log(doubleB);
            double log2 = Math.log(doubleA);
            double log3 = Math.abs(log1 / log2);*/
            idf.add(i, log);
            System.out.println(idf.get(i));
        }
        return idf;
    }

    private ArrayList<Integer> getCountFilesWithEveryWord(List<String> arrayAfterSort, ArrayList<Integer> tdD1,
                                                          ArrayList<Integer> tdD2, ArrayList<Integer> tdD3) {
        ArrayList<Integer> documentFrequency = new ArrayList<>();
        for (int i = 0; i < tdD1.size(); i++) {
            int temp = 0;
            if (tdD1.get(i) != 0)
                temp++;
            if (tdD2.get(i) != 0)
                temp++;
            if (tdD3.get(i) != 0)
                temp++;

            documentFrequency.add(i, temp);
            System.out.println(arrayAfterSort.get(i));
            System.out.println(documentFrequency.get(i));
        }
        return documentFrequency;
    }

    private Double getLength(ArrayList<Double> tfIdfD1In2Skale) {
        double sum = 0;
        for (int i = 0; i < tfIdfD1In2Skale.size(); i++) {
            sum += tfIdfD1In2Skale.get(i);
        }
        double sqrt = Math.sqrt(sum);
        return sqrt;
    }

    private ArrayList<Double> getTfIdf(ArrayList<Integer> tdDoc, ArrayList<Double> idf) {
        ArrayList<Double> tfIdfD1 = new ArrayList<Double>();

        for (int i = 0; i < idf.size(); i++) {
            double getCount = tdDoc.get(i).doubleValue();
            double temp = idf.get(i) * getCount;
            tfIdfD1.add(i, temp);
            System.out.println(tfIdfD1.get(i));
        }
        return tfIdfD1;
    }

    private ArrayList<Double> getTfIdfSkale(ArrayList<Double> idf) {
        ArrayList<Double> tfIdf = new ArrayList<Double>();
        for (int i = 0; i < idf.size(); i++) {
            double temp = idf.get(i) * idf.get(i);
            tfIdf.add(i, temp);
            System.out.println(tfIdf.get(i));
        }
        return tfIdf;
    }

    private ArrayList<Integer> getCountEachTokensInFile(ArrayList<String> d1Array, List<String> arrayAfterSort) {
        ArrayList<Integer> tf = new ArrayList<Integer>();
        for (int i = 0; i < arrayAfterSort.size(); i++) {
            String temp = arrayAfterSort.get(i);
            int numberOfElephants = (int) d1Array
                    .stream()
                    .filter(p -> p.equals(temp))
                    .count();

            tf.add(i, numberOfElephants);
            System.out.println(numberOfElephants);
            System.out.println(arrayAfterSort.get(i));
        }
//        System.out.println(tf);
        return tf;
    }
    public String getDocFromFileSystem(File file) {
        long start = System.currentTimeMillis();
        String s = usingBufferedReader(file.getPath());
        System.out.println(s);
        return s;
    }



    public ArrayList<String> getWordsWithoutStop(ArrayList<String> newS) {
        String stopWordsString = usingBufferedReader("src/resources/stop_words.txt");
        ArrayList<String> stopWords = getWordsFromString(stopWordsString);
        newS.removeAll(stopWords);
        return newS;
    }

    private static String usingBufferedReader(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }


    public ArrayList<String> getWordsAfterPorter(ArrayList<String> res) {
        ArrayList<String> afterPorter = new ArrayList<>();
        AlgorithmPorter algorithmPorter = new AlgorithmPorter();
        for (int i = 0; i < res.size(); i++) {
            String temp = algorithmPorter.stem(res.get(i));
            afterPorter.add(i, temp);
            System.out.println(temp);
        }
        return afterPorter;
    }

    public ArrayList<String> getWordsFromString(String q) {
        String s = q.replaceAll("[^а-яёА-Я a-zA-Z]", " ");
        s = s.toLowerCase();
        System.out.println(s);
        ArrayList<String> res = new ArrayList<String>(Arrays.asList(s.split("\\s+")));
        System.out.println(res);
        return res;
    }

    public ArrayList<CompareResults> getResults(ArrayList<String> fileAfterPorter1, ArrayList<String> fileAfterPorter2,
                                                ArrayList<String> fileAfterPorter3, ArrayList<String> fileAfterPorter4) {

        return (getArraysNew(fileAfterPorter1, fileAfterPorter2, fileAfterPorter3, fileAfterPorter4));

    }
}
