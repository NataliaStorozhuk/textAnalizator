package sample;


import lombok.Setter;
import sample.DTO.AllTokensClass;
import sample.DTO.BookProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static sample.FileConverter.FileToBookConverter.getBookFromFileWithoutNames;

@Setter
public class Analyzer {

    //тоже пока пусть лежит
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

    //пока непонятно, но вдруг пригодится
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

    // Возведение в квадрат каждого элемента списка
    private ArrayList<Double> getDoubleListSkale(ArrayList<Double> idf) {
        ArrayList<Double> tfIdf = new ArrayList<>();
        for (int i = 0; i < idf.size(); i++) {
            double temp = idf.get(i) * idf.get(i);
            tfIdf.add(i, temp);
            //      System.out.println(tfIdf.get(i));
        }
        return tfIdf;
    }

    //получить косинусную меру сходства
    public Double getCos(AllTokensClass studyViborka, AllTokensClass testViborka, Double getSkalar) {

        CompletableFuture<ArrayList<Double>> completableFutureStudy = CompletableFuture.supplyAsync(() -> getDoubleListSkale((ArrayList<Double>) studyViborka.getW()));
        CompletableFuture<ArrayList<Double>> completableFutureTest = CompletableFuture.supplyAsync(() -> getDoubleListSkale((ArrayList<Double>) testViborka.getW()));

        ArrayList<Double> WstudyIn2Skale = completableFutureStudy.join();
        ArrayList<Double> WtestIn2Skale = completableFutureTest.join();

        //   ArrayList<Double> WstudyIn2Skale = getDoubleListSkale((ArrayList<Double>) studyViborka.getW());
        // ArrayList<Double> WtestIn2Skale = getDoubleListSkale((ArrayList<Double>) testViborka.getW());
        Double getCos = 0.0;
        Double getChisl = Math.sqrt(getSkalar);
        Double getZnam = 0.0;
        Double getZnam1 = 0.0;
        Double getZnam1Sum = 0.0;
        Double getZnam2 = 0.0;
        Double getZnam2Sum = 0.0;

        for (int i = 0; i < WstudyIn2Skale.size(); i++) {
            getZnam1Sum += WstudyIn2Skale.get(i);
        }

        for (int i = 0; i < WtestIn2Skale.size(); i++) {
            getZnam2Sum += WtestIn2Skale.get(i);
        }
        getZnam1 = Math.sqrt(getZnam1Sum);
        getZnam2 = Math.sqrt(getZnam2Sum);

        //getZnam1 = getZnam1Sum;
        // getZnam2 = getZnam2Sum;

        getZnam = getZnam1 * getZnam2;
        getCos = getChisl / getZnam;

//        System.out.println("getCos=" + getCos);
        return getCos;
    }

    //получить скалярное произведение двух векторов
    public Double getSkalar(AllTokensClass studyViborka, AllTokensClass testViborka) {
        Double getSkalar = 0.0;
        Integer сountPairs = 0;

        for (int j = 0; j < testViborka.arrayAfterSort.size(); j++) {

            for (int i = 0; i < studyViborka.arrayAfterSort.size(); i++) {

                if (studyViborka.arrayAfterSort.get(i).equals(testViborka.arrayAfterSort.get(j))) {
                    getSkalar += (studyViborka.w.get(i) * testViborka.w.get(j));
                    сountPairs++;
                    break;
                }

            }
        }

        //     System.out.println("skalar=" + getSkalar);
        //   System.out.println("pair=" + сountPairs);
        return getSkalar;
    }


    //Получить скалярное произведение и косинусную меру сходства по новому  файлу и обучающей выборке
    public Double getFileCos(String pathName, AllTokensClass studyViborka) {
        File file = new File(pathName);
        System.out.println(file.getPath());
//        BookProfile testBook1 = getBookFromFile(file);
        BookProfile testBook1 = getBookFromFileWithoutNames(file);

        ArrayList<BookProfile> testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook1);
        AllTokensClass testViborka = sample.StatisticGetter.getBaseFrequencies(testBookOnly);
        testViborka.w = testBook1.getW();

        Double skalar = getSkalar(studyViborka, testViborka);
        Double cos = getCos(studyViborka, testViborka, skalar);
        System.out.println("skalar=" + skalar);
        System.out.println("cos=" + cos);
        return cos;
    }
}
