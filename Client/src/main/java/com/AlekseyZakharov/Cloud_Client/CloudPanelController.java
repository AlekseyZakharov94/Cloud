package com.AlekseyZakharov.Cloud_Client;

import com.AlekseyZakharov.Cloud_Common.FileInfo;
import com.AlekseyZakharov.Cloud_Server.ServerApp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CloudPanelController implements Initializable {

    public TableView<FileInfo> filesTable;
    public TextField pathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(120);
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
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
            };
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss");
        TableColumn<FileInfo, String> fileLastModifiedColumn = new TableColumn<>("last modified");
        fileLastModifiedColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileLastModifiedColumn.setPrefWidth(120);

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileLastModifiedColumn);

        updateFilesTable(Paths.get(ServerApp.getSourceDir()));
        filesTable.getSortOrder().add(fileTypeColumn);


    }

    public void updateFilesTable(Path path) {
        pathField.setText(path.normalize().toString());
        filesTable.getItems().clear();
        try {
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to update file list",
                    ButtonType.APPLY);
            alert.showAndWait();
        }
        filesTable.sort();
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (!upperPath.normalize().toString().equals(ServerApp.getSourceDir())) {
            updateFilesTable(upperPath);
        }
    }

    public void surfDirectories(MouseEvent mouseEvent) {
        Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem()
                .getFileName());
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

