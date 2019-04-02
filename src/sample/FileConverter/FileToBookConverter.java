package sample.FileConverter;

import sample.AlgorithmPorter;
import sample.DTO.BookProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
        String sWithoutNames = cutNamesFromString(s);
        ArrayList<String> wordsFromString = getWordsFromString(sWithoutNames);

        ArrayList<String> wordsWithoutStop = getWordsWithoutStop(wordsFromString);
        ArrayList<String> fileAfterPorter = getWordsAfterPorter(wordsWithoutStop);

        BookProfile book = new BookProfile(file.getName(), fileAfterPorter);
        return book;
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
    public static String cutNamesFromString(String text) {

        String[] arr2 = text.replaceAll("\\b\\s+[A-ZА-Я][А-я\\w]+\\b", "").split("[!.?]");

        String resultString = "";
        for (String s : arr2) {
            resultString += s;
        }
        return resultString;
    }

    /*Получаем слова из строки*/
    public static ArrayList<String> getWordsFromString(String q) {
        String s = q.replaceAll("[^а-яёА-Я]", " ");
        s = s.toLowerCase();
        ArrayList<String> res = new ArrayList<String>(Arrays.asList(s.split("\\s+")));
        return res;
    }

    /*Удаляет стоп слова из списка*/
    public static ArrayList<String> getWordsWithoutStop(ArrayList<String> newS) {
        String stopWordsString = usingBufferedReader("src/resources/stop_words.txt");
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
