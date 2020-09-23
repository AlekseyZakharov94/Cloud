package com.AlekseyZakharov.Cloud_Client;

import com.AlekseyZakharov.Cloud_Common.AuthorizationRequest;
import com.AlekseyZakharov.Cloud_Common.DeleteFileRequest;
import com.AlekseyZakharov.Cloud_Common.DownloadFileRequest;
import com.AlekseyZakharov.Cloud_Common.SendFileRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.stream.Stream;

public class MainController {

    public VBox cloudPanel;
    public VBox clientPanel;


    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void deleteFile(ActionEvent actionEvent) {
        ClientPanelController clientCtrl = (ClientPanelController) clientPanel.getProperties().get("ctrl");
        CloudPanelController cloudCtrl = (CloudPanelController) cloudPanel.getProperties().get("ctrl");
        if (checkSelection(clientCtrl, cloudCtrl)) {
            if (clientCtrl.getSelectedFileName() != null) {
                Path filePath = Paths.get(clientCtrl.getCurrentPath(), clientCtrl.getSelectedFileName());
                try {
                    if (Files.isDirectory(filePath)) {
                        try (Stream<Path> walk = Files.walk(filePath)) {
                            walk.sorted(Comparator.reverseOrder())
                                    .map(Path::toFile)
                                    .forEach(File::delete);
                        }
                    } else
                        Files.delete(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientCtrl.updateFilesTable(Paths.get(clientCtrl.getCurrentPath()));
            }
            if (cloudCtrl.getSelectedFileName() != null) {
                try {
                    SignInWindowController.network.
                            sendMessage(new DeleteFileRequest(cloudCtrl.getCurrentPath() + "\\" +
                                    cloudCtrl.getSelectedFileName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cloudCtrl.updateFilesTable(Paths.get(cloudCtrl.getCurrentPath()).normalize().toAbsolutePath());
            }
        }
    }

    private boolean checkSelection(ClientPanelController clientCtrl, CloudPanelController cloudCtrl) {
        if (clientCtrl.getSelectedFileName() == null && cloudCtrl.getSelectedFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select file to delete", ButtonType.APPLY);
            alert.showAndWait();
            return false;
        } else
            return true;
    }


    public void sendFile(ActionEvent actionEvent) {
        ClientPanelController clientCtrl = (ClientPanelController) clientPanel.getProperties().get("ctrl");
        CloudPanelController cloudCtrl = (CloudPanelController) cloudPanel.getProperties().get("ctrl");
        if (checkSelection(clientCtrl, cloudCtrl)) {
            if (clientCtrl.getSelectedFileName() != null) {
                Path filePath = Paths.get(clientCtrl.getCurrentPath() + "\\" +
                        clientCtrl.getSelectedFileName());
                try {
                    SignInWindowController.network.sendMessage(new SendFileRequest(clientCtrl.getSelectedFileName(),
                            Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cloudCtrl.updateFilesTable(Paths.get(cloudCtrl.getCurrentPath()).normalize().toAbsolutePath());
            }
        }
    }

    public void downLoadFile(ActionEvent actionEvent) {
        ClientPanelController clientCtrl = (ClientPanelController) clientPanel.getProperties().get("ctrl");
        CloudPanelController cloudCtrl = (CloudPanelController) cloudPanel.getProperties().get("ctrl");
        if (checkSelection(clientCtrl, cloudCtrl)) {
            if (cloudCtrl.getSelectedFileName() != null) {
                Path filePath = Paths.get(cloudCtrl.getCurrentPath() + "\\" +
                        cloudCtrl.getSelectedFileName());
                try {
                    SignInWindowController.network.sendMessage(new DownloadFileRequest(cloudCtrl.getSelectedFileName()));
                    Files.write(Paths.get(clientCtrl.getCurrentPath() + "\\" + cloudCtrl.getSelectedFileName()),
                            (byte[]) SignInWindowController.network.receiveMessage(),
                            StandardOpenOption.CREATE);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                clientCtrl.updateFilesTable(Paths.get(clientCtrl.getCurrentPath()));
            }
        }
    }
}

