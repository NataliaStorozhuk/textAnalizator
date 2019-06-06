package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.Services.BookService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static sample.FileConverter.JsonFileCreator.addNewBookJsonInDB;

public class BooksController extends ControllerConstructor {

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<Book> table = new TableView<>();
    private final Label label = new Label("Книги");

    private final TextField newPath = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();
    private ObservableList<Book> booksData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        label.setFont(new Font("Arial", 20));
        label.setText("Книги жанра " + currentGenre.getNameGenre());
        initData();

        drawTable();

        final HBox hBox = getAddBox();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hBox);
        VBox.setVgrow(table, Priority.ALWAYS);

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            try {
                openGenrePage(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }


    void setStage(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(vbox));

    }

    private HBox getAddBox() {
        final HBox hBox = new HBox();

        newAdd.setGraphic(imageButtonAdd());
        back.setGraphic(imageButtonBack());

        newPath.setPromptText("Путь к файлу");
        newPath.setMinWidth(500);
        newPath.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            newPath.setText(file.getPath());
        });

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Book newBook = addNewBookJsonInDB(applicationPath, currentGenre, newPath.getText());

            //обновили интерфейсик
            booksData.add(newBook);
            table.refresh();
            newPath.setText("");

        });

        hBox.getChildren().addAll(back, newPath, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }


    private void drawTable() {

        table.setEditable(true);

        TableColumn idColumn = new TableColumn("ID книги");
        TableColumn nameColumn = new TableColumn("Название");
        nameColumn.setMinWidth(200);
        TableColumn pathColumn = new TableColumn("Путь");
        TableColumn indexedColumn = new TableColumn("Индексирована");
        TableColumn trainingColumn = new TableColumn("Обуч. выб.");
        TableColumn<sample.DBModels.Book, sample.DBModels.Book> deleteColumn = new TableColumn<>("Удалить");

        idColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("idBook"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("filePath"));

        indexedColumn.setCellValueFactory(new PropertyValueFactory<Book, CheckBox>("indexed"));
        indexedColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>) arg0 -> {
            Book book = arg0.getValue();

            CheckBox checkBox = new CheckBox();

            checkBox.selectedProperty().setValue(book.getIndexed());

            checkBox.setAlignment(Pos.CENTER);

            return new SimpleObjectProperty<CheckBox>(checkBox);

        });

        trainingColumn.setCellValueFactory(new PropertyValueFactory<Book, CheckBox>("training"));
        trainingColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>) arg0 -> {
            Book book = arg0.getValue();

            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(book.getTraining());
            checkBox.setAlignment(Pos.CENTER);
            return new SimpleObjectProperty<>(checkBox);

        });

        deleteColumnCreator(deleteColumn);


        table.setItems(booksData);

        table.getColumns().addAll(idColumn, nameColumn, pathColumn, indexedColumn, trainingColumn, deleteColumn);
    }

    private void deleteColumnCreator(TableColumn<Book, Book> deleteColumn) {
        deleteColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        deleteColumn.setCellFactory(new Callback<TableColumn<Book, Book>,
                TableCell<Book, Book>>() {
            @Override
            public TableCell<Book, Book> call(TableColumn<Book,
                    Book> deleteColumn) {
                return new TableCell<Book, Book>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);
                    }

                    @Override
                    public void updateItem(final Book person, boolean empty) {
                        deleteBook(person, empty);
                        table.setItems(booksData);
                    }

                    private void deleteBook(Book person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(event -> {
                                BookService BookService = new BookService();
                                BookService.deleteBook(BookService.findBook(person.getIdBook()));
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

    // подготавливаем данные из базы данных
    private void initData() {

        booksData.clear();
        List<Book> books = BookService.getBooksWithGenre(currentGenre);
        booksData.addAll(books);
    }

    public void setGenre(Genre person) {
        currentGenre = person;
    }

}
