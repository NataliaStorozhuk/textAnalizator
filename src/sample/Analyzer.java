package sample;


import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class Analyzer {

  /*  public List<String> getSimiliarity(ArrayList<Book> books) {

        //Работаем с исходником
        //     ArrayList<Integer> tdQ = getTf(qAfterPorter, qAfterPorter);
       ArrayList<Double> tfIdfQ = getTfIdfQuery(arrayAfterSort, books, idf, bookRequest);

        List<String> qAfterSort = qAfterPorter.stream().distinct().collect(Collectors.toList());
        Collections.sort(qAfterSort);

        CompareResults compareResults1 =
                getCompareResults(fileAfterPorter1, qAfterPorter, arrayAfterSort, tfIdfD1, tfIdfQ, qAfterSort);

        return compareResults;
    }*/



    /*  private ArrayList<Double> getTfIdfQuery(List<String> arrayAfterSort, ArrayList<Integer> tdD1, ArrayList<Integer> tdD2, ArrayList<Integer> tdD3, ArrayList<Double> idf, ArrayList<String> QArray) {
        List<String> QArraySort = QArray.stream().distinct().collect(Collectors.toList());
        Collections.sort(QArraySort);
        ArrayList<Integer> tdQ = getTf(QArray, QArraySort);
        ArrayList<Double> tfMaxTf = getTfMaxTf(tdQ);

        ArrayList<Integer> countQDocs = getQCountCocs(arrayAfterSort, tdD1, tdD2, tdD3, QArray, QArraySort);

        return getTfIdfQ(arrayAfterSort, idf, QArray, tfMaxTf, countQDocs);
    }*/

    private ArrayList<Double> getTfIdfQ(List<String> arrayAfterSort, ArrayList<Double> idf, ArrayList<String> QArray, ArrayList<Double> tfMaxTf, ArrayList<Integer> countQDocs) {
        ArrayList<Double> tfIdfQ = new ArrayList<>();

        for (int i = 0; i < countQDocs.size(); i++) {
            double temp = 0.0;
            if (countQDocs.get(i) != 0) {

                for (int j = 0; j < arrayAfterSort.size(); j++) {
                    if (arrayAfterSort.get(j).equals(QArray.get(i))) {
                        temp = countQDocs.get(i).doubleValue() * tfMaxTf.get(i).doubleValue() * idf.get(j);
                        break;
                    }
                }

            }
            tfIdfQ.add(temp);
        }
        return tfIdfQ;
    }

    private CompareResults getCompareResults(ArrayList<String> fileAfterPorter1, ArrayList<String> qAfterPorter, List<String> arrayAfterSort, ArrayList<Double> tfIdfD1, ArrayList<Double> tfIdfQ, List<String> qAfterSort) {
        long start = System.currentTimeMillis();


        //формируем общий из 2x, сортируем, выкидываем повторы
     /*   List<String> arrayTokensForTwo = getAllTokensArray(qAfterPorter, fileAfterPorter1);
        ArrayList<Double> tfIdfForDoc1 = getTfIgfForPairDoc(arrayAfterSort, tfIdfD1, arrayTokensForTwo);
        ArrayList<Double> tfIdfForDoc2 = getTfIgfForPairDoc(qAfterSort, tfIdfQ, arrayTokensForTwo);

        //получаем tf-idf в квадрате
        ArrayList<Double> tfIdfD1In2Skale = getTfIdfSkale(tfIdfForDoc1);
        ArrayList<Double> tfIdfQIn2Skale = getTfIdfSkale(tfIdfForDoc2);


        //получаем скалярное произведение
        Double getSkalar = getSkalar(tfIdfForDoc1, tfIdfForDoc2);

        //получаем косинусное
        Double getCos = getCos(tfIdfD1In2Skale, tfIdfQIn2Skale, getSkalar);

        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;

*/
        CompareResults compareResults = new CompareResults();
       /* compareResults.skalar = getSkalar;
        compareResults.cos = getCos;
        compareResults.durability = timeConsumedMillis;*/
        return compareResults;
    }


    private ArrayList<Double> getTfIgfForPairDoc(List<String> arrayAfterSort, ArrayList<Double> tfIdfD1,
                                                 List<String> arrayTokensForTwo) {
        ArrayList<Double> tfIdfForDoc1 = new ArrayList<>();

        for (int i = 0; i < arrayTokensForTwo.size(); i++) {
            double temp = 0.0;
            for (int j = 0; j < arrayAfterSort.size(); j++) {
                if (arrayAfterSort.get(j).equals(arrayTokensForTwo.get(i))) {
                    temp = tfIdfD1.get(j);
                    break;
                }
            }
            tfIdfForDoc1.add(temp);
        }

        return tfIdfForDoc1;
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


    public static Double getSkalar(AllTokensClass studyViborka, AllTokensClass testViborka) {
        Double getSkalar = 0.0;
        Integer сountPairs = 0;

        for (int i = 0; i < studyViborka.arrayAfterSort.size(); i++) {

            for (int j = 0; j < testViborka.arrayAfterSort.size(); j++) {

                if (studyViborka.arrayAfterSort.get(i).equals(testViborka.arrayAfterSort.get(j))) {
                    getSkalar += (studyViborka.w.get(i) * testViborka.w.get(j));
                    сountPairs++;
                    break;
                }

            }
        }

        System.out.println("skalar=" + getSkalar);
        System.out.println("pair=" + сountPairs);
        return getSkalar;
    }

    public static Double getSkalarWithDetectiveWords(AllTokensClass studyViborka, AllTokensClass testViborka, ArrayList<String> words) {
        Double getSkalar = 0.0;
        Integer сountPairs = 0;

        for (int i = 0; i < studyViborka.arrayAfterSort.size(); i++) {

            for (int j = 0; j < testViborka.arrayAfterSort.size(); j++) {

                if (studyViborka.arrayAfterSort.get(i).equals(testViborka.arrayAfterSort.get(j))) {

                    for (int l = 0; l < words.size(); l++) {
                        if (words.get(l).equals(studyViborka.arrayAfterSort.get(i))){}
                    }

                    getSkalar += (studyViborka.w.get(i) * testViborka.w.get(j));
                    сountPairs++;
                    break;
                }

            }
        }

        System.out.println("skalar=" + getSkalar);
        System.out.println("pair=" + сountPairs);
        return getSkalar;
    }
}
