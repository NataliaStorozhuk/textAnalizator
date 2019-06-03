package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.Services.BookService;
import sample.Services.GenreService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GenresController extends ControllerConstructor {

    private Stage stage;

    private final VBox vbox = new VBox();

    private final TableView<Genre> table = new TableView<Genre>();
    private final Label label = new Label("Жанры");


    private final TextField newName = new TextField();
    private final TextField newPath = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();
    private ObservableList<Genre> genresData = FXCollections.observableArrayList();


    // инициализируем форму данными
    @FXML
    private void initialize() {


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
                    openAdminMainPage(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private HBox getAddGenreBox() {
        final HBox hBox = new HBox();

        newAdd.setGraphic(imageButtonAdd());
        back.setGraphic(imageButtonBack());

        newName.setPromptText("Название");
        newPath.setPromptText("Путь к обучающей выборке");
        newPath.setMinWidth(400);

        newPath.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                final DirectoryChooser fileChooser = new DirectoryChooser();
                File file = fileChooser.showDialog(stage);
                newPath.setText(file.getPath());
            }
        });

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
        studyColumn.setCellValueFactory(arg0 -> {
            Genre genre = arg0.getValue();

            Label label = new Label();

            List<Book> books = BookService.getTrainingBooksWithGenre(genre);
            label.setText(String.valueOf(books.size()));

            label.setAlignment(Pos.CENTER);

            return new SimpleObjectProperty<Label>(label);

        });
    }

    private void allBooksColumnCreator(TableColumn<Genre, Label> allColumn) {
        allColumn.setCellValueFactory(arg0 -> {
            Genre genre = arg0.getValue();

            Label label = new Label();

            List<Book> books = BookService.getBooksWithGenre(genre);

            label.setText(String.valueOf(books.size()));

            label.setAlignment(Pos.CENTER);

            return new SimpleObjectProperty<>(label);

        });
    }

    private void indexedColumnCreator(TableColumn<Genre, Label> indexedColumn) {
        indexedColumn.setCellValueFactory(arg0 -> {
            Genre genre = arg0.getValue();

            Label label = new Label();
            List<Book> books = BookService.getIndexedBooksWithGenre(genre);
            label.setText(String.valueOf(books.size()));

            label.setAlignment(Pos.CENTER);

            return new SimpleObjectProperty<Label>(label);

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
                    }

                    private void openLexemes(Genre person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(lexemsImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {

                                    openLexemesPage(person, stage);

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
                                    openBookPage(person, stage);

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
                            button.setOnAction(event -> {

                                //TODO Тут запуск дообучения
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Дообучение завершено успешно!");

                                // Header Text: null
                                alert.setHeaderText(null);
                                alert.setContentText("Окошко с дообучением");

                                alert.showAndWait();

                                initData();
                                table.refresh();

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

    private void initData() {
        genresData.clear();
        List<Genre> genres = GenreService.findAllGenres();
        genresData.addAll(genres);
    }

    void setStage(Stage stage) {

        this.stage = stage;
        stage.setScene(new Scene(vbox));

    }

}
