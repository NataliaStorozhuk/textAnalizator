package sample.Analizator;


import lombok.Setter;
import sample.DTO.BookProfile;
import sample.DTO.GenreProfile;
import sample.FileConverter.TxtToBookConverter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Setter
public class Analyzer {


    // Возведение в квадрат каждого элемента списка
    private ArrayList<Double> getDoubleListSkale(ArrayList<Double> idf) {
        ArrayList<Double> tfIdf = new ArrayList<>();
        for (int i = 0; i < idf.size(); i++) {
            double temp = idf.get(i) * idf.get(i);
            tfIdf.add(i, temp);
        }
        return tfIdf;
    }

    //получить косинусную меру сходства
    public Double getCos(GenreProfile studyViborka, GenreProfile testViborka, Double getSkalar) {

        CompletableFuture<ArrayList<Double>> completableFutureStudy = CompletableFuture.supplyAsync(() -> getDoubleListSkale((ArrayList<Double>) studyViborka.getWArray()));
        CompletableFuture<ArrayList<Double>> completableFutureTest = CompletableFuture.supplyAsync(() -> getDoubleListSkale((ArrayList<Double>) testViborka.getWArray()));

        ArrayList<Double> WstudyIn2Skale = completableFutureStudy.join();
        ArrayList<Double> WtestIn2Skale = completableFutureTest.join();

        Double getCos = 0.0;
        Double getChisl = getSkalar;
        Double getZnam = 0.0;
        Double getZnam1 = 0.0;
        Double getZnam1Sum = 0.0;
        Double getZnam2 = 0.0;
        Double getZnam2Sum = 0.0;

        for (Double aDouble : WstudyIn2Skale) {
            getZnam1Sum += aDouble;
        }

        for (Double aDouble : WtestIn2Skale) {
            getZnam2Sum += aDouble;
        }
        getZnam1 = Math.sqrt(getZnam1Sum);
        getZnam2 = Math.sqrt(getZnam2Sum);

        getZnam = getZnam1 * getZnam2;
        getCos = getChisl / getZnam;

        return getCos;
    }

    //получить скалярное произведение двух векторов
    public Double getSkalar(GenreProfile studyViborka, GenreProfile testViborka) {
        double getSkalar = 0.0;
        int сountPairs = 0;

        for (int j = 0; j < testViborka.genreLexemas.size(); j++) {

            for (int i = 0; i < studyViborka.genreLexemas.size(); i++) {

                if (studyViborka.genreLexemas.get(i).getLexema().equals(testViborka.genreLexemas.get(j).getLexema())) {
                    getSkalar += (studyViborka.genreLexemas.get(i).getW() * testViborka.genreLexemas.get(j).getW());
                    сountPairs++;
                    break;
                }

            }
        }

        return getSkalar;
    }


    //Получить скалярное произведение и косинусную меру сходства по новому  файлу и обучающей выборке
    public Double getFileCos(String pathName, GenreProfile studyViborka) {
        GenreProfile testViborka = getTestViborka(pathName);

        Double skalar = getSkalar(studyViborka, testViborka);
        Double cos = getCos(studyViborka, testViborka, skalar);
        System.out.println("skalar=" + skalar);
        System.out.println("cos=" + cos);
        return cos;
    }

    public GenreProfile getTestViborka(String pathName) {
        File file = new File(pathName);
        System.out.println(file.getPath());
        TxtToBookConverter fileToBookConverter = new TxtToBookConverter();
        BookProfile testBook1 = fileToBookConverter.getBookProfileFromTxt(file);

        ArrayList<BookProfile> testBookOnly = new ArrayList<>();
        testBookOnly.add(testBook1);

        GenreProfile testViborka = StatisticGetter.getBaseFrequencies(testBookOnly);
        for (int i = 0; i < testBook1.getW().size(); i++) {
            testViborka.genreLexemas.get(i).setW(testBook1.getW().get(i));
        }

        return testViborka;
    }

    public static ArrayList<Double> getAverageW(ArrayList<BookProfile> books) {
        ArrayList<Double> w = new ArrayList<Double>();
        for (int j = 0; j < books.get(0).getW().size(); j++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (BookProfile book : books) {
                BigDecimal getW_ = new BigDecimal(book.getW().get(j));
                sum = sum.add(getW_);
            }

            BigDecimal zero = new BigDecimal(0);
            BigDecimal n = new BigDecimal(books.size());
            if (!sum.equals(zero)) {
                zero = sum.divide(n, 5, BigDecimal.ROUND_HALF_EVEN);
            }
            w.add(zero.doubleValue());

        }
        return w;
    }
}
