package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import sample.DAO.InfoDao;
import sample.DBModels.Info;
import sample.DBModels.Lexema;
import sample.FileConverter.FileCreator;
import sample.Services.InfoService;

import java.io.IOException;

public class SettingsController extends ControllerConstructor {

    private Stage stage;

    private final VBox vbox = new VBox();

    private final TableView<Lexema> table = new TableView<Lexema>();

    private final Label label = new Label("Настройки");

    private Info info;

    private final TextField newWord = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();

    private ObservableList<Lexema> wordsData = FXCollections.observableArrayList();

    private final Label textW = new Label();
    private final Button saveW = new Button();
    private final TextField w = new TextField();

    private final Label textPrecision = new Label();
    private final TextField precision = new TextField();
    private final Button savePrecision = new Button();

    @FXML
    public void initialize() {
        label.setFont(new Font("Arial", 20));
        initData();

        drawTable();

        final HBox hBox = getAddBox();

        final HBox wBox = getWBox();
        final HBox precisionBox = getPrecisionBox();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, wBox, precisionBox, table, hBox);
        VBox.setVgrow(table, Priority.ALWAYS);


        back.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            try {
                FileCreator.updateFileStopWords(wordsData, info.getStopWordsPath());
                openAdminMainPage(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    private HBox getAddBox() {

        final HBox hBox = new HBox();

        newAdd.setGraphic(imageButtonAdd());
        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            wordsData.add(new Lexema(newWord.getText()));
            table.refresh();

        });

        back.setGraphic(imageButtonBack());

        newWord.setPromptText("Стоп-слово");
        hBox.getChildren().addAll(back, newWord, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox getWBox() {
        final HBox hBox = new HBox();

        textW.setText("Коэффициент W ");

        w.setText(info.getCofW().toString());
        w.setEditable(true);

        saveW.setGraphic(imageButtonOk());

        saveW.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            info.setCofW(Double.valueOf(w.getText()));
            InfoService.updateInfo(info);
        });


        hBox.getChildren().addAll(textW, w, saveW);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private HBox getPrecisionBox() {
        final HBox hBox = new HBox();

        textPrecision.setText("Граничная мера");

        savePrecision.setGraphic(imageButtonOk());

        savePrecision.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if ((Double.valueOf(precision.getText()) > 1.0) || (Double.valueOf(precision.getText()) < 0.0)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Сохранение граничной меры!");

                alert.setHeaderText(null);
                alert.setContentText("Граничная мера должна находиться в диапазоне 0.0-1.0");

                alert.showAndWait();
            } else {
                info.setPrecision(Double.valueOf(precision.getText()));
                InfoService.updateInfo(info);
            }
        });


        precision.setText(info.getPrecision().toString());
        precision.setEditable(true);
        hBox.getChildren().addAll(textPrecision, precision, savePrecision);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(vbox));
    }

    private void drawTable() {


        table.setEditable(true);

        TableColumn wordColumn = new TableColumn("Слово");
        TableColumn<Lexema, Lexema> deleteColumn = new TableColumn<>("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        wordColumn.setCellValueFactory(new PropertyValueFactory<Lexema, String>("lexema"));

        deleteColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        deleteColumn.setCellFactory(new Callback<TableColumn<Lexema, Lexema>,
                TableCell<Lexema, Lexema>>() {
            @Override
            public TableCell<Lexema, Lexema> call(TableColumn<Lexema,
                    Lexema> deleteColumn) {
                return new TableCell<Lexema, Lexema>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);
                    }

                    @Override
                    public void updateItem(final Lexema person, boolean empty) {
                        deleteWord(person, empty);
                        table.setItems(wordsData);
                    }

                    private void deleteWord(Lexema person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(event -> {
                                wordsData.remove(person);
                                table.refresh();
                            });
                        } else {
                            //   button.setText("error");
                        }     //не знаю почему, но тут все время person=null, когда таблицу обновляем
                    }
                };
            }
        });


        table.setItems(wordsData);

        table.getColumns().addAll(wordColumn, deleteColumn);
    }

    // подготавливаем данные из базы данных
    private void initData() {

        info = InfoDao.findById(1);
        wordsData = FileCreator.getFromFileStopWords(info.getStopWordsPath());
    }
}
