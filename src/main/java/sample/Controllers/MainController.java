package sample.Controllers;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sample.Analyzer;
import sample.DBModels.Genre;
import sample.DBModels.Info;
import sample.DTO.Profile;
import sample.FileConverter.ObjectToJsonConverter;
import sample.Services.InfoService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainController {

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

    File file;
    private Analyzer analyser = new Analyzer();
    List<Genre> genres;
    Genre currentGenre;
    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
    private String pathFileDetectives = desktopPath + "\\detectives\\";
    private String pathFileAnother = desktopPath + "\\another\\";

    private Stage stage;
    @FXML
    private Button testButton;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {

        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        genres = session.createQuery("FROM Genre").list();
        List<String> genresNames = new ArrayList<>();
        for (Genre genre : genres) genresNames.add(genre.getNameGenre());
        genreCombobox.setItems((ObservableList) genresNames);

        testButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                //получить файл который будем анализировать
                //превратить его в вектор
                Profile testViborka = analyser.getTestViborka(file.getPath());

                //получить характеристики из файла жанра
                //TODO подсунуть путь такой, какой будет на самом деле в файловой системе, из базы
                //получаем текущий жанр
                for (Genre genre : genres)
                    if (genre.getNameGenre().equals(genreCombobox.getValue())) {
                        currentGenre = genre;
                    }

                Profile studyViborka = (Profile) ObjectToJsonConverter.fromJsonToObject(currentGenre.getFilePath(), Profile.class);

                //посчитать всякую инфу полезную
                //TODO вот тут вставить всякую модную обработку, если она, конечно, будет
                Double skalar = analyser.getSkalar(studyViborka, testViborka);
                skalarResult.setText(skalar.toString());

                Double cos = analyser.getCos(studyViborka, testViborka, skalar);
                cosResult.setText(cos.toString());

                //вывести результат
                Info info = InfoService.findInfo(1);
                if (info.getCofW() > cos) {
                    textResult.setText("Поздравляю! Книга " + file.getName() + "НЕ принадлежит к жанру " + currentGenre.getNameGenre());
                } else
                    textResult.setText("Книга " + file.getName() + "НЕ принадлежит к жанру " + currentGenre.getNameGenre());


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
                    openGenres();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonSettings.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openSettings();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openUsers();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void openGenres() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));

        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Жанры");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        GenresController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }

    private void openUsers() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/users.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Пользователи");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        UsersController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }

    private void openSettings() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Настроечки");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        SettingsController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }


}
