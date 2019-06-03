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
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.DBModels.User;
import sample.Services.UserService;

import java.io.IOException;
import java.util.List;

public class UsersController extends ControllerConstructor {

    private Stage stage;

    private final VBox vbox = new VBox();

    private final TableView<sample.DBModels.User> table = new TableView<>();
    private final Label label = new Label("Пользователи");

    private final TextField newLogin = new TextField();
    private final TextField newPassword = new TextField();
    private final CheckBox newRights = new CheckBox();
    private final Button newAdd = new Button();
    private final Button back = new Button();
    private ObservableList<sample.DBModels.User> usersData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        label.setFont(new Font("Arial", 20));
        initData();

        drawTable();

        final HBox hBox = getAddBox();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hBox);
        VBox.setVgrow(table, Priority.ALWAYS);

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            User newUser = new User(newLogin.getText(), String.valueOf((newPassword.getText()).hashCode()), newRights.isSelected());
            UserService.saveUser(newUser);
            usersData.add(newUser);
            table.refresh();

        });

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            try {
                openAdminMainPage(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    private HBox getAddBox() {
        final HBox hBox = new HBox();

        newAdd.setGraphic(imageButtonAdd());
        back.setGraphic(imageButtonBack());

        newPassword.setPromptText("Пароль");
        newLogin.setPromptText("Логин");
        newRights.setText("Права");

        hBox.getChildren().addAll(back, newLogin, newPassword, newRights, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    void setStage(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(vbox));
    }

    private void drawTable() {

        table.setEditable(true);

        TableColumn idColumn = new TableColumn("ID пользователя");
        TableColumn loginColumn = new TableColumn("Логин");
        TableColumn rightsColumn = new TableColumn("Администратор");
        TableColumn<sample.DBModels.User, sample.DBModels.User> deleteColumn = new TableColumn<>("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<sample.DBModels.User, String>("idUser"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<sample.DBModels.User, String>("login"));
        rightsColumn.setCellValueFactory(new PropertyValueFactory<sample.DBModels.User, CheckBox>("rights"));
        rightsColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<User, CheckBox>, ObservableValue<CheckBox>>) arg0 -> {
            User user = arg0.getValue();

            CheckBox checkBox = new CheckBox();

            checkBox.selectedProperty().setValue(user.getRights());

            checkBox.setAlignment(Pos.CENTER);

            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {

                user.setRights(new_val);
                UserService.updateUser(user);
                initData();
                table.refresh();


            });

            return new SimpleObjectProperty<>(checkBox);

        });

        deleteColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        deleteColumn.setCellFactory(new Callback<TableColumn<sample.DBModels.User, sample.DBModels.User>,
                TableCell<sample.DBModels.User, sample.DBModels.User>>() {
            @Override
            public TableCell<sample.DBModels.User, sample.DBModels.User> call(TableColumn<sample.DBModels.User,
                    sample.DBModels.User> deleteColumn) {
                return new TableCell<sample.DBModels.User, sample.DBModels.User>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);
                    }

                    @Override
                    public void updateItem(final sample.DBModels.User person, boolean empty) {
                        deleteUser(person, empty);
                        table.setItems(usersData);
                    }

                    private void deleteUser(sample.DBModels.User person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(event -> {
                                UserService.deleteUser(UserService.findUser(person.getIdUser()));
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


        table.setItems(usersData);

        table.getColumns().addAll(idColumn, loginColumn, rightsColumn, deleteColumn);
    }

    // подготавливаем данные из базы данных
    private void initData() {
        usersData.clear();
        List<sample.DBModels.User> users = UserService.findAllUsers();
        usersData.addAll(users);
    }
}
