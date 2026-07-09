package com.smartorganizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);
    private final OrganizerService organizerService = new OrganizerService();
    private Path selectedFolder = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart File Organizer");

        Label titleLabel = new Label("Smart File Organizer");
        titleLabel.setStyle("-font-size: 20px; -fx-font-weight: bold;");

        Label folderLabel = new Label("Folder:");
        TextField folderPathField = new TextField();
        folderPathField.setEditable(false);
        folderPathField.setPrefWidth(300);

        Button browseButton = new Button("Browse");
        Button organizeButton = new Button("Organize");
        Button undoButton = new Button("Undo");
        Label statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: gray;");

        browseButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                selectedFolder = selectedDirectory.toPath();
                folderPathField.setText(selectedFolder.toAbsolutePath().toString());
                statusLabel.setText("Folder selectat cu succes.");
                statusLabel.setStyle("-fx-text-fill: green;");
            }
        });

        organizeButton.setOnAction(e -> {
            if (selectedFolder == null) {
                statusLabel.setText("Eroare: Vă rugăm să selectați mai întâi un folder!");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            try {
                statusLabel.setText("Se organizează...");
                organizerService.organizeFolder(selectedFolder);
                statusLabel.setText("Organizare completă cu succes!");
                statusLabel.setStyle("-fx-text-fill: green;");
            } catch (Exception ex) {
                statusLabel.setText("Eroare la organizare! Verificați permisiunile.");
                statusLabel.setStyle("-fx-text-fill: red;");
                logger.error("Eroare în timpul rulării interfeței grafice la sortare", ex);
            }
        });

        undoButton.setOnAction(e -> {
            if (selectedFolder == null) {
                statusLabel.setText("Eroare: Vă rugăm să selectați folderul pentru a face Undo!");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            try {
                statusLabel.setText("Se procesează Undo...");
                organizerService.undoLastOperation(selectedFolder);
                statusLabel.setText("Operațiunea a fost anulată cu succes!");
                statusLabel.setStyle("-fx-text-fill: blue;");
            } catch (Exception ex) {
                statusLabel.setText("Eroare la executarea operațiunii Undo.");
                statusLabel.setStyle("-fx-text-fill: red;");
                logger.error("Eroare în timpul rulării interfeței grafice la Undo", ex);
            }
        });

        HBox fileSelectionBox = new HBox(10, folderLabel, folderPathField, browseButton);
        fileSelectionBox.setAlignment(Pos.CENTER);

        HBox actionButtonBox = new HBox(20, organizeButton, undoButton);
        actionButtonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, titleLabel, fileSelectionBox, actionButtonBox, statusLabel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(450, 250);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}