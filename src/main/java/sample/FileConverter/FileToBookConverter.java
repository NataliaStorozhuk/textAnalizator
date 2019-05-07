package sample.FileConverter;

import org.testng.annotations.Test;
import sample.AlgorithmPorter;
import sample.DTO.BookProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileToBookConverter {

    /*Из файла прямо в объект типа BookProfile*/
    public static BookProfile getBookFromFile(File file) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> wordsFromString = getWordsFromString(s);

        ArrayList<String> wordsWithoutStop = getWordsWithoutStop(wordsFromString);
        ArrayList<String> fileAfterPorter = getWordsAfterPorter(wordsWithoutStop);

        BookProfile book = new BookProfile(file.getName(), fileAfterPorter);
        return book;
    }


    /*Из файла прямо в объект типа BookProfile БЕЗ ИМЕН*/
    public static BookProfile getBookFromFileWithoutNames(File file) {

        String s = usingBufferedReader(file.getPath());

        ArrayList<String> names = getNamesFromString(s);

        ArrayList<String> wordsFromString = getWordsFromString(s);
        //    System.out.println("Число слов с именами и слоп словами"+wordsFromString.size());

        ArrayList<String> wordsWithoutStop = getWordsWithoutStop(wordsFromString);
        //  System.out.println("Число слов с именами и БЕЗ стоп слов"+wordsWithoutStop.size());

        ArrayList<String> wordsAfterPorter = getWordsAfterPorter(wordsWithoutStop);
        ArrayList<String> wordsWithoutNames = getWordsWithoutNames(wordsAfterPorter, names);
        //  System.out.println("Число слов БЕЗ ИМЕН И СЛОП СЛОВ"+wordsWithoutNames.size());
        BookProfile book = new BookProfile(file.getName(), wordsWithoutNames);
        return book;
    }

    /*Считываем файл в строку*/
    public static String usingBufferedReader(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        //этол только для ресурсов подходит=(
        //     InputStream in = FileToBookConverter.class.getResourceAsStream(filePath);
        // BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //  try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }


    @Test
    public void testtest() {
        getNamesFromString("Энн ела рыбу Катю?! Рыбу готовила Энн.? Рыба была \"не очень\", впрочем и Энн тоже не очень" +
                "и Катя и Маша и НИКОЛЯ тоже такие себе повара. ");
    }

    /*Вырезать имена из строки*/
    public static ArrayList<String> getNamesFromString(String text) {

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

        //    System.out.println("Имена из файла!"+names);
        ArrayList<String> namesAfterPorter = getWordsAfterPorter(names);
        //   System.out.println("Имена из файла уже после алгоритма Портера"+namesAfterPorter);
        ArrayList<String> namesDistinct = (ArrayList<String>) namesAfterPorter.stream().distinct().collect(Collectors.toList());
        //   System.out.println("Имена из файла после удаления дубликатов"+namesDistinct);
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
        String stopWordsString = usingBufferedReader(FileToBookConverter.class.getResource("/stop_words.txt").getPath());
        ArrayList<String> stopWords = getWordsFromString(stopWordsString);
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
