package rollingcubes.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.tinylog.Logger;
import rollingcubes.results.GameResult;
import rollingcubes.results.GameResultRepository;
import util.javafx.ControllerHelper;
import util.javafx.FileChooserHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class HighScoreController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultRepository gameResultRepository;

    @FXML
    private TableView<GameResult> highScoreTable;

    @FXML
    private TableColumn<GameResult, String> player;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    @FXML
    private TableColumn<GameResult, Duration> duration;

    @FXML
    private TableColumn<GameResult, ZonedDateTime> created;

    @FXML
    private void initialize() {
        Logger.debug("Loading high scores...");

        List<GameResult> highScoreList = gameResultRepository.findBest(10);

        player.setCellValueFactory(new PropertyValueFactory<>("player"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));

        duration.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Duration item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DurationFormatUtils.formatDuration(item.toMillis(), "H:mm:ss"));
                }
            }
        });

        created.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        ControllerHelper.loadAndShowFXML(fxmlLoader, "/fxml/opening.fxml", stage);
    }

    public void handleSaveScoreboard(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooserHelper.show(false, stage)
                .ifPresent(file -> {
                    try {
                        gameResultRepository.saveToFile(file);
                    } catch (IOException ex) {
                        Logger.error(ex.getMessage());
                    }
                });

        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
    }
}
