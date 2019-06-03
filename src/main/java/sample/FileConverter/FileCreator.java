package sample.FileConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DTO.Lexema;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static sample.FileConverter.FileToBookConverter.getWordsFromString;
import static sample.FileConverter.FileToBookConverter.usingBufferedReader;

public class FileCreator {

    public static void updateFileStopWords(ObservableList<Lexema> lexemas, String pathFile) throws IOException {
        FileWriter nFile = new FileWriter(pathFile);

        for (Lexema lexema : lexemas) {
            nFile.write(lexema.getLexema() + "\n");

        }
        nFile.close();
    }

    public static ObservableList<Lexema> getFromFileStopWords(String pathFile) {
        ObservableList<Lexema> wordsData = FXCollections.observableArrayList();

        String stopWordsString = usingBufferedReader(pathFile);
        ArrayList<String> stopWords = getWordsFromString(stopWordsString);
        for (
                int i = 0; i < stopWords.size(); i++) {
            wordsData.add(new Lexema(stopWords.get(i)));
        }
        return wordsData;
    }
}