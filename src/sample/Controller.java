package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static sample.FileConverter.FileToBookConverter.*;

//import java.awt.*;

public class Controller {


    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
    private String pathFileDetectives = desktopPath + "\\detectives\\";
    private String pathFileAnother = desktopPath + "\\another\\";
    Analyzer analyzer = new Analyzer();

    private Stage stage;
    @FXML
    private Button download1, download2, download3, download4, compare;

    @FXML
    private Label name1, name2, name3, name4, countLex1, countLex2, countLex3, countLex4,
            countStop1, countStop2, countStop3, countStop4, durabilityStem1, durabilityStem2,
            durabilityStem3, durabilityStem4;
    @FXML
    private Label skalar1, cos1, evkl1, manh1, durability1,
            skalar2, cos2, evkl2, manh2, durability2,
            skalar3, cos3, evkl3, manh3, durability3;

    ArrayList<String> fileAfterPorter1, fileAfterPorter2, fileAfterPorter3, fileAfterPorter4;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {
        download1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {

                    openFile1(file);
                }
            }
        });
        download2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    openFile2(file);
                }
            }
        });

        download3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    openFile3(file);
                }
            }
        });

        download4.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    openFile4(file);
                }
            }
        });


        compare.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //!!!
           /*     ArrayList<CompareResults> compareResults = analyzer.getResults(fileAfterPorter1, fileAfterPorter2,
                        fileAfterPorter3, fileAfterPorter4);

                skalar1.setText(compareResults.get(0).skalar.toString());
                cos1.setText(compareResults.get(0).cos.toString());
                durability1.setText(compareResults.get(0).durability.toString());


                cos2.setText(compareResults.get(1).cos.toString());
                durability2.setText(compareResults.get(1).durability.toString());

                skalar3.setText(compareResults.get(2).skalar.toString());
                cos3.setText(compareResults.get(2).cos.toString());
                durability3.setText(compareResults.get(2).durability.toString());*/
            }
        });
    }

    private void openFile1(File file) {

        String s = usingBufferedReader(file.getPath());
        ArrayList<String> newS = getWordsFromString(s);
        name1.setText(file.getName());
        countLex1.setText(String.valueOf(newS.size()));

        newS = getWordsWithoutStop(newS);
        countStop1.setText(String.valueOf(newS.size()));

        long start = System.currentTimeMillis();
        fileAfterPorter1 = getWordsAfterPorter(newS);
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        durabilityStem1.setText(String.valueOf(timeConsumedMillis));
    }

    private void openFile2(File file) {

        Analyzer analyzer = new Analyzer();
        String s = usingBufferedReader(file.getPath());
        name2.setText(file.getName());
        ArrayList<String> newS = getWordsFromString(s);
        countLex2.setText(String.valueOf(newS.size()));

        newS = getWordsWithoutStop(newS);
        countStop2.setText(String.valueOf(newS.size()));

        long start = System.currentTimeMillis();
        ArrayList<String> afterP = getWordsAfterPorter(newS);
        fileAfterPorter2 = afterP;
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        durabilityStem2.setText(String.valueOf(timeConsumedMillis));

    }

    private void openFile3(File file) {

        Analyzer analyzer = new Analyzer();
        String s = usingBufferedReader(file.getPath());
        name3.setText(file.getName());
        ArrayList<String> newS = getWordsFromString(s);
        countLex3.setText(String.valueOf(newS.size()));

        newS = getWordsWithoutStop(newS);
        countStop3.setText(String.valueOf(newS.size()));

        long start = System.currentTimeMillis();
        ArrayList<String> afterP = getWordsAfterPorter(newS);
        fileAfterPorter3 = afterP;
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        durabilityStem3.setText(String.valueOf(timeConsumedMillis));

    }

    private void openFile4(File file) {

        Analyzer analyzer = new Analyzer();
        String s = usingBufferedReader(file.getPath());
        name4.setText(file.getName());
        ArrayList<String> newS = getWordsFromString(s);
        countLex4.setText(String.valueOf(newS.size()));

        newS = getWordsWithoutStop(newS);
        countStop4.setText(String.valueOf(newS.size()));

        long start = System.currentTimeMillis();
        ArrayList<String> afterP = getWordsAfterPorter(newS);
        fileAfterPorter4 = afterP;
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        durabilityStem4.setText(String.valueOf(timeConsumedMillis));

    }

    public void setStageAndSetupListeners(Stage primaryStage) {

        stage = primaryStage;
    }

}
