package com.AlekseyZakharov.Cloud_Client;

import com.AlekseyZakharov.Cloud_Common.FileInfo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientPanelController implements Initializable{

    public TableView<FileInfo> filesTable;
    public ComboBox<Path> disksBox;
    public TextField pathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(120);
        fileSizeColumn.setCellFactory(column -> new TableCell<FileInfo, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    String text = String.format("%, d bytes", item);
                    if (item == -1L) {
                        text = "[DIR]";
                    }
                    setText(text);
                }
            }
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss");
        TableColumn<FileInfo, String> fileLastModifiedColumn = new TableColumn<>("Время изменения");
        fileLastModifiedColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileLastModifiedColumn.setPrefWidth(120);

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileLastModifiedColumn);

        updateFilesTable(Paths.get("C:"));
        filesTable.getSortOrder().add(fileTypeColumn);

        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p);
        }
        disksBox.getSelectionModel().selectFirst();
    }

    public void updateFilesTable(Path path) {
        pathField.setText(path.normalize().toAbsolutePath().toString());
        filesTable.getItems().clear();
        try {
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось обновить список файлов", ButtonType.APPLY);
            alert.showAndWait();
        }
        filesTable.sort();
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<Path> element = (ComboBox<Path>) actionEvent.getSource();
        updateFilesTable(element.getSelectionModel().getSelectedItem());
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {
            updateFilesTable(upperPath);
        }
    }

    public void surfDirectories(MouseEvent mouseEvent) {
        Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getFileName());
        if ((mouseEvent.getClickCount() == 2) && (Files.isDirectory(path))) {
            updateFilesTable(path);
        }
    }

    public String getSelectedFileName() {
        if (!filesTable.isFocused()) {
            return null;
        } else {
            return filesTable.getSelectionModel().getSelectedItem().getFileName();
        }
    }

    public String getCurrentPath() {
        return pathField.getText();
    }
}
