package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Analizator.Analyzer;
import sample.DBModels.Genre;
import sample.DBModels.Info;
import sample.DTO.GenreProfile;
import sample.Services.GenreService;
import sample.Services.InfoService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static sample.FileConverter.JsonFileCreator.addNewBookJsonInDB;
import static sample.FileConverter.ObjectToJsonConverter.fromJsonToObject;


public class MainController extends ControllerConstructor {

    @FXML
    public TextField filePath;

    @FXML
    public ComboBox genreCombobox;

    @FXML
    public Button analysis;

    @FXML
    public Label skalarResult, cosResult, persentResult, textResult;

    @FXML
    public VBox adminSettings;
    @FXML
    public Button buttonUsers, buttonGenres, buttonSettings;

    private File file;
    private Analyzer analyser = new Analyzer();
    private List<Genre> genres;
    private Genre currentGenre;

    private Stage stage;
    @FXML
    private Button testButton;

    @FXML
    public void initialize() {

        comboboxSetItems();

        testButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                //получить файл который будем анализировать
                //превратить его в вектор
                GenreProfile bookProfile = analyser.getTestViborka(file.getPath());

                //получить характеристики из файла жанра
                //TODO подсунуть путь такой, какой будет на самом деле в файловой системе, из базы
                //получаем текущий жанр
                for (Genre genre : genres)
                    if (genre.getNameGenre().equals(genreCombobox.getValue())) {
                        currentGenre = genre;
                    }

                String filePath = currentGenre.getFilePath();
                GenreProfile genreProfile = (GenreProfile) fromJsonToObject(filePath, GenreProfile.class);

                //посчитать всякую инфу полезную

                Info info = InfoService.findInfo(1);

                //TODO вот тут вставить всякую модную обработку, если она, конечно, будет
                Double skalar = analyser.getSkalar(genreProfile, bookProfile);
                skalarResult.setText(skalar.toString());

                Double cos = analyser.getCos(genreProfile, bookProfile, skalar);
                cosResult.setText(cos.toString() + "(min=" + info.getPrecision() + ")");

                //вывести результат

                if (info.getPrecision() < cos) {
                    textResult.setText("Поздравляю! Книга " + file.getName() + " принадлежит к жанру " + currentGenre.getNameGenre());
                    addNewBookJsonInDB(applicationPath, currentGenre, file.getPath());
                } else
                    textResult.setText("Книга " + file.getName() + " НЕ принадлежит к жанру " + currentGenre.getNameGenre());

                persentResult.setText(String.valueOf((cos * 100)));

                //добавить книгу с параметрами в зависимости от результата
                //создать модный файл в фс с книгой этой

            }
        });

        filePath.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                final FileChooser fileChooser = new FileChooser();
                file = fileChooser.showOpenDialog(stage);
                filePath.setText(file.getPath());
            }
        });
        buttonGenres.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openGenrePage(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonSettings.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openSettings(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openUsers(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void comboboxSetItems() {
        genres = GenreService.findAllGenres();
        List<String> genresNames = new ArrayList<>();
        for (Genre genre : genres) genresNames.add(genre.getNameGenre());

        ObservableList<String> observableArrayList =
                FXCollections.observableArrayList(genresNames);
        genreCombobox.setItems(observableArrayList);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
