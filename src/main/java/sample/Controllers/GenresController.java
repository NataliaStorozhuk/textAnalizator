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
import sample.DTO.BookProfile;
import sample.DTO.GenreProfile;
import sample.FileConverter.ObjectToJsonConverter;
import sample.Services.BookService;
import sample.Services.GenreService;
import sample.StatisticGetter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static sample.FileConverter.FileLoader.listFilesFromFolder;
import static sample.FileConverter.ObjectToJsonConverter.fromJsonToObject;

public class GenresController extends ControllerConstructor {

    private Stage stage;

    private final VBox vbox = new VBox();
    private File folder;
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

                String genrePath = (applicationPath + "genres\\");
                String booksPath = (applicationPath + "books\\" +
                        newName.getText());


                //Создаем жанр в базе сразу
                Genre newGenre = new Genre(newName.getText(), genrePath + newName.getText() + ".json");
                GenreService.saveGenre(newGenre);
                genresData.add(newGenre);
                table.refresh();

                //Получаем книги и пишем их в фс
                ArrayList<BookProfile> books = getBookProfiles(booksPath, newGenre, folder);

                //Получаем показатели все по выборке
                GenreProfile testViborka = StatisticGetter.getBaseFrequencies(books); //это очень долго бежит
                //   testViborka.setW(Analyzer.getAverageW(books));

                //Пишем в gson
                ObjectToJsonConverter.fromObjectToJson(applicationPath + "genres\\" +
                        newName.getText() + ".json", testViborka);


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Добавление жанра");

                // Header Text: null
                alert.setHeaderText(null);
                alert.setContentText("Файлы добавлены успешно!");

                alert.showAndWait();
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

    private ArrayList<BookProfile> getBookProfiles(String bookPath, Genre newGenre, File folder) {
        ArrayList<BookProfile> books = listFilesFromFolder(folder);

        File directory = new File(bookPath);

        if (!directory.exists()) {
            directory.mkdir();
        }

        for (BookProfile book : books) {
            String resultPath = (bookPath + "\\" + book.getName() + ".json");
            ObjectToJsonConverter.fromObjectToJson(resultPath, book);

            //записали в базочку
            Book newBook = new Book(book.name, resultPath, true, true, newGenre);
            BookService.saveBook(newBook);
        }
        return books;
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
                //Работа с окошком
                final DirectoryChooser directoryChooser = new DirectoryChooser();
                folder = directoryChooser.showDialog(stage);
                newPath.setText(folder.getPath());

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

                                List<Book> booksFromBase = BookService.getBooksWithGenre(person);
                                boolean tmp = true;
                                //Если все книги уже проиндексированы, то зачем ещё раз гонять
                                for (Book book : booksFromBase) {
                                    if (book.getIndexed() == false) {
                                        tmp = false;
                                        break;
                                    }
                                }
                                if (tmp == false) //если хотя бы одна - нет
                                {
                                    genreTraining(booksFromBase);
                                }

                                //TODO Тут запуск дообучения
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Окошко с дообучением");

                                // Header Text: null
                                alert.setHeaderText(null);
                                alert.setContentText("Дообучение завершено успешно!");

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

    private void genreTraining(List<Book> booksFromBase) {
        ArrayList<BookProfile> booksFromJson = new ArrayList<>();    //взяли книжечки исходные
        for (Book book : booksFromBase) {
            booksFromJson.add((BookProfile) fromJsonToObject(book.getFilePath(), BookProfile.class));
            book.setIndexed(true);
            BookService.updateBook(book);
        }

        //Получаем показатели все по выборке
        GenreProfile testViborka = StatisticGetter.getBaseFrequencies(booksFromJson); //это очень долго бежит
        //Пишем в gson
        ObjectToJsonConverter.fromObjectToJson(applicationPath + "genres\\" +
                newName.getText() + ".json", testViborka);
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
                                    GenreService.deleteGenre(GenreService.findGenre(person.getIdGenre()));
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
