package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.Services.GenreService;

import java.io.IOException;
import java.util.List;

public class GenresController {

    private Stage stage;

    final VBox vbox = new VBox();

    final TableView<Genre> table = new TableView<Genre>();
    final Label label = new Label("Жанры");


    // final Label actionTaken = new Label();
    final TextField newName = new TextField();
    final TextField newPath = new TextField();
    // final CheckBox newRights = new CheckBox();
    final Button newAdd = new Button();
    final Button back = new Button();
    private ObservableList<Genre> genresData = FXCollections.observableArrayList();

    private final Image deleteImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Cancel-icon.png"
    );

    private final Image addImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Add-icon.png"
    );

    private final Image backImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Snapback-icon.png"
    );

    private final Image booksImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Favorite-icon.png"
    );

    private final Image lexemsImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Shuffle-On-icon.png"
    );

    private final Image reloadImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Play-Green-icon.png"
    );


    // инициализируем форму данными
    @FXML
    private void initialize() throws IOException {


        label.setFont(new Font("Arial", 20));
        initData();
        drawTable();

        final HBox hBox = getAddGenreBox();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hBox);
        VBox.setVgrow(table, Priority.ALWAYS);

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                GenreService genreService = new GenreService();
                Genre newGenre = new Genre(newName.getText(), newPath.getText());
                genreService.saveGenre(newGenre);
                genresData.add(newGenre);
                table.refresh();

            }
        });

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openAdminPage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private HBox getAddGenreBox() {
        final HBox hBox = new HBox();

        final ImageView buttonGraphicAdd = new ImageView();
        buttonGraphicAdd.setImage(addImage);
        newAdd.setGraphic(buttonGraphicAdd);

        final ImageView buttonGraphicBack = new ImageView();
        buttonGraphicBack.setImage(backImage);
        back.setGraphic(buttonGraphicBack);

        newName.setPromptText("Название");
        newPath.setPromptText("Путь к обучающей выборке");

        hBox.getChildren().addAll(back, newName, newPath, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private TableView drawTable() {

        table.setEditable(true);

        TableColumn idColumn = new TableColumn("ID жанра");
        TableColumn nameColumn = new TableColumn("Название");
        TableColumn pathColumn = new TableColumn("Путь к файлу");
        TableColumn indexedColumn = new TableColumn("Индекс");
        TableColumn studyColumn = new TableColumn("Обуч. выб.");
        TableColumn allBooksColumn = new TableColumn("Все");
        TableColumn<Genre, Genre> lexemesColumn = new TableColumn<>("Лексемы");
        TableColumn<Genre, Genre> booksColumn = new TableColumn<>("Книги");
        TableColumn<Genre, Genre> reloadColumn = new TableColumn<>("Дообучение");
        TableColumn<Genre, Genre> deleteColumn = new TableColumn<>("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<Genre, String>("idGenre"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Genre, String>("nameGenre"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<Genre, String>("filePath"));
        indexedColumnCreator(indexedColumn);
        studyColumnCreator(studyColumn);
        allBooksColumnCreator(allBooksColumn);

        lexemesColumnCreator(lexemesColumn);
        booksColumnCreator(booksColumn);
        reloadColumnCreator(reloadColumn);
        deleteColumnCreator(deleteColumn);

        table.setItems(genresData);

        table.getColumns().addAll(idColumn, nameColumn, pathColumn, indexedColumn, studyColumn, allBooksColumn, lexemesColumn, booksColumn, reloadColumn, deleteColumn);
        return table;
    }

    private void studyColumnCreator(TableColumn<Genre, Label> studyColumn) {
        studyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<sample.DBModels.Genre, Label>, ObservableValue<Label>>() {

            @Override
            public ObservableValue<Label> call(
                    TableColumn.CellDataFeatures<sample.DBModels.Genre, Label> arg0) {
                sample.DBModels.Genre genre = arg0.getValue();

                Label label = new Label();

                SessionFactory sessionFactory = new Configuration().configure()
                        .buildSessionFactory();
                Session session = sessionFactory.openSession();
                List<Book> books = session.createQuery("FROM Book book where book.training=true and book.idGenre =:testParam").setParameter("testParam", genre).list();
                label.setText(String.valueOf(books.size()));

                label.setAlignment(Pos.CENTER);

                return new SimpleObjectProperty<Label>(label);

            }

        });
    }

    private void allBooksColumnCreator(TableColumn<Genre, Label> allColumn) {
        allColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<sample.DBModels.Genre, Label>, ObservableValue<Label>>() {

            @Override
            public ObservableValue<Label> call(
                    TableColumn.CellDataFeatures<sample.DBModels.Genre, Label> arg0) {
                sample.DBModels.Genre genre = arg0.getValue();

                Label label = new Label();

                SessionFactory sessionFactory = new Configuration().configure()
                        .buildSessionFactory();
                Session session = sessionFactory.openSession();
                List<Book> books = session.createQuery("FROM Book book where book.idGenre =:testParam").setParameter("testParam", genre).list();
                label.setText(String.valueOf(books.size()));

                label.setAlignment(Pos.CENTER);

                return new SimpleObjectProperty<Label>(label);

            }

        });
    }

    private void indexedColumnCreator(TableColumn<Genre, Label> indexedColumn) {
        indexedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<sample.DBModels.Genre, Label>, ObservableValue<Label>>() {

            @Override
            public ObservableValue<Label> call(
                    TableColumn.CellDataFeatures<sample.DBModels.Genre, Label> arg0) {
                sample.DBModels.Genre genre = arg0.getValue();

                Label label = new Label();

                SessionFactory sessionFactory = new Configuration().configure()
                        .buildSessionFactory();
                Session session = sessionFactory.openSession();
                List<Book> books = session.createQuery("FROM Book book where book.indexed=true and book.idGenre =:testParam").setParameter("testParam", genre).list();
                label.setText(String.valueOf(books.size()));

                //     label.selectedProperty().setValue(user.getRights());

                label.setAlignment(Pos.CENTER);

                return new SimpleObjectProperty<Label>(label);

            }

        });
    }


    private void lexemesColumnCreator(TableColumn<Genre, Genre> lexemesColumn) {
        lexemesColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        lexemesColumn.setCellFactory(new Callback<TableColumn<Genre, Genre>, TableCell<Genre, Genre>>() {
            @Override
            public TableCell<Genre, Genre> call(TableColumn<Genre, Genre> lexemesColumn) {
                return new TableCell<Genre, Genre>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);

                    }

                    @Override
                    public void updateItem(final Genre person, boolean empty) {
                        openLexemes(person, empty);
                        //table.setItems(genresData);
                    }

                    private void openLexemes(Genre person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(lexemsImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {


                                    GenreInfoController controller = new GenreInfoController();
                                    controller.setGenre(person);
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genreInfo.fxml"));
                                    loader.setController(controller);
                                    AnchorPane page = null;
                                    try {
                                        page = (AnchorPane) loader.load();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    stage.setTitle("Лексемы жанра");

                                    Scene scene = new Scene(page);
                                    stage.setScene(scene);
                                    controller.setStage(stage);
                                    stage.show();

                                }
                            });
                        } else {
                            //   button.setText("error");
                        }     //не знаю почему, но тут все время person=null, когда таблицу обновляем
                    }
                };
            }
        });
    }

    private void booksColumnCreator(TableColumn<Genre, Genre> booksColumn) {
        booksColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        booksColumn.setCellFactory(new Callback<TableColumn<Genre, Genre>, TableCell<Genre, Genre>>() {
            @Override
            public TableCell<Genre, Genre> call(TableColumn<Genre, Genre> booksColumn) {
                return new TableCell<Genre, Genre>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);

                    }

                    @Override
                    public void updateItem(final Genre person, boolean empty) {
                        openBooks(person, empty);
                    }

                    private void openBooks(Genre person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(booksImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {


                                    //FXMLLoader loader = new FXMLLoader("/sample/EditBook.fxml");
                                    //   Parent root = loader.load();
                                   /* Main.primaryStage.setScene(new Scene(root));
                                    ControllerClass controllerEditBook = loader.getController(); //получаем контроллер для второй формы
                                    controllerEditBook.someMethod(someParameters); // передаем необходимые параметры
                                    Main.primaryStage.show();
*/
                                    //  Передаём адресата в контроллер.
                                    BooksController controller = new BooksController();
                                    controller.setGenre(person);
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/books.fxml"));
                                    loader.setController(controller);
                                    AnchorPane page = null;
                                    try {
                                        page = (AnchorPane) loader.load();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    stage.setTitle("Книги жанра");

                                    Scene scene = new Scene(page);
                                    stage.setScene(scene);
                                    controller.setStage(stage);
                                    stage.show();

                                }
                            });
                        } else {
                            //   button.setText("error");
                        }     //не знаю почему, но тут все время person=null, когда таблицу обновляем
                    }
                };
            }
        });
    }

    private void reloadColumnCreator(TableColumn<Genre, Genre> reloadColumn) {
        reloadColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        reloadColumn.setCellFactory(new Callback<TableColumn<Genre, Genre>, TableCell<Genre, Genre>>() {
            @Override
            public TableCell<Genre, Genre> call(TableColumn<Genre, Genre> reloadColumn) {
                return new TableCell<Genre, Genre>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);

                    }

                    @Override
                    public void updateItem(final Genre person, boolean empty) {
                        reloadTraining(person, empty);
                    }

                    private void reloadTraining(Genre person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(reloadImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {


                                    //TODO Тут запуск дообучения
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Дообучение завершено успешно!");

                                    // Header Text: null
                                    alert.setHeaderText(null);
                                    alert.setContentText("Окошко с дообучением");

                                    alert.showAndWait();

                                    initData();
                                    table.refresh();

                                }
                            });
                        } else {
                            //   button.setText("error");
                        }     //не знаю почему, но тут все время person=null, когда таблицу обновляем
                    }
                };
            }
        });
    }

    private void deleteColumnCreator(TableColumn<Genre, Genre> deleteColumn) {
        deleteColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        deleteColumn.setCellFactory(new Callback<TableColumn<Genre, Genre>, TableCell<Genre, Genre>>() {
            @Override
            public TableCell<Genre, Genre> call(TableColumn<Genre, Genre> deleteColumn) {
                return new TableCell<Genre, Genre>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);

                    }

                    @Override
                    public void updateItem(final Genre person, boolean empty) {
                        deleteGenre(person, empty);
                        table.setItems(genresData);
                    }

                    private void deleteGenre(Genre person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    ObservableList<Genre> genresData1 = FXCollections.observableArrayList();
                                    for (Genre genre : genresData) {
                                        if (genre.getIdGenre() != person.getIdGenre())
                                            genresData1.add(genre);
                                    }
                                    genresData = genresData1;
                                    table.refresh();
                                }
                            });
                        } else {
                            //   button.setText("error");
                        }     //не знаю почему, но тут все время person=null, когда таблицу обновляем
                    }
                };
            }
        });
    }

    // подготавливаем данные для таблицы
    // вы можете получать их с базы данных
    private void initData() {
        genresData.clear();
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Genre> genres = session.createQuery("FROM Genre").list();
        genresData.addAll(genres);
        session.close();
    }

    public void setStage(Stage stage) {

        this.stage = stage;
        stage.setScene(new Scene(vbox));

    }

    private void openAdminPage() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Анализ текста");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setStage(stage);
        controller.adminSettings.setVisible(true);
        stage.show();

    }

}
