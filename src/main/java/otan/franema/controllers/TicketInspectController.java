package otan.franema.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import otan.franema.FranemaApplication;
import otan.franema.view.SceneEntity;

import java.io.IOException;

public class TicketInspectController {
    public BorderPane ticketInspectPanelPane;
    public Button editButton = new Button();
    public TextField nameField = new TextField();
    public TextField timeField = new TextField();
    public Button exitButton = new Button();
    public Button deleteButton = new Button();
    public Button buyButton = new Button();
    public VBox menu = new VBox();
    public VBox nameBox = new VBox();
    public Label nameLabel = new Label();
    public VBox addressBox = new VBox();
    public Label timeLabel = new Label();
    public ComboBox<String> cinemaPicker = new ComboBox<>();
    public Label cinemaLabel = new Label();
    public VBox cinemaBox = new VBox();
    public HBox buttons = new HBox();

    @FXML
    public void initialize() {
        System.out.println("TicketInspectController initialized!");
        nameField.setPromptText("Shrek");
        nameField.editableProperty().setValue(false);
        nameLabel.setText("Movie name:");
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER);

        timeField.setPromptText("YYYY-MM-DD HH:MM:SS");
        timeField.editableProperty().setValue(false);
        timeLabel.setText("Date and time:");
        addressBox.getChildren().addAll(timeLabel, timeField);
        addressBox.setAlignment(Pos.CENTER);

        cinemaLabel.setText("Cinema: (click for more info)");
        FranemaApplication.appProvider.getAllCinemas().forEach(cinema -> cinemaPicker.getItems().add(cinema.getName()));
        cinemaPicker.setOnShown(event -> cinemaPicker.hide());
        cinemaPicker.setOnMouseClicked(this::cinemaPickerOnMouseClick);
        cinemaBox.getChildren().addAll(cinemaLabel, cinemaPicker);
        cinemaBox.setAlignment(Pos.CENTER);

        nameField.setText(FranemaApplication.appProvider.getCurrentTicket().getMovie());
        timeField.setText(FranemaApplication.appProvider.getCurrentTicket().getDate());


        editButton.setText("Edit");
        editButton.setOnMouseClicked(this::editButtonOnMouseClick);

        exitButton.setText("Close");
        exitButton.setOnMouseClicked(this::exitButtonOnMouseClick);

        deleteButton.setText("Delete");
        deleteButton.setOnMouseClicked(this::deleteButtonOnMouseClick);

        buyButton.setText("Buy");
        buyButton.setOnMouseClicked(this::buyButtonOnMouseClick);

        buttons.getChildren().addAll(buyButton, exitButton);
        if (FranemaApplication.appProvider.getCurrentUser().isAdmin()) {
            buttons.getChildren().addAll(editButton, deleteButton);
        }
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        menu.getChildren().addAll(nameBox, addressBox, cinemaBox, buttons);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        ticketInspectPanelPane.setCenter(menu);

        if (FranemaApplication.appProvider.getCurrentTicket().getId() == 0) {
            nameField.editableProperty().setValue(true);
            timeField.editableProperty().setValue(true);
            cinemaPicker.setOnShown(null);
            editButton.setText("Save");
            editButton.setOnMouseClicked(this::saveButtonOnMouseClick);
        } else {
            cinemaPicker.getSelectionModel().select(FranemaApplication.appProvider.getCurrentTicket().getCinema().getName());
        }


        ticketInspectPanelPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            menu.setPrefWidth(ticketInspectPanelPane.getWidth());
            if (ticketInspectPanelPane.getWidth() > 500) {
                timeField.setMaxWidth(ticketInspectPanelPane.getWidth() / 2);
                nameField.setMaxWidth(ticketInspectPanelPane.getWidth() / 2);
            } else {
                timeField.setMaxWidth(ticketInspectPanelPane.getWidth());
                nameField.setMaxWidth(ticketInspectPanelPane.getWidth());
            }
        });
        ticketInspectPanelPane.heightProperty().addListener((observable, oldValue, newValue) -> menu.setPrefHeight(ticketInspectPanelPane.getHeight()));
    }

    private void deleteButtonOnMouseClick(MouseEvent mouseEvent) {
        FranemaApplication.appProvider.deleteTicket(FranemaApplication.appProvider.getCurrentTicket());
        FranemaApplication.stageManager.closePopup((Stage) ticketInspectPanelPane.getScene().getWindow());
        try {
            FranemaApplication.stageManager.showScene(FranemaApplication.stageManager.getCurrentScene());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void buyButtonOnMouseClick(MouseEvent mouseEvent) {
        FranemaApplication.appProvider.buyTicket(FranemaApplication.appProvider.getCurrentTicket());
        buyButton.setText("Ticket bought!");

        try {
            FranemaApplication.stageManager.showScene(FranemaApplication.stageManager.getCurrentScene());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buyButton.setText("Buy");
        });
    }

    private void cinemaPickerOnMouseClick(MouseEvent mouseEvent) {
        if (editButton.getText().equals("Edit")) {
            FranemaApplication.appProvider.setCurrentCinema(FranemaApplication.appProvider.getAllCinemas().get(cinemaPicker.getSelectionModel().getSelectedIndex()));
            try {
                FranemaApplication.stageManager.showPopup(SceneEntity.CINEMA_INSPECT);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void exitButtonOnMouseClick(MouseEvent mouseEvent) {
        FranemaApplication.stageManager.closePopup((Stage) ticketInspectPanelPane.getScene().getWindow());
    }

    private void editButtonOnMouseClick(MouseEvent mouseEvent) {
        cinemaPicker.setOnShown(null);
        nameField.editableProperty().setValue(true);
        timeField.editableProperty().setValue(true);
        editButton.setText("Save");
        editButton.setOnMouseClicked(this::saveButtonOnMouseClick);
    }

    private void saveButtonOnMouseClick(MouseEvent mouseEvent) {
        nameField.editableProperty().setValue(false);
        timeField.editableProperty().setValue(false);
        cinemaPicker.setOnShown(event -> cinemaPicker.hide());
        editButton.setText("Edit");
        editButton.setOnMouseClicked(this::editButtonOnMouseClick);
        FranemaApplication.appProvider.getCurrentTicket().setMovie(nameField.getText());
        FranemaApplication.appProvider.getCurrentTicket().setDate(timeField.getText());
        FranemaApplication.appProvider.getCurrentTicket().setCinema(FranemaApplication.appProvider.getAllCinemas().get(cinemaPicker.getSelectionModel().getSelectedIndex()));
        FranemaApplication.appProvider.saveTicket(FranemaApplication.appProvider.getCurrentTicket());
        try {
            FranemaApplication.stageManager.showScene(FranemaApplication.stageManager.getCurrentScene());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}