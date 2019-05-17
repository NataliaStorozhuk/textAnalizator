package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.User;

import java.io.IOException;

public class GenresController {

    private Stage stage;

    @FXML
    private Button back;

    @FXML
    private Button genreInfo;

    private ObservableList<User> usersData = FXCollections.observableArrayList();

    // инициализируем форму данными
    @FXML
    private void initialize() throws IOException {
        initData();

        final TableView<User> table = new TableView<>();

        TableColumn idColumn = new TableColumn("ID пользователя");
        TableColumn loginColumn = new TableColumn("Логин controller");
        TableColumn passwordColumn = new TableColumn("Пароль controller");
        TableColumn rightsColumn = new TableColumn("Права controller");
        TableColumn deleteColumn = new TableColumn("Настройки controller");

        // устанавливаем тип и значение которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        rightsColumn.setCellValueFactory(new PropertyValueFactory<>("rights"));
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));


        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
                    @Override
                    public TableCell call(final TableColumn<User, String> param) {
                        final TableCell<User, String> cell = new TableCell<User, String>() {

                            final Button btn = new Button("Just Do It");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        User person = getTableView().getItems().get(getIndex());
                                        System.out.println(((User) person).getLogin()
                                                + "   " + person.getPassword());

                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Test Connection");

                                        // Header Text: null
                                        alert.setHeaderText(null);
                                        alert.setContentText("Второе окошко открылось и все!!");

                                        alert.showAndWait();

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        deleteColumn.setCellFactory(cellFactory);
        // заполняем таблицу данными
        table.setItems(usersData);

        table.getColumns().addAll(idColumn, loginColumn, passwordColumn, rightsColumn, deleteColumn);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));


        AnchorPane page = (AnchorPane) loader.load();

        Scene scene = new Scene(page);

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();
    }

    // подготавливаем данные для таблицы
    // вы можете получать их с базы данных
    private void initData() {
        usersData.add(new User(1, "Alex", "qwerty", true));
        usersData.add(new User(2, "Bob", "dsfsdfw", true));
        usersData.add(new User(3, "Jeck", "dsfdsfwe", true));
        usersData.add(new User(4, "Mike", "iueern", true));
        usersData.add(new User(5, "colin", "woeirn", true));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
