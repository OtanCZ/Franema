package otan.franema.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import otan.franema.FranemaApplication;

public class CinemaInspectController {
    public BorderPane cinemaInspectPanelPane;
    public Button editButton = new Button();
    public Button deleteButton = new Button();
    public TextField nameField = new TextField();
    public TextArea addressField = new TextArea();
    public Button exitButton = new Button();
    public VBox menu = new VBox();
    public VBox nameBox = new VBox();
    public Label nameLabel = new Label();
    public VBox addressBox = new VBox();
    public Label addressLabel = new Label();
    public HBox buttons = new HBox();

    @FXML
    public void initialize() {
        System.out.println("CinemaInspectController initialized!");
        nameField.setPromptText("Franta cinema");
        nameField.editableProperty().setValue(false);
        nameLabel.setText("Cinema name:");
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER);

        addressField.setPromptText("Franta street 1, 12345 Frantovice");
        addressField.editableProperty().setValue(false);
        addressLabel.setText("Address:");
        addressBox.getChildren().addAll(addressLabel, addressField);
        addressBox.setAlignment(Pos.CENTER);

        nameField.setText(FranemaApplication.appProvider.getCurrentCinema().getName());
        addressField.setText(FranemaApplication.appProvider.getCurrentCinema().getAddress());


        editButton.setText("Edit");
        editButton.setOnMouseClicked(this::editButtonOnMouseClick);

        exitButton.setText("Close");
        exitButton.setOnMouseClicked(this::exitButtonOnMouseClick);

        deleteButton.setText("Delete (this also deletes all movies and tickets!)");
        deleteButton.setOnMouseClicked(this::deleteButtonOnMouseClick);

        buttons.getChildren().add(exitButton);
        if(FranemaApplication.appProvider.getCurrentUser().isAdmin()) {
            buttons.getChildren().addAll(editButton, deleteButton);
        }
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        menu.getChildren().addAll(nameBox, addressBox, buttons);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        cinemaInspectPanelPane.setCenter(menu);

        if(FranemaApplication.appProvider.getCurrentCinema().getId() == 0){
           nameField.editableProperty().setValue(true);
           addressField.editableProperty().setValue(true);
           editButton.setText("Save");
           editButton.setOnMouseClicked(this::saveButtonOnMouseClick);
        }


        cinemaInspectPanelPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            menu.setPrefWidth(cinemaInspectPanelPane.getWidth());
            if (cinemaInspectPanelPane.getWidth() > 500) {
                addressField.setMaxWidth(cinemaInspectPanelPane.getWidth() / 2);
                nameField.setMaxWidth(cinemaInspectPanelPane.getWidth() / 2);
            } else {
                addressField.setMaxWidth(cinemaInspectPanelPane.getWidth());
                nameField.setMaxWidth(cinemaInspectPanelPane.getWidth());
            }
        });
        cinemaInspectPanelPane.heightProperty().addListener((observable, oldValue, newValue) -> menu.setPrefHeight(cinemaInspectPanelPane.getHeight()));
    }

    private void deleteButtonOnMouseClick(MouseEvent mouseEvent) {
        FranemaApplication.appProvider.deleteCinema(FranemaApplication.appProvider.getCurrentCinema());
        FranemaApplication.stageManager.closePopup((Stage) cinemaInspectPanelPane.getScene().getWindow());
        try {
            FranemaApplication.stageManager.showScene(FranemaApplication.stageManager.getCurrentScene());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void exitButtonOnMouseClick(MouseEvent mouseEvent) {
        FranemaApplication.stageManager.closePopup((Stage) cinemaInspectPanelPane.getScene().getWindow());
    }

    private void editButtonOnMouseClick(MouseEvent mouseEvent) {
        nameField.editableProperty().setValue(true);
        addressField.editableProperty().setValue(true);
        editButton.setText("Save");
        editButton.setOnMouseClicked(this::saveButtonOnMouseClick);
    }

    private void saveButtonOnMouseClick(MouseEvent mouseEvent) {
        nameField.editableProperty().setValue(false);
        addressField.editableProperty().setValue(false);
        editButton.setText("Edit");
        editButton.setOnMouseClicked(this::editButtonOnMouseClick);
        FranemaApplication.appProvider.getCurrentCinema().setName(nameField.getText());
        FranemaApplication.appProvider.getCurrentCinema().setAddress(addressField.getText());
        FranemaApplication.appProvider.saveCinema(FranemaApplication.appProvider.getCurrentCinema());
        try {
            FranemaApplication.stageManager.showScene(FranemaApplication.stageManager.getCurrentScene());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
