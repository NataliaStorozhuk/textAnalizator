package sample.FileConverter;

import sample.Analizator.AlgorithmPorter;
import sample.DTO.BookProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TxtToBookConverter {

    //Ходим по папке, собираем файлы в объекты BookProfile
    public static ArrayList<BookProfile> listBooksFromFolder(File folder) {

        ArrayList<BookProfile> books = new ArrayList<>();
        CompletableFuture[] futures = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    books.add(TxtToBookConverter.getBookProfileFromTxt(file));
                    return null;
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        return books;
    }


    /*Из файла прямо в объект типа BookProfile БЕЗ ИМЕН*/
    public static BookProfile getBookProfileFromTxt(File file) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> names = getNamesFromString(s);

        ArrayList<String> wordsFromString = getWordsFromString(s);
        //    System.out.println("Число слов с именами и слоп словами"+wordsFromString.size());

        ArrayList<String> wordsWithoutStop = getWordsWithoutStop(wordsFromString);
        //  System.out.println("Число слов с именами и БЕЗ стоп слов"+wordsWithoutStop.size());

        ArrayList<String> wordsAfterPorter = getWordsAfterPorter(wordsWithoutStop);
        ArrayList<String> wordsWithoutSFWords = getWordsWithoutSFWords(wordsAfterPorter);

        ArrayList<String> wordsWithoutNames = getWordsWithoutNames(wordsWithoutSFWords, names);
        //  System.out.println("Число слов БЕЗ ИМЕН И СЛОП СЛОВ"+wordsWithoutNames.size());
        BookProfile book = new BookProfile(file.getName(), wordsWithoutNames);
        return book;
    }

    private static ArrayList<String> getWordsWithoutSFWords(ArrayList<String> wordsAfterPorter) {
        ArrayList<String> result = wordsAfterPorter;

        String stopWordsString = usingBufferedReader((TxtToBookConverter.class.getResource("/stop_lexemas_words.txt").getPath()));
        ArrayList<String> stopWords = getWordsFromString(stopWordsString);

        result.removeAll(stopWords);
        return result;

    }

    /*Считываем файл в строку*/
    public static String usingBufferedReader(String filePath) {
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


    /*Вырезать имена из строки*/
    private static ArrayList<String> getNamesFromString(String text) {

        String regexp = "([А-ЯA-Z]((т.п.|т.д.|пр.)|[^?!.\\(]|\\([^\\)]*\\))*[.?!])";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);

        //тут последнее предложение обрубается, если нет знака препинания в конце!!!
        ArrayList<String> predl = new ArrayList<>();
        while (matcher.find()) {

            predl.add(Character.toLowerCase(matcher.group().charAt(0)) + matcher.group().substring(1));

        }

        //Имена - это все слова с большой, которые не стоят в начале предложения
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < predl.size(); i++) {

            String regexpNames = "\\b\\s+[A-ZА-Я][А-я\\w]+\\b";
            Pattern patternNames = Pattern.compile(regexpNames);
            Matcher matcherNames = patternNames.matcher(predl.get(i));

            while (matcherNames.find()) {
                names.add(matcherNames.group().toLowerCase().replaceAll("\\s+", ""));
            }
        }

        ArrayList<String> namesAfterPorter = getWordsAfterPorter(names);
        ArrayList<String> namesDistinct = (ArrayList<String>) namesAfterPorter.stream().distinct().collect(Collectors.toList());
        return namesDistinct;

    }

    /*Получаем слова из строки*/
    public static ArrayList<String> getWordsFromString(String q) {
        String s = q.replaceAll("[^а-яёА-Я]", " ");
        s = s.toLowerCase();
        ArrayList<String> res = new ArrayList<String>(Arrays.asList(s.split("\\s+")));
        return res;
    }

    private static ArrayList<String> getWordsWithoutNames(ArrayList<String> wordsFromString, ArrayList<String> names) {
        wordsFromString.removeAll(names);
        return wordsFromString;
    }

    /*Удаляет стоп слова из списка*/
    public static ArrayList<String> getWordsWithoutStop(ArrayList<String> newS) {
        String stopWordsString = usingBufferedReader((TxtToBookConverter.class.getResource("/stop_words.txt").getPath()));
        ArrayList<String> stopWords = getWordsFromString(stopWordsString);
        stopWords.add("");
        newS.removeAll(stopWords);
        return newS;
    }


    /*Обрабатываем слово с помощью алгоритма Портера*/
    public static ArrayList<String> getWordsAfterPorter(ArrayList<String> words) {
        ArrayList<String> afterPorter = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String temp = AlgorithmPorter.stem(words.get(i));
            afterPorter.add(i, temp);
        }
        return afterPorter;
    }

}
